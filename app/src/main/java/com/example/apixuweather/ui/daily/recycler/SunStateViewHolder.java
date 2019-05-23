package com.example.apixuweather.ui.daily.recycler;

import android.view.View;
import android.widget.TextView;


import com.example.apixuweather.R;
import com.example.apixuweather.ui.base.BaseViewHolder;
import com.example.apixuweather.ui.base.ViewHolderTouchListener;

import androidx.annotation.NonNull;
import butterknife.BindView;

public class SunStateViewHolder extends BaseViewHolder<SunStateItem> {

    public static final int LAYOUT = R.layout.view_sun_state;

    @BindView(R.id.sunrise)
    TextView mSunrise;
    @BindView(R.id.sunset)
    TextView mSunset;

    public SunStateViewHolder(@NonNull View itemView, ViewHolderTouchListener listener) {
        super(itemView, listener);
    }

    @Override
    public void bind(SunStateItem model) {
        mSunrise.setText(model.getSunrise());
        mSunset.setText(model.getSunset());
    }
}
