package com.example.apixuweather.repo;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.widget.ImageView;

import com.example.apixuweather.IApplication;

import org.jetbrains.annotations.NotNull;

import androidx.annotation.DrawableRes;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;

import static com.example.apixuweather.utils.Const.I_TAG;

public class IconManager {

    public static final String TAG = I_TAG + IconManager.class.getSimpleName();

    public static final String NA_ICON = "w_0_white_static";

    public static Drawable getDrawable(String name) {

        Context context = IApplication.getInstance();

        try {
            return AppCompatResources.getDrawable(context, getDrawableResId(name));
        } catch (Resources.NotFoundException e) {
            Log.e("12345_", "Not Found Drawable " + name);
            return getNA();
        }
    }

    @DrawableRes
    public static int getDrawableResId(String name){
        Context context = IApplication.getInstance();
        return context.getResources().getIdentifier(name, "drawable", context.getPackageName());
    }

    public static void animate(@NotNull ImageView view) {
        animate(view.getDrawable());
    }

    public static void animate(Drawable d) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && d instanceof AnimatedVectorDrawable) {
            AnimatedVectorDrawable avd = (AnimatedVectorDrawable) d;
            avd.start();
        } else if (d instanceof AnimatedVectorDrawableCompat) {
            AnimatedVectorDrawableCompat avd = (AnimatedVectorDrawableCompat) d;
            avd.start();
        }
    }

    public static Drawable getNA(){
        Context context = IApplication.getInstance();
        final int drawableId = context.getResources().getIdentifier(NA_ICON, "drawable", context.getPackageName());
        return AppCompatResources.getDrawable(context, drawableId);
    }
}
