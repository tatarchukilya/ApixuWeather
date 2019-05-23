package com.example.apixuweather.rest.client;

import com.example.apixuweather.rest.response.MapDeserializer;
import com.example.apixuweather.rest.response.maps.ResponseAddress;
import com.example.apixuweather.rest.response.maps.ResponseDetails;
import com.example.apixuweather.rest.response.maps.ResponsePlace;
import com.example.apixuweather.rest.response.maps.ResponseTimeZone;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GoogleMapsClient extends RestClient {

    @Override
    protected String getBaseUrl() {
        return "https://maps.googleapis.com/maps/api/";
    }

    @Override
    Gson createGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        MapDeserializer deserializer = new MapDeserializer<>();
        gsonBuilder.registerTypeAdapter(ResponseAddress.class, deserializer);
        gsonBuilder.registerTypeAdapter(ResponseDetails.class, deserializer);
        gsonBuilder.registerTypeAdapter(ResponsePlace.class, deserializer);
        gsonBuilder.registerTypeAdapter(ResponseTimeZone.class, deserializer);
        return gsonBuilder.create();
    }
}
