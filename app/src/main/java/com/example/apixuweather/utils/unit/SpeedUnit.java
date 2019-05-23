package com.example.apixuweather.utils.unit;

import com.example.apixuweather.R;

import org.jetbrains.annotations.Contract;

import androidx.annotation.StringRes;

public enum SpeedUnit {

    Kph(R.string.Kph), Mph(R.string.Mph), Ms(R.string.Ms);

    private int mId;

    SpeedUnit(int id) {
        mId = id;
    }

    @Contract(pure = true)
    @StringRes
    public int getId() {
        return mId;
    }

    @Contract(pure = true)
    public static SpeedUnit toEnum(int id){
        switch (id){
            case R.string.Kph:
                return Kph;
            case R.string.Mph:
                return Mph;
            default:
                return Ms;
        }
    }
}
