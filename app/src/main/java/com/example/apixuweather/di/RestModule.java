package com.example.apixuweather.di;

import com.example.apixuweather.rest.api.ApixuApi;
import com.example.apixuweather.rest.api.GoogleMapsApi;
import com.example.apixuweather.rest.client.ApixuClient;
import com.example.apixuweather.rest.client.GoogleMapsClient;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class RestModule {

    @Provides
    @Singleton
    GoogleMapsClient provideLocationClient(){
        return new GoogleMapsClient();
    }

    @Provides
    @Singleton
    GoogleMapsApi provideLocationApi(GoogleMapsClient client){
        return client.createService(GoogleMapsApi.class);
    }

    @Provides
    @Singleton
    ApixuClient provideApixuClient(){
        return new ApixuClient();
    }

    @Provides
    @Singleton
    ApixuApi provideApixuApi(ApixuClient client){
        return client.createService(ApixuApi.class);
    }
}
