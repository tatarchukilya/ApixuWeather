package com.example.apixuweather.ui.module.pressure;


import com.example.apixuweather.repo.ISharePreference;
import com.example.apixuweather.rest.response.apixu.Current;
import com.example.apixuweather.rest.response.apixu.Hour;

public class PressureModel {

    private String mPressure;
    private Integer mPressureUnit;

    public PressureModel(Hour hour) {
        mPressure = hour.getPressure();
        mPressureUnit = ISharePreference.getPressureUnit().getId();
    }

    public PressureModel(Current current) {
        mPressureUnit = ISharePreference.getPressureUnit().getId();
        mPressure = current.getPressure();
    }

    public String getPressure() {
        return mPressure;
    }

    public Integer getPressureUnit() {
        return mPressureUnit;
    }
}
