package com.example.apixuweather.ui.daily.recycler;

import android.graphics.drawable.Drawable;


import com.example.apixuweather.repo.ISharePreference;
import com.example.apixuweather.repo.IconManager;
import com.example.apixuweather.rest.response.apixu.Hour;
import com.example.apixuweather.ui.base.BaseItem;
import com.example.apixuweather.utils.IDateFormatter;

import org.jetbrains.annotations.NotNull;
import org.threeten.bp.Instant;
import org.threeten.bp.OffsetDateTime;
import org.threeten.bp.ZoneOffset;

public class PartDayItem implements BaseItem {

    private Integer mPartOfDate;
    private Drawable mIcon;
    private String mDescription;
    private String mTemp;
    private String mWindSpeed;
    private Integer mWindSpeedUnit;
    private Integer mWindDirection;
    private String mPressure;
    private Integer mPressureUnit;
    private String mHumidity;

    @Override
    public int getLayoutType() {
        return PartDayViewHolder.LAYOUT;
    }

    public PartDayItem(@NotNull Hour hour, int timezone) {
        OffsetDateTime date = Instant.ofEpochSecond(hour.getTimeEpoch()).atOffset(ZoneOffset.ofTotalSeconds(timezone));
        mPartOfDate = IDateFormatter.getPartOfDay(date);
        mIcon = IconManager.getDrawable(hour.getIconName(false));
        mDescription = hour.getCondition().getText();
        mTemp = hour.getTemp();
        mWindSpeed = hour.getWind();
        mWindSpeedUnit = ISharePreference.getSpeedUnit().getId();
        mWindDirection = hour.getWindDirection();
        mPressure = hour.getPressure();
        mPressureUnit = ISharePreference.getPressureUnit().getId();
        mHumidity = hour.getHumidity() + "%";
    }

    public Integer getPartOfDate() {
        return mPartOfDate;
    }

    public Drawable getIcon() {
        return mIcon;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getTemp() {
        return mTemp;
    }

    public String getWindSpeed() {
        return mWindSpeed;
    }

    public Integer getWindSpeedUnit() {
        return mWindSpeedUnit;
    }

    public Integer getWindDirection() {
        return mWindDirection;
    }

    public String getPressure() {
        return mPressure;
    }

    public Integer getPressureUnit() {
        return mPressureUnit;
    }

    public String getHumidity() {
        return mHumidity;
    }
}
