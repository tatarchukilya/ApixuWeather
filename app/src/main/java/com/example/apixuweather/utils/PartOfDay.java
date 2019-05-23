package com.example.apixuweather.utils;

import com.example.apixuweather.R;

import androidx.annotation.StringRes;

public enum PartOfDay {

    Morning(R.string.morning), Afternoon(R.string.afternoon), Evening(R.string.evening), Night(R.string.humidity);

    private int mId;

    PartOfDay(int id) {
        mId = id;
    }

    @StringRes
    public int getId() {
        return mId;
    }
}
