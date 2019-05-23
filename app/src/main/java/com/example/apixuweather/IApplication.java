package com.example.apixuweather;

import android.app.Application;

import com.example.apixuweather.di.AppComponent;
import com.example.apixuweather.di.AppModule;
import com.example.apixuweather.di.DaggerAppComponent;
import com.jakewharton.threetenabp.AndroidThreeTen;

import org.jetbrains.annotations.Contract;

public class IApplication extends Application {

    private static IApplication sIApplication;
    private static AppComponent mAppComponent;

    public IApplication() {
        initComponent();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        AndroidThreeTen.init(this);
        sIApplication = this;
    }

    private void initComponent() {
     /*   sAppComponent = DaggerAppComponent
                .builder()
                .createAppBuilder(this)
                .buildAppComponent();*/

        mAppComponent = DaggerAppComponent.builder().appModule(new AppModule(this)).build();
    }

    @Contract(pure = true)
    public static IApplication getInstance() {
        return sIApplication;
    }

    @Contract(pure = true)
    public static AppComponent getAppComponent() {
        return mAppComponent;
    }
}
