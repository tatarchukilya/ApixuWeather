package com.example.apixuweather.repo;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.apixuweather.IApplication;
import com.example.apixuweather.R;
import com.example.apixuweather.utils.unit.PressureUnit;
import com.example.apixuweather.utils.unit.SpeedUnit;
import com.example.apixuweather.utils.unit.TempUnit;

import org.jetbrains.annotations.NotNull;

import static com.example.apixuweather.utils.Const.CURRENT_LOCATION_ID;
import static com.example.apixuweather.utils.Const.I_TAG;

public class ISharePreference {

    public static final String TAG = I_TAG + ISharePreference.class.getSimpleName();

    public static final String TEMP_KEY = "temperature_unit";
    public static final String PRESSURE_KEY = "pressure_unit";
    public static final String SPEED_KEY = "speed_unit";
    public static final String LOCATION_KEY = "location_id";
    public static final String NOTIFICATION_KEY = "is_notification";
    public static final String ROOT_DIALOG_KEY = "is_root_dialog_show";

    public static void setTempUnit(@NotNull TempUnit unit) {
        getPreferences()
                .edit()
                .putInt(TEMP_KEY, unit.getId())
                .apply();
    }

    public static TempUnit getTempUnit() {
        return TempUnit.toEnum(getPreferences().getInt(TEMP_KEY, 0));
    }

    public static boolean isNotificationOn() {
        return getPreferences().getBoolean(NOTIFICATION_KEY, true);
    }

    public static void setNotification(boolean isOn) {
        Log.i(TAG, "notification: " + isOn);
        getPreferences()
                .edit()
                .putBoolean(NOTIFICATION_KEY, isOn)
                .apply();
    }

    public static void setSpeedUnit(@NotNull SpeedUnit unit) {
        getPreferences()
                .edit()
                .putInt(SPEED_KEY, unit.getId())
                .apply();
    }

    public static SpeedUnit getSpeedUnit() {
        return SpeedUnit.toEnum(getPreferences().getInt(SPEED_KEY, R.string.Ms));
    }

    public static void setPressureUnit(@NotNull PressureUnit unit) {
        getPreferences()
                .edit()
                .putInt(PRESSURE_KEY, unit.getId())
                .apply();
    }

    public static PressureUnit getPressureUnit() {
        return PressureUnit.toEnum(getPreferences()
                .getInt(PRESSURE_KEY, R.string.mmHg));
    }

    public static void setLocationId(String id) {
        getPreferences()
                .edit()
                .putString(LOCATION_KEY, id)
                .apply();
    }

    public static String getLocationId() {
        return getPreferences().getString(LOCATION_KEY, CURRENT_LOCATION_ID);
    }

    public static boolean isRootDialogShow() {
        return getPreferences().getBoolean(ROOT_DIALOG_KEY, false);
    }

    public static void setRootDialogShowed() {
        getPreferences()
                .edit()
                .putBoolean(ROOT_DIALOG_KEY, true)
                .apply();
    }

    public static SharedPreferences getPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(IApplication.getInstance());
    }
}
