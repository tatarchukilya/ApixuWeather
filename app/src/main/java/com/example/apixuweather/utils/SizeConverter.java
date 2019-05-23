package com.example.apixuweather.utils;

import android.util.TypedValue;

import com.example.apixuweather.IApplication;

public class SizeConverter {

    public static int dpToPx(float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, IApplication.getInstance().getResources().getDisplayMetrics());
    }

    public static int spToPx(float sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, IApplication.getInstance().getResources().getDisplayMetrics());
    }

    public static int dpToSp(float dp) {
        return (int) (dpToPx(dp) / IApplication.getInstance().getResources().getDisplayMetrics().scaledDensity);
    }
}
