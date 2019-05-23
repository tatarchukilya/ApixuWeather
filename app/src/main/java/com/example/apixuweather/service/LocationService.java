package com.example.apixuweather.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.location.Location;
import android.net.ConnectivityManager;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.apixuweather.IApplication;
import com.example.apixuweather.R;
import com.example.apixuweather.repo.DataBaseModel;
import com.example.apixuweather.repo.ISharePreference;
import com.example.apixuweather.repo.IconManager;
import com.example.apixuweather.repo.LocationModel;
import com.example.apixuweather.repo.NetModel;
import com.example.apixuweather.rest.error.ErrorHandlerAlter;
import com.example.apixuweather.rest.response.apixu.ForecastResponse;
import com.example.apixuweather.ui.MainActivity;
import com.example.apixuweather.utils.SystemUtils;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;

import org.jetbrains.annotations.NotNull;
import org.threeten.bp.Instant;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;
import androidx.core.util.Pair;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.disposables.Disposable;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

import static com.example.apixuweather.repo.ISharePreference.LOCATION_KEY;
import static com.example.apixuweather.repo.ISharePreference.NOTIFICATION_KEY;
import static com.example.apixuweather.repo.IconManager.NA_ICON;
import static com.example.apixuweather.utils.Const.I_TAG;

public class LocationService extends Service implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = I_TAG + LocationService.class.getSimpleName();
    private static final int NOTIFICATION_ID = 3011;
    private static final String CHANNEL_ID = "weather_channel_3011";

    @Inject
    LocationModel mLocationModel;

    @Inject
    NetModel mNetModel;

    @Inject
    DataBaseModel mDataBase;

    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder mBuilder;
    private RemoteViews mRemoteViews;
    private PendingIntent mPendingIntent;

    private LocationCallback mLocationCallback;

    private final IBinder mBinder = new LocationBinder();

    private Disposable mNetDisposable;
    private Disposable mDbDisposable;

    private BroadcastReceiver mScreenOnReceiver;
    private BroadcastReceiver mScreenOfReceiver;
    private BroadcastReceiver mNetReceiver;

    private long lastUpdate = 0;

    /**
     * -------------------------
     */
    public class LocationBinder extends Binder {
        public LocationService getService() {
            return LocationService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        getCurrentWeather();
        return mBinder;
    }

    /**
     * --------------------------
     */

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate");
        IApplication.getAppComponent().inject(this);
        ISharePreference.getPreferences().registerOnSharedPreferenceChangeListener(this);
        initNotification();
        registerScreenReceivers();
        mLocationModel.requestUpdate(getLocationCallBack());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ISharePreference.getPreferences().unregisterOnSharedPreferenceChangeListener(this);
        unregisterScreenReceiver();
        unregisterNetBroadcast();
        if (mNetDisposable != null) mNetDisposable.dispose();
        mLocationModel.removeUpdate(mLocationCallback);
        mLocationCallback = null;
        disposeDb();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand");
        if (ISharePreference.isNotificationOn()) startForeground();
        return Service.START_STICKY;
    }

    private LocationCallback getLocationCallBack() {
        if (mLocationCallback == null) {
            mLocationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    super.onLocationResult(locationResult);
                    mNetDisposable = Observable.fromIterable(locationResult.getLocations())
                            .doOnNext(location -> Log.i(TAG, "New Location Lat: " + location.getLatitude() + " Lng: " + location.getLongitude()))
                            .flatMap(new Function<Location, ObservableSource<? extends ForecastResponse>>() {
                                @Override
                                public ObservableSource<? extends ForecastResponse> apply(Location location1) throws Exception {
                                    if (Instant.now().getEpochSecond() - lastUpdate > 60) {
                                        lastUpdate = Instant.now().getEpochSecond();
                                        return mNetModel.addForecast(location1);
                                    } else {
                                        return Observable.empty();
                                    }
                                }
                            })
                            .subscribe(forecastResponse -> mDataBase.add(forecastResponse), new errorHandler());
                }
            };
        }
        return mLocationCallback;
    }

    private class errorHandler implements Consumer<Throwable> {

        @Override
        public void accept(Throwable throwable) {
            int resId = ErrorHandlerAlter.handelError(throwable);
            if (resId == R.string.no_network_connection) {
                registerNetBroadcast();
            }
        }
    }

    private void registerScreenReceivers() {
        if (mScreenOnReceiver == null) {
            mScreenOnReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    mLocationModel.requestUpdate(getLocationCallBack());
                    getCurrentWeather();
                    Log.i(TAG, "Screen On");
                }
            };
        }

        if (mScreenOfReceiver == null) {
            mScreenOfReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    mLocationModel.removeUpdate(mLocationCallback);
                    mLocationCallback = null;
                    Log.i(TAG, "Screen Of");
                }
            };
        }
        registerReceiver(mScreenOnReceiver, new IntentFilter(Intent.ACTION_SCREEN_ON));
        registerReceiver(mScreenOfReceiver, new IntentFilter(Intent.ACTION_SCREEN_OFF));
    }

    private void registerNetBroadcast() {
        Log.i(TAG, "register broadcast");
        try {
            if (mNetReceiver == null) {
                mNetReceiver = new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        Log.i(TAG, "broadcast onReceive");
                        if (SystemUtils.isConnected(LocationService.this)) {
                            mLocationModel.requestUpdate(getLocationCallBack());
                            unregisterReceiver(mNetReceiver);
                        }
                    }
                };
                registerReceiver(mNetReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
            }
        } catch (IllegalArgumentException e) {
            Log.i(TAG, "receiver is already registered", e);
        }
    }

    private void unregisterNetBroadcast() {
        try {
            if (mNetReceiver != null) {
                unregisterReceiver(mNetReceiver);
                mNetReceiver = null;
            }
        } catch (IllegalArgumentException e) {
            Log.e(TAG, "receiver is not registered ", e);
        }
    }

    private void unregisterScreenReceiver() {
        if (mScreenOnReceiver != null) {
            unregisterReceiver(mScreenOnReceiver);
            mScreenOnReceiver = null;
        }

        if (mScreenOfReceiver != null) {
            unregisterReceiver(mScreenOfReceiver);
            mScreenOfReceiver = null;
        }
    }

    /**
     * Notification
     */
    private void getCurrentWeather() {
        disposeDb();
        if (ISharePreference.isNotificationOn()) {
            mDbDisposable = Flowable.zip(
                    mDataBase.getCurrentLocForecast(),
                    mDataBase.getCurrentLocLastHour(), (forecastResponses, hours) -> {
                        Log.i(TAG, "update notification");
                        if (!forecastResponses.isEmpty()) {
                            if (forecastResponses.get(0).isRelevance()) {
                                return new NotifItem(forecastResponses.get(0));
                            } else if (!hours.isEmpty() && forecastResponses.get(0).isValidate()) {
                                return new NotifItem(forecastResponses.get(0), hours.get(0));
                            }
                        }
                        return new NotifItem();
                    }).subscribe(this::sendNotification);
        }
    }

    public void initNotification() {
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.app_name);
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT);
            mChannel.setSound(null, null);
            mNotificationManager.createNotificationChannel(mChannel);
        }
    }

    public void sendNotification(NotifItem item) {
        Log.i(TAG, "send notification");
        mNotificationManager.notify(NOTIFICATION_ID, getNotification(item));
    }

    private PendingIntent getPaddingIntent() {
        if (mPendingIntent == null) {
            Intent intent = new Intent(this, MainActivity.class);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
            stackBuilder.addNextIntentWithParentStack(intent);
            mPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        }
        return mPendingIntent;
    }

    private Notification getNotification(@NotNull NotifItem item) {
        Log.i(TAG, "getNotification");
        mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(IconManager.getDrawableResId(item.getDegIconName()))
                .setContent(bindNotificationLayout(item))
                .setContentIntent(getPaddingIntent());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mBuilder.setChannelId(CHANNEL_ID);
        }
        mBuilder.setPriority(Notification.PRIORITY_HIGH);

        return mBuilder.build();
    }

    private RemoteViews bindNotificationLayout(NotifItem item) {
        if (mRemoteViews == null)
            mRemoteViews = new RemoteViews(getPackageName(), R.layout.view_notification);

        mRemoteViews.setImageViewResource(R.id.icon, IconManager.getDrawableResId(item.getIconName()));
        if (item.getIconName().equals(NA_ICON)) {
            mRemoteViews.setTextViewText(R.id.address, getResources().getString(R.string.no_data));
            return mRemoteViews;
        }

        mRemoteViews.setTextViewText(R.id.wind_title, getResources().getString(R.string.wind) + ":");
        mRemoteViews.setTextViewText(R.id.pressure_title, getResources().getString(R.string.pressure) + ":");
        //mRemoteViews.setImageViewResource(R.id.icon, IconManager.getDrawableResId(item.getIconName()));
        mRemoteViews.setTextViewText(R.id.address, createAddress(item.getAddress()));
        mRemoteViews.setTextViewText(R.id.temperature, item.getTemp());
        mRemoteViews.setTextViewText(R.id.wind_speed, item.getWindSpeed().equals("0") ? getResources().getString(R.string.calm) : item.getWindSpeed());
        mRemoteViews.setTextViewText(R.id.wind_speed_units, item.getWindSpeed().equals("0") ? "" : getResources().getString(item.getWindSpeedUnit()) + ",");
        mRemoteViews.setTextViewText(R.id.wind_direction, item.getWindSpeed().equals("0") ? "" : getResources().getString(item.getWindDirection()));
        mRemoteViews.setTextViewText(R.id.pressure, item.getPressure());
        mRemoteViews.setTextViewText(R.id.pressure_unit, getResources().getString(item.getPressureUnit()));
        return mRemoteViews;
    }

    private void disposeDb() {
        if (mDbDisposable != null) mDbDisposable.dispose();
    }

    /**
     * ---------------
     */

    @NotNull
    private String createAddress(@NotNull Pair<String, String> address) {
        String c = ", ";
        if ((address.first == null || address.first.equals("")) || (address.second == null || address.second.equals(""))) {
            c = "";
        }
        return address.first + c + address.second;
    }

    private void startForeground() {
        startForeground(NOTIFICATION_ID, getNotification(new NotifItem()));
        getCurrentWeather();
    }

    private void stopForeground() {
        stopForeground(true);
        disposeDb();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(LOCATION_KEY)) {
            return;
        }
        if (key.equals(NOTIFICATION_KEY)) {
            if (ISharePreference.isNotificationOn()) {
                Log.i(TAG, "start foreground");
                mLocationModel.requestUpdate(getLocationCallBack());
                startForeground();
            } else {
                Log.i(TAG, "stop foreground");
                stopForeground();
            }
            return;
        }
        getCurrentWeather();
    }
}
