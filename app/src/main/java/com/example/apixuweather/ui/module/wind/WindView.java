package com.example.apixuweather.ui.module.wind;

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

public class WindView {

    Resources mResources;
    TextView mSpeed;
    TextView mUnit;
    TextView mDirection;
    ImageView mIcon;

    public WindView(View view) {
        mResources = view.getResources();
        mSpeed = view.findViewById(R.id.wind_speed);
        mUnit = view.findViewById(R.id.wind_speed_units);
        mDirection = view.findViewById(R.id.wind_direction);
        mIcon = view.findViewById(R.id.wind_icon);
    }

    public void bind(WindModel model) {
        if (model.getWindSpeed().equals("0")) {
            setCalm();
        } else {
            mSpeed.setText(model.getWindSpeed());
            mUnit.setText(String.format("%s%s", mResources.getString(model.getSpeedUnit()), model.getWindDirection() == -1 ? "" : ","));
            if (model.getWindDirection() != -1)
                mDirection.setText(mResources.getString(model.getWindDirection()));
        }
    }

    public void bind(PartDayItem model) {
        if (model.getWindSpeed().equals("0")) {
            setCalm();
        } else {
            mSpeed.setText(model.getWindSpeed());
            mUnit.setText(String.format("%s%s", mResources.getString(model.getWindSpeedUnit()), model.getWindDirection() == -1 ? "" : ","));
            if (model.getWindDirection() != -1)
                mDirection.setText(mResources.getString(model.getWindDirection()));
        }
    }

    public void bind(HourItem item) {
        if (item.getWindSpeed().equals("0")) {
            setCalm();
        } else {
            mSpeed.setText(item.getWindSpeed());
            mUnit.setText(item.getPressureUnit());
            mDirection.setText(item.getWindDir());
        }
    }

    private void setCalm() {
        mSpeed.setText(mResources.getString(R.string.calm));
    }

    public void setLarge() {
        mSpeed.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        mUnit.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        mDirection.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        mIcon.setLayoutParams(new LinearLayout.LayoutParams(SizeConverter.dpToPx(24), SizeConverter.dpToPx(24)));
    }
}
