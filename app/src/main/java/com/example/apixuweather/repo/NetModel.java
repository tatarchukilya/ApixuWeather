package com.example.apixuweather.repo;

import android.location.Location;
import android.util.Log;

import com.example.apixuweather.IApplication;
import com.example.apixuweather.rest.api.ApixuApi;
import com.example.apixuweather.rest.api.GoogleMapsApi;
import com.example.apixuweather.rest.request.ApixuRequest;
import com.example.apixuweather.rest.request.maps.AddressRequest;
import com.example.apixuweather.rest.request.maps.DetailsRequest;
import com.example.apixuweather.rest.request.maps.PlaceRequest;
import com.example.apixuweather.rest.request.maps.TimeZoneRequest;
import com.example.apixuweather.rest.response.apixu.CurrentResponse;
import com.example.apixuweather.rest.response.apixu.DataLocation;
import com.example.apixuweather.rest.response.apixu.ForecastResponse;
import com.example.apixuweather.rest.response.maps.Prediction;
import com.example.apixuweather.rest.response.maps.ResponseAddress;
import com.example.apixuweather.rest.response.maps.ResponseDetails;
import com.example.apixuweather.rest.response.maps.ResponsePlace;
import com.example.apixuweather.rest.response.maps.ResponseTimeZone;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import static com.example.apixuweather.utils.Const.I_TAG;

public class NetModel {

    public static final String TAG = I_TAG + NetModel.class.getSimpleName();

    @Inject
    GoogleMapsApi mMapsApi;

    @Inject
    ApixuApi mApixuApi;

    public NetModel() {
        IApplication.getAppComponent().inject(this);
    }

    public Observable<CurrentResponse> getCurrent(double lat, double lng) {
        return mApixuApi.getCurrent(new ApixuRequest(lat, lng).toMap())
                .subscribeOn(Schedulers.io());
    }

    public Observable<ForecastResponse> getForecast(double lat, double lng) {
        return mApixuApi.getForecast(new ApixuRequest(lat, lng).toMap())
                .subscribeOn(Schedulers.io());
    }

    public Observable<ResponseAddress> getAddress(double lat, double lng) {
        return mMapsApi.getAddress(new AddressRequest(lat, lng).toMap())
                .subscribeOn(Schedulers.io());
    }

    public Observable<ResponsePlace> getPlace(String input) {
        return mMapsApi.getPlaces(new PlaceRequest(input).toMap())
                .subscribeOn(Schedulers.io());
    }

    public Observable<ResponseDetails> getDetail(String placeId) {
        return mMapsApi.getDetails(new DetailsRequest(placeId).toMap())
                .subscribeOn(Schedulers.io());
    }

    public Observable<ResponseTimeZone> getTimeZone(double lat, double lng) {
        return mMapsApi.getTimeZone(new TimeZoneRequest(lat, lng).toMap())
                .subscribeOn(Schedulers.io());
    }

    public Observable<ForecastResponse> addForecast(Location location) {
        return Observable.zip(getForecast(location.getLatitude(), location.getLongitude()),
                getAddress(location.getLatitude(), location.getLongitude()),
                getTimeZone(location.getLatitude(), location.getLongitude()),
                (response, responseAddress, responseTimeZone) -> {
                    response.setDataLocation(location, responseAddress, responseTimeZone);
                    return response;
                })
                .observeOn(Schedulers.computation());
    }

    public Observable<ForecastResponse> search(String input) {
        return getPlace(input)
                .switchMap(new Function<ResponsePlace, ObservableSource<? extends Prediction>>() {
                    @Override
                    public ObservableSource<Prediction> apply(ResponsePlace responsePlace) throws Exception {
                        return Observable.fromIterable(responsePlace.getPredictions());
                    }
                }).flatMap(new Function<Prediction, ObservableSource<ResponseDetails>>() {
                               @Override
                               public ObservableSource<ResponseDetails> apply(Prediction prediction) throws Exception {
                                   return getDetail(prediction.getPlaceId());
                               }
                           }, new BiFunction<Prediction, ResponseDetails, DataLocation>() {
                               @Override
                               public DataLocation apply(Prediction prediction, ResponseDetails details) throws Exception {
                                   return new DataLocation(prediction, details);
                               }
                           }
                ).flatMap(new Function<DataLocation, ObservableSource<? extends ForecastResponse>>() {
                    @Override
                    public ObservableSource<? extends ForecastResponse> apply(DataLocation dataLocation) throws Exception {
                        return getForecast(dataLocation.getLat(), dataLocation.getLng());
                    }
                }, new BiFunction<DataLocation, ForecastResponse, ForecastResponse>() {
                    @Override
                    public ForecastResponse apply(DataLocation dataLocation, ForecastResponse response) throws Exception {
                        response.setLocationData(dataLocation);
                        return response;
                    }
                }).retryWhen(new Function<Observable<Throwable>, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(Observable<Throwable> throwableObservable) throws Exception {
                        return throwableObservable.take(3).delay(1, TimeUnit.SECONDS);
                    }
                });
    }

    public Observable<ForecastResponse> refresh(DataLocation location){
        return getForecast(location.getLat(), location.getLng())
                .flatMap(new Function<ForecastResponse, Observable<ForecastResponse>>() {
                    @Override
                    public Observable<ForecastResponse> apply(ForecastResponse response) throws Exception {
                        response.setLocationData(location);
                        Log.i(TAG, response.getLocation().getRegion());
                        return Observable.just(response);
                    }
                })
                .subscribeOn(Schedulers.io());
    }
}
