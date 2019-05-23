package com.example.apixuweather.di;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.apixuweather.IApplication;
import com.example.apixuweather.repo.DataBaseModel;
import com.example.apixuweather.repo.IconManager;
import com.example.apixuweather.repo.LocationModel;
import com.example.apixuweather.repo.NetModel;
import com.google.android.gms.location.FusedLocationProviderClient;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    private IApplication mApplication;

    public AppModule(IApplication application) {
        mApplication = application;
    }

    @Provides
    @Singleton
    Context provideContext() {
        return mApplication;
    }

    @Provides
    @Singleton
    FusedLocationProviderClient provideLocationClient() {
        return new FusedLocationProviderClient(mApplication);
    }

    @Provides
    @Singleton
    LocationModel provideLocationModel() {
        return new LocationModel();
    }

    @Provides
    @Singleton
    NetModel provideNetModel() {
        return new NetModel();
    }

    @Singleton
    @Provides
    DataBaseModel provideDataBase() {
        return new DataBaseModel();
    }

    @Singleton
    @Provides
    IconManager provideIconMansger() {
        return new IconManager();
    }

    @Singleton
    @Provides
    SharedPreferences provideSharePreference(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }
}
