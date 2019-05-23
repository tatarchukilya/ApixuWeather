package com.example.apixuweather.ui.settings.recycler;

import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.apixuweather.R;
import com.example.apixuweather.repo.ISharePreference;
import com.example.apixuweather.ui.base.BaseViewHolder;
import com.example.apixuweather.ui.base.ViewHolderTouchListener;
import com.example.apixuweather.utils.unit.TempUnit;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;

public class TempViewHolder extends BaseViewHolder<TempSettingsItem> {

    @LayoutRes
    public static final int LAYOUT = R.layout.view_settings_temp;

    private RadioGroup mRadioGroup;
    private RadioButton mFahrenheit;
    private RadioButton mCelsius;

    public TempViewHolder(@NonNull View itemView, ViewHolderTouchListener listener) {
        super(itemView, listener);

        ((RadioGroup) itemView.findViewById(R.id.temperature_group)).setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case -1:
                    break;
                case R.id.fahrenheit:
                    setSettings(TempUnit.Fahrenheit);
                    break;
                case R.id.celsius:
                    setSettings(TempUnit.Celsius);
                    break;
            }
        });
    }

    @Override
    public void bind(TempSettingsItem model) {
        switch (ISharePreference.getTempUnit()) {
            case Fahrenheit:
                ((RadioButton) itemView.findViewById(R.id.fahrenheit)).setChecked(true);
                break;
            case Celsius:
                ((RadioButton) itemView.findViewById(R.id.celsius)).setChecked(true);
        }
    }

    private void setSettings(TempUnit unit) {
        if (ISharePreference.getTempUnit() != unit) ISharePreference.setTempUnit(unit);
    }
}
