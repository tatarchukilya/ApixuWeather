package com.example.apixuweather.ui.root.recycler;

import android.graphics.drawable.Drawable;

import com.example.apixuweather.IApplication;
import com.example.apixuweather.R;
import com.example.apixuweather.repo.IconManager;
import com.example.apixuweather.rest.response.apixu.Current;
import com.example.apixuweather.rest.response.apixu.Hour;
import com.example.apixuweather.ui.base.BaseItem;

import org.jetbrains.annotations.NotNull;

public class WeatherNowItem implements BaseItem {

    private String mTemp;
    private String mFeelsLike;
    private String mDescription;
    private Drawable mIcon;

    @Override
    public int getLayoutType() {
        return WeatherNowViewHolder.LAYOUT;
    }

    public WeatherNowItem(@NotNull Current current) {
        mTemp = current.getTemp();
        mFeelsLike = current.getFeelsLike();
        mDescription = current.getCondition().getText();
        mIcon = IconManager.getDrawable(current.getIconName(true));
    }

    public WeatherNowItem(Hour hour) {
        if (hour == null) {
            mTemp = "";
            mFeelsLike = "";
            mDescription = IApplication.getInstance().getResources().getString(R.string.no_data);
            mIcon = IconManager.getNA();
        } else {
            mTemp = hour.getTemp();
            mFeelsLike = "";
            mDescription = hour.getCondition().getText();
            mIcon = IconManager.getDrawable(hour.getIconName(true));
        }
    }

    public WeatherNowItem(String temp, String feelsLike, String description, Drawable icon) {
        mTemp = temp;
        mFeelsLike = feelsLike;
        mDescription = description;
        mIcon = icon;
    }

    public String getTemp() {
        return mTemp;
    }

    public String getFeelsLike() {
        return mFeelsLike;
    }

    public String getDescription() {
        return mDescription;
    }

    public Drawable getIcon() {
        return mIcon;
    }
}
