package com.example.apixuweather.ui.settings.recycler;

import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.apixuweather.R;
import com.example.apixuweather.repo.ISharePreference;
import com.example.apixuweather.ui.base.BaseViewHolder;
import com.example.apixuweather.ui.base.ViewHolderTouchListener;
import com.example.apixuweather.utils.unit.SpeedUnit;

import androidx.annotation.NonNull;

public class WindViewHolder extends BaseViewHolder<WindSettingsItem> {

    public static final int LAYOUT = R.layout.view_settings_wind;

    public WindViewHolder(@NonNull View itemView, ViewHolderTouchListener listener) {
        super(itemView, listener);
        ((RadioGroup) itemView.findViewById(R.id.wind_group)).setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case -1:
                    break;
                case R.id.ms:
                    setSettings(SpeedUnit.Ms);
                    break;
                case R.id.Mph:
                    setSettings(SpeedUnit.Mph);
                    break;
                case R.id.Kph:
                    setSettings(SpeedUnit.Kph);
                    break;
            }
        });
    }

    @Override
    public void bind(WindSettingsItem model) {
        switch (ISharePreference.getSpeedUnit()) {
            case Ms:
                ((RadioButton) itemView.findViewById(R.id.ms)).setChecked(true);
                break;
            case Mph:
                ((RadioButton) itemView.findViewById(R.id.Mph)).setChecked(true);
                break;
            case Kph:
                ((RadioButton) itemView.findViewById(R.id.Kph)).setChecked(true);
                break;
        }
    }

    private void setSettings(SpeedUnit unit) {
        if (unit != ISharePreference.getSpeedUnit()) ISharePreference.setSpeedUnit(unit);
    }
}
