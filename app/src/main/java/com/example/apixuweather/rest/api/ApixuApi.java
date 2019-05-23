package com.example.apixuweather.rest.api;



import com.example.apixuweather.rest.response.apixu.CurrentResponse;
import com.example.apixuweather.rest.response.apixu.ForecastResponse;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface ApixuApi {

    // http://api.apixu.com/

    @GET("forecast.json?")
    Observable<ForecastResponse> getForecast(@QueryMap Map<String, String> map);

    @GET("current.json?")
    Observable<CurrentResponse> getCurrent(@QueryMap Map<String, String> map);

}
