package com.example.apixuweather.ui.root.recycler;

import android.graphics.drawable.Drawable;

import com.example.apixuweather.repo.IconManager;
import com.example.apixuweather.rest.response.apixu.Forecastday;
import com.example.apixuweather.ui.base.BaseItem;
import com.example.apixuweather.utils.IDateFormatter;

import org.threeten.bp.Instant;
import org.threeten.bp.OffsetDateTime;
import org.threeten.bp.ZoneOffset;

public class DailyItem implements BaseItem {

    private String mDayOfWeek;
    private String mDate;
    private String mTempMax;
    private String mTempMin;
    private Drawable mIcon;
    private long mDateEpoch;

    @Override
    public int getLayoutType() {
        return DailyViewHolder.LAYOUT;
    }

    public DailyItem(Forecastday forecastday) {

        OffsetDateTime dateTime = Instant.ofEpochSecond(forecastday.getDateEpoch()).atOffset(ZoneOffset.UTC);
        mDayOfWeek = dateTime.format(IDateFormatter.dayOfWeek());
        mDate = dateTime.format(IDateFormatter.dayAndMonth());

        mTempMax = forecastday.getTempMax();
        mTempMin = forecastday.getTempMin();

        mIcon = IconManager.getDrawable(forecastday.getDay().getIconName(false));

        mDateEpoch = forecastday.getDateEpoch();
    }

    public String getDayOfWeek() {
        return mDayOfWeek;
    }

    public String getDate() {
        return mDate;
    }

    public String getTempMax() {
        return mTempMax;
    }

    public String getTempMin() {
        return mTempMin;
    }

    public Drawable getIcon() {
        return mIcon;
    }

    public long getDateEpoch() {
        return mDateEpoch;
    }
}
