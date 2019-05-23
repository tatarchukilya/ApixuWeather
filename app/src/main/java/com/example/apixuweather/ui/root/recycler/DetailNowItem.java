package com.example.apixuweather.ui.root.recycler;


import com.example.apixuweather.rest.response.apixu.Current;
import com.example.apixuweather.ui.base.BaseItem;
import com.example.apixuweather.ui.module.pressure.PressureModel;
import com.example.apixuweather.ui.module.wind.WindModel;

public class DetailNowItem implements BaseItem {

    private WindModel mWind;
    private PressureModel mPressure;
    private Integer mHumidity;

    @Override
    public int getLayoutType() {
        return DetailNowViewHolder.LAYOUT;
    }

    public DetailNowItem(Current current) {
        mWind = new WindModel(current);
        mPressure = new PressureModel(current);
        mHumidity = current.getHumidity();
    }

    public WindModel getWind() {
        return mWind;
    }

    public PressureModel getPressure() {
        return mPressure;
    }

    public Integer getHumidity() {
        return mHumidity;
    }
}
