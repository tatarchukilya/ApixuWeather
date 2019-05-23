package com.example.apixuweather.ui.module.pressure;

import android.content.res.Resources;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.apixuweather.R;
import com.example.apixuweather.ui.daily.recycler.PartDayItem;
import com.example.apixuweather.ui.root.recycler.HourItem;
import com.example.apixuweather.utils.SizeConverter;

public class PressureView {

    private Resources mResources;
    private TextView mPressure;
    private TextView mPressureUnit;
    private ImageView mIcon;

    public PressureView(View view) {
        mResources = view.getResources();
        mPressure = view.findViewById(R.id.pressure);
        mPressureUnit = view.findViewById(R.id.pressure_unit);
        mIcon = view.findViewById(R.id.temp_icon);
    }

    public void bind(PressureModel item){
        mPressure.setText(item.getPressure());
        mPressureUnit.setText(mResources.getString(item.getPressureUnit()));
    }

    public void bind(HourItem item){
        mPressure.setText(item.getPressure());
        mPressureUnit.setText(item.getPressureUnit());
    }

    public void bind(PartDayItem item){
        mPressure.setText(item.getPressure());
        mPressureUnit.setText(item.getPressureUnit());
    }

    public void setLarge(){
        mPressure.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        mIcon.setLayoutParams(new LinearLayout.LayoutParams(SizeConverter.dpToPx(24), SizeConverter.dpToPx(24)));
    }
}
