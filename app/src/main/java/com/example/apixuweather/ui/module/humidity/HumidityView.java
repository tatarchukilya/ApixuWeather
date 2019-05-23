package com.example.apixuweather.ui.module.humidity;

import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.apixuweather.R;
import com.example.apixuweather.ui.daily.recycler.PartDayItem;
import com.example.apixuweather.ui.root.recycler.HourItem;
import com.example.apixuweather.utils.SizeConverter;

import java.util.Locale;

public class HumidityView {

    private View mView;
    private TextView mHumidity;
    private ImageView mIcon;

    public HumidityView(View view) {
        mView = view;
        mHumidity = view.findViewById(R.id.humidity);
        mIcon = view.findViewById(R.id.humidity_icon);
    }

    public void bind(Integer humidity){
        mHumidity.setText(String.format(Locale.getDefault(), "%d%%", humidity));
    }

    public void bind(HourItem item){
        mHumidity.setText(item.getHumidity());
    }

    public void bind(PartDayItem item){
        mHumidity.setText(item.getHumidity());
    }

    public void setLarge(){
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(SizeConverter.dpToPx(24), SizeConverter.dpToPx(24));
        mIcon.setLayoutParams(params);
        mHumidity.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
    }
}
