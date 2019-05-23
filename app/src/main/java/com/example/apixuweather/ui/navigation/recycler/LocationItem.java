package com.example.apixuweather.ui.navigation.recycler;

import android.graphics.drawable.Drawable;

import com.example.apixuweather.R;
import com.example.apixuweather.repo.ISharePreference;
import com.example.apixuweather.repo.IconManager;
import com.example.apixuweather.rest.response.apixu.ForecastResponse;
import com.example.apixuweather.rest.response.apixu.Hour;
import com.example.apixuweather.ui.base.BaseItem;

import androidx.annotation.ColorRes;

import org.jetbrains.annotations.NotNull;

import static com.example.apixuweather.utils.Const.CURRENT_LOCATION_ID;

public class LocationItem implements BaseItem {

    private String mLocationId;
    private String mFirstName;
    private String mSecondName;
    private String mTemp;
    private Drawable mIcon;
    @ColorRes
    private int mColor;

    @Override
    public int getLayoutType() {
        return mLocationId.equals(CURRENT_LOCATION_ID) ? CurrentLocationViewHolder.LAYOUT : LocationViewHolder.LAYOUT;
    }

    public LocationItem(@NotNull ForecastResponse response) {
        mLocationId = response.getLocationId();
        mFirstName = response.getLocation().getName();
        mSecondName = response.getLocation().getRegion();
        mTemp = response.getCurrent().getTemp();
        mIcon = IconManager.getDrawable(response.getCurrent().getIconName(true));
        mColor = ISharePreference.getLocationId().equals(mLocationId) ? R.color.colorAccent : R.color.colorSecondaryText;
    }

    public LocationItem(@NotNull ForecastResponse response, @NotNull Hour hour) {
        mLocationId = response.getLocationId();
        mFirstName = response.getLocation().getName();
        mSecondName = response.getLocation().getRegion();
        mColor = ISharePreference.getLocationId().equals(mLocationId) ? R.color.colorAccent : R.color.colorSecondaryText;

        if (!response.isValidate()) {
            mTemp = "-";
            mIcon = IconManager.getDrawable("w_0_color_static");
            return;
        }

        if (response.isRelevance()) {
            mTemp = response.getCurrent().getTemp();
            mIcon = IconManager.getDrawable(response.getCurrent().getIconName(true));
        } else {
            mTemp = hour.getTemp();
            mIcon = IconManager.getDrawable(hour.getIconName(true));
        }
    }

    public String getLocationId() {
        return mLocationId;
    }

    public String getFirstName() {
        return mFirstName;
    }

    public String getSecondName() {
        return mSecondName;
    }

    public String getTemp() {
        return mTemp;
    }

    public Drawable getIcon() {
        return mIcon;
    }

    @ColorRes
    public int getColor() {
        return mColor;
    }
}
