package com.example.apixuweather.utils;

import android.graphics.Bitmap;
import android.view.View;

import org.jetbrains.annotations.NotNull;

public class Screenshot {

    public static Bitmap takescreenshot(@NotNull View v) {
        v.setDrawingCacheEnabled(true);
        v.buildDrawingCache(true);
        Bitmap b = Bitmap.createBitmap(v.getDrawingCache());
        v.setDrawingCacheEnabled(false);
        return b;
    }

    public static Bitmap takescreenshotOfRootView(@NotNull View v) {
        return takescreenshot(v.getRootView());
    }
}
