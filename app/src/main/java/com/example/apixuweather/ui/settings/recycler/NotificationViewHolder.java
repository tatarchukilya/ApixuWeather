package com.example.apixuweather.ui.settings.recycler;

import android.content.SharedPreferences;
import android.view.View;

import com.example.apixuweather.R;
import com.example.apixuweather.repo.ISharePreference;
import com.example.apixuweather.ui.base.BaseViewHolder;
import com.example.apixuweather.ui.base.ViewHolderTouchListener;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;

import static com.example.apixuweather.repo.ISharePreference.NOTIFICATION_KEY;

public class NotificationViewHolder extends BaseViewHolder<NotificationItem> implements SharedPreferences.OnSharedPreferenceChangeListener {

    @LayoutRes
    public static final int LAYOUT = R.layout.view_settings_notification;

    private SwitchCompat mSwitchCompat;

    public NotificationViewHolder(@NonNull View itemView, ViewHolderTouchListener listener) {
        super(itemView, listener);
        mSwitchCompat = itemView.findViewById(R.id.switch_notification);
        mSwitchCompat.setOnCheckedChangeListener((buttonView, isChecked) -> ISharePreference.setNotification(isChecked));
    }

    @Override
    public void bind(NotificationItem model) {
        ISharePreference.getPreferences().registerOnSharedPreferenceChangeListener(this);
        mSwitchCompat.setChecked(ISharePreference.isNotificationOn());
    }

    public void unbind() {
        ISharePreference.getPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(NOTIFICATION_KEY)) {
            mSwitchCompat.setChecked(ISharePreference.isNotificationOn());
        }
    }
}
