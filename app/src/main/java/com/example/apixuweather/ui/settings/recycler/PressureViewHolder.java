package com.example.apixuweather.ui.settings.recycler;

import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.apixuweather.R;
import com.example.apixuweather.repo.ISharePreference;
import com.example.apixuweather.ui.base.BaseViewHolder;
import com.example.apixuweather.ui.base.ViewHolderTouchListener;
import com.example.apixuweather.utils.unit.PressureUnit;

import androidx.annotation.NonNull;

public class PressureViewHolder extends BaseViewHolder<PressureSettingsItem> {

    public static final int LAYOUT = R.layout.view_settings_pressure;

    public PressureViewHolder(@NonNull View itemView, ViewHolderTouchListener listener) {
        super(itemView, listener);

        ((RadioGroup)itemView.findViewById(R.id.pressure_group)).setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.mmHH:
                    setSettings(PressureUnit.mmHg);
                    break;
                case R.id.hPa:
                    setSettings(PressureUnit.hPa);
                    break;
                case R.id.inHg:
                    setSettings(PressureUnit.inHg);
                    break;
            }
        });
    }

    @Override
    public void bind(PressureSettingsItem model) {
        switch (ISharePreference.getPressureUnit()) {
            case mmHg:
                ((RadioButton) itemView.findViewById(R.id.mmHH)).setChecked(true);
                break;
            case hPa:
                ((RadioButton) itemView.findViewById(R.id.hPa)).setChecked(true);
                break;
            case inHg:
                ((RadioButton) itemView.findViewById(R.id.inHg)).setChecked(true);
                break;
        }
    }

    private void setSettings(PressureUnit unit) {
        if (ISharePreference.getPressureUnit() != unit) ISharePreference.setPressureUnit(unit);
    }
}
