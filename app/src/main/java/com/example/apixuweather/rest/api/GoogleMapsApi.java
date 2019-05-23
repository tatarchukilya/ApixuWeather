package com.example.apixuweather.rest.api;

import com.example.apixuweather.rest.response.maps.ResponseAddress;
import com.example.apixuweather.rest.response.maps.ResponseDetails;
import com.example.apixuweather.rest.response.maps.ResponsePlace;
import com.example.apixuweather.rest.response.maps.ResponseTimeZone;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface GoogleMapsApi {

    @GET("place/autocomplete/json?")
    Observable<ResponsePlace> getPlaces(@QueryMap Map<String, String> map);

    @GET("place/details/json?")
    Observable<ResponseDetails> getDetails(@QueryMap Map<String, String> map);

    @GET("geocode/json?")
    Observable<ResponseAddress> getAddress(@QueryMap Map<String, String> map);

    @GET("timezone/json?")
    Observable<ResponseTimeZone> getTimeZone(@QueryMap Map<String, String> map);
}
