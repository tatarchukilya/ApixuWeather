package com.example.apixuweather.ui.root.recycler;

import android.view.View;


import com.example.apixuweather.R;
import com.example.apixuweather.ui.base.BaseViewHolder;
import com.example.apixuweather.ui.base.ViewHolderTouchListener;
import com.example.apixuweather.ui.module.humidity.HumidityView;
import com.example.apixuweather.ui.module.pressure.PressureView;
import com.example.apixuweather.ui.module.wind.WindView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;

public class DetailNowViewHolder extends BaseViewHolder<DetailNowItem> {

    private WindView mWindView;
    private HumidityView mHumidityView;
    private PressureView mPressureView;

    @LayoutRes
    public static final int LAYOUT = R.layout.view_detail;

    public DetailNowViewHolder(@NonNull View itemView, ViewHolderTouchListener listener) {
        super(itemView, listener);
        mWindView = new WindView(itemView);
        mPressureView = new PressureView(itemView);
        mHumidityView = new HumidityView(itemView);
    }

    @Override
    public void bind(DetailNowItem model) {
        mWindView.bind(model.getWind());
        mPressureView.bind(model.getPressure());
        mHumidityView.bind(model.getHumidity());
    }
}
