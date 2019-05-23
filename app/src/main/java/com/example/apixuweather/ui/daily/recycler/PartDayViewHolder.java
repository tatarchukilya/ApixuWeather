package com.example.apixuweather.ui.daily.recycler;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.apixuweather.R;
import com.example.apixuweather.ui.base.BaseViewHolder;
import com.example.apixuweather.ui.base.ViewHolderTouchListener;
import com.example.apixuweather.ui.module.humidity.HumidityView;
import com.example.apixuweather.ui.module.pressure.PressureView;
import com.example.apixuweather.ui.module.temperature.TemperatureView;
import com.example.apixuweather.ui.module.wind.WindView;

import androidx.annotation.NonNull;
import butterknife.BindView;

public class PartDayViewHolder extends BaseViewHolder<PartDayItem> {

    public static final int LAYOUT = R.layout.view_part_of_day;

    private WindView mWind;
    private TemperatureView mTemp;
    private PressureView mPressure;
    private HumidityView mHumidity;

    @BindView(R.id.description)
    TextView mDescription;
    @BindView(R.id.icon)
    ImageView mIcon;
    @BindView(R.id.part_of_day)
    TextView mPartOfDay;

    public PartDayViewHolder(@NonNull View itemView, ViewHolderTouchListener listener) {
        super(itemView, listener);
        mWind = new WindView(itemView);
        mTemp = new TemperatureView(itemView);
        mPressure = new PressureView(itemView);
        mHumidity = new HumidityView(itemView);
    }

    @Override
    public void bind(PartDayItem model) {
        mWind.bind(model);
        mTemp.bind(model);
        mPressure.bind(model);
        mHumidity.bind(model);
        mDescription.setText(model.getDescription());
        mIcon.setImageDrawable(model.getIcon());
        mPartOfDay.setText(model.getPartOfDate());
    }
}
