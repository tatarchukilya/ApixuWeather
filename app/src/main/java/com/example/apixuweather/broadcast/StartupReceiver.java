package com.example.apixuweather.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.example.apixuweather.repo.ISharePreference;
import com.example.apixuweather.service.LocationService;

import static com.example.apixuweather.utils.Const.I_TAG;

public class StartupReceiver extends BroadcastReceiver {

    private static final String TAG = I_TAG + StartupReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "Received broadcast intent: " + intent.getAction());
        if (ISharePreference.isNotificationOn()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(new Intent(context, LocationService.class));
            } else {
                context.startService(new Intent(context, LocationService.class));
            }
        }
    }
}
