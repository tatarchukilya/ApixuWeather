package com.example.apixuweather.utils.unit;

import com.example.apixuweather.R;

import org.jetbrains.annotations.Contract;

import androidx.annotation.StringRes;

public enum PressureUnit {
    hPa(R.string.hPa), inHg(R.string.inHg), mmHg(R.string.mmHg);

    private int id;

    PressureUnit(int id) {
        this.id = id;
    }

    @Contract(pure = true)
    @StringRes
    public int getId() {
        return id;
    }

    @Contract(pure = true)
    public static PressureUnit toEnum(int id){
        switch (id){
            case R.string.hPa:
                return hPa;
            case R.string.inHg:
                return inHg;
            default:
                return mmHg;
        }
    }
}
