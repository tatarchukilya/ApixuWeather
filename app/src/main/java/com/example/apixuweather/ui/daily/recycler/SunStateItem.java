package com.example.apixuweather.ui.daily.recycler;
import com.example.apixuweather.rest.response.apixu.Astro;
import com.example.apixuweather.ui.base.BaseItem;
import com.example.apixuweather.utils.IDateFormatter;

import org.threeten.bp.LocalTime;

public class SunStateItem implements BaseItem {

    private String mSunrise;
    private String mSunset;

    @Override
    public int getLayoutType() {
        return SunStateViewHolder.LAYOUT;
    }

    public SunStateItem(Astro astro) {
        mSunrise = LocalTime.parse(astro.getSunrise(), IDateFormatter.time12()).format(IDateFormatter.time24());
        mSunset = LocalTime.parse(astro.getSunset(), IDateFormatter.time12()).format(IDateFormatter.time24());
    }

    public SunStateItem() {
    }

    public String getSunrise() {
        return mSunrise;
    }

    public String getSunset() {
        return mSunset;
    }

    public void setSunrise(String sunrise) {
        mSunrise = sunrise;
    }

    public void setSunset(String sunset) {
        mSunset = sunset;
    }
}
