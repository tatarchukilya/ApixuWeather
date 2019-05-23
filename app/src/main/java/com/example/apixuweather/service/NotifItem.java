package com.example.apixuweather.service;

import android.util.Log;

import com.example.apixuweather.repo.ISharePreference;
import com.example.apixuweather.rest.response.apixu.ForecastResponse;
import com.example.apixuweather.rest.response.apixu.Hour;

import org.jetbrains.annotations.NotNull;

import androidx.annotation.StringRes;
import androidx.core.util.Pair;

import static com.example.apixuweather.repo.IconManager.NA_ICON;
import static com.example.apixuweather.utils.Const.I_TAG;

public class NotifItem {

    public static final String TAG = I_TAG + NotifItem.class.getSimpleName();

    private String mIconName;

    private Pair<String, String> mAddress;

    private String mTemp;

    private String mWindSpeed;
    @StringRes
    private int mWindSpeedUnit;
    @StringRes
    private int mWindDirection;

    private String mPressure;
    @StringRes
    private int mPressureUnit;

    private String mDegIconName;

    public NotifItem(@NotNull ForecastResponse response) {
        mIconName = response.getCurrent().getIconName(false);
        mAddress = response.getLocation().getAddress();
        mTemp = response.getCurrent().getTemp();
        mWindSpeed = response.getCurrent().getWind();
        mWindSpeedUnit = ISharePreference.getSpeedUnit().getId();
        mWindDirection = response.getCurrent().getWindDirection();
        mPressure = response.getCurrent().getPressure();
        mPressureUnit = ISharePreference.getPressureUnit().getId();
        mDegIconName = response.getCurrent().getDegIconName();
    }

    public NotifItem(@NotNull ForecastResponse response, @NotNull Hour hour) {

        mAddress = response.getLocation().getAddress();
        mIconName = hour.getIconName(false);
        mTemp = hour.getTemp();
        mWindSpeed = hour.getWind();
        mWindSpeedUnit = ISharePreference.getSpeedUnit().getId();
        mWindDirection = hour.getWindDirection();
        mPressure = hour.getPressure();
        mPressureUnit = ISharePreference.getPressureUnit().getId();
        mDegIconName = hour.getDegIconName();
    }

    public NotifItem() {
        mIconName = NA_ICON;
    }

    public String getIconName() {
        return mIconName;
    }

    public Pair<String, String> getAddress() {
        return mAddress;
    }

    public String getTemp() {
        return mTemp;
    }

    public String getWindSpeed() {
        return mWindSpeed;
    }

    public int getWindSpeedUnit() {
        return mWindSpeedUnit;
    }

    public int getWindDirection() {
        return mWindDirection;
    }

    public String getPressure() {
        return mPressure;
    }

    public int getPressureUnit() {
        return mPressureUnit;
    }

    public String getDegIconName() {
        return mDegIconName == null ? NA_ICON : mDegIconName;
    }
}
