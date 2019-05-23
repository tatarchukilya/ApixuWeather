package com.example.apixuweather.ui.module.wind;


import com.example.apixuweather.repo.ISharePreference;
import com.example.apixuweather.rest.response.apixu.Current;
import com.example.apixuweather.rest.response.apixu.Day;
import com.example.apixuweather.rest.response.apixu.Hour;

public class WindModel {

    private String mWindSpeed;
    private Integer mSpeedUnit;
    private Integer mWindDirection;

    public WindModel(Hour hour) {
        mWindSpeed = hour.getWind();
        mSpeedUnit = ISharePreference.getSpeedUnit().getId();
        mWindDirection = hour.getWindDirection();
    }

    public WindModel(Current current) {
        mWindSpeed = current.getWind();
        mSpeedUnit = ISharePreference.getSpeedUnit().getId();
        mWindDirection = current.getWindDirection();
    }

    public WindModel(Day day) {
        mWindSpeed = day.getWind();
        mSpeedUnit = ISharePreference.getSpeedUnit().getId();
    }

    public String getWindSpeed() {
        return mWindSpeed;
    }

    public Integer getSpeedUnit() {
        return mSpeedUnit;
    }

    public Integer getWindDirection() {
        return mWindDirection;
    }
}
