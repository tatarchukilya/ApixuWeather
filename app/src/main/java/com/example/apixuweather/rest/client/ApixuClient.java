package com.example.apixuweather.rest.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ApixuClient extends RestClient {

    @Override
    Gson createGson() {
        return new GsonBuilder().create();
    }

    @Override
    protected String getBaseUrl() {
        return "https://api.apixu.com/v1/";
    }
}
