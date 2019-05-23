package com.example.apixuweather.ui.module.temperature;

import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.apixuweather.R;
import com.example.apixuweather.ui.daily.recycler.PartDayItem;
import com.example.apixuweather.ui.root.recycler.HourItem;
import com.example.apixuweather.utils.SizeConverter;

public class TemperatureView {

    private TextView mTemperature;
    private ImageView mIcon;

    public TemperatureView(View view) {
        mTemperature = view.findViewById(R.id.temperature);
        mIcon = view.findViewById(R.id.temp_icon);
    }

    public void bind(String temp) {
        mTemperature.setText(temp);
    }

    public void bind(HourItem item) {
        mTemperature.setText(item.getTemp());
    }

    public void bind(PartDayItem model) {
        mTemperature.setText(model.getTemp());
    }

    public void setLarge() {
        mTemperature.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(SizeConverter.dpToPx(24), SizeConverter.dpToPx(24));
        mIcon.setLayoutParams(params);
    }
}
