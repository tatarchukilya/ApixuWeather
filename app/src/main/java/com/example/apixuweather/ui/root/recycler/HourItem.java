package com.example.apixuweather.ui.root.recycler;

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

public class HourItem implements BaseItem {

    private String mTime;
    private String mTemp;
    private String mDescription;
    private Drawable mIcon;
    private String mPressure;
    private Integer mPressureUnit;
    private String mWindSpeed;
    private Integer mWindUnit;
    private Integer mWindDir;
    private String mHumidity;

    @Override
    public int getLayoutType() {
        return HourViewHolder.LAYOUT;
    }

    public HourItem(@NotNull Hour hour, int offset) {
        //LocalDateTime date = Instant.ofEpochSecond(hour.getTimeEpoch()).atZone(ZoneId.systemDefault()).toLocalDateTime();
        OffsetDateTime dateTime = Instant.ofEpochSecond(hour.getTimeEpoch()).atOffset(ZoneOffset.ofTotalSeconds(offset));
        mTime = dateTime.format(IDateFormatter.time24());
        mTemp = hour.getTemp();
        mDescription = hour.getCondition().getText();
        mIcon = IconManager.getDrawable(hour.getIconName(true));
        mPressure = hour.getPressure();
        mPressureUnit = ISharePreference.getPressureUnit().getId();
        mWindSpeed = hour.getWind();
        mWindDir = hour.getWindDirection();
        mWindUnit = ISharePreference.getSpeedUnit().getId();
        mHumidity = hour.getHumidity() + "%";

    }

    public String getTime() {
        return mTime;
    }

    public String getTemp() {
        return mTemp;
    }

    public Drawable getIcon() {
        return mIcon;
    }

    public String getPressure() {
        return mPressure;
    }

    public Integer getPressureUnit() {
        return mPressureUnit;
    }

    public String getWindSpeed() {
        return mWindSpeed;
    }

    public Integer getWindUnit() {
        return mWindUnit;
    }

    public Integer getWindDir() {
        return mWindDir;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getHumidity() {
        return mHumidity;
    }
}
