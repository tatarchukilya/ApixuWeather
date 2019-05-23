package com.example.apixuweather.ui.base;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.example.apixuweather.repo.ISharePreference;
import com.example.apixuweather.service.LocationService;
import com.example.apixuweather.utils.SystemUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


import static com.example.apixuweather.utils.Const.I_TAG;

public abstract class LocationActivity extends BaseActivity implements ServiceConnection {

    private final static String TAG = I_TAG + LocationActivity.class.getSimpleName();

    public static final int REQUEST_LOCATION_PERMISSION = 1;
    public static final int REQUEST_SERVICE_ERROR = 2;
    public static final int REQUEST_TURN_LOCATION = 3;

    private LocationService mLocationService;
    private boolean isServiceLocationBound = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        configLocation();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart");
        if (isLocationPermissionGranted()){
            enableLocating();
            startService();
        } else {
            requestLocationPermission();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        unBindService();
    }

    private void startService(){
        startService(new Intent(this, LocationService.class));
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(new Intent(this, LocationService.class));
        } else {
            startService(new Intent(this, LocationService.class));
        }*/
        bindService(new Intent(this, LocationService.class), this, Context.BIND_AUTO_CREATE);
    }

    private void unBindService() {
        if (isServiceLocationBound) {
            unbindService(this);
            isServiceLocationBound = false;
        }
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        LocationService.LocationBinder binder = (LocationService.LocationBinder) service;
        mLocationService = binder.getService();
        isServiceLocationBound = true;
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        mLocationService = null;
        isServiceLocationBound = false;
    }

    public void enableLocating() {
        Log.i(TAG, "enable location");
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(new LocationRequest().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY));
        SettingsClient client = LocationServices.getSettingsClient(this);

        client.checkLocationSettings(builder.build()).addOnFailureListener(this, e -> {
            if (e instanceof ResolvableApiException) {
                // LocationElement settings are not satisfied, but this can be fixed
                // by showing the user a dialog.
                try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
                    ResolvableApiException resolvable = (ResolvableApiException) e;
                    resolvable.startResolutionForResult(this, REQUEST_TURN_LOCATION);
                } catch (IntentSender.SendIntentException sendEx) {
                    // Ignore the error.
                }
            }
        });
    }

    public boolean isLocationPermissionGranted() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    public void requestLocationPermission() {
        Log.i(TAG, "request permission");
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            for (int i = 0; i < permissions.length; i++) {
                if (permissions[i].equals(Manifest.permission.ACCESS_FINE_LOCATION) && grantResults[i] == 0) {
                    enableLocating();
                    startService();
                } else {
                    ISharePreference.setNotification(false);
                }
            }
        }
    }

    public void configLocation() {
        // проверка на наличие google services
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int errorCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (errorCode != ConnectionResult.SUCCESS) {
            Log.e(TAG, "GOOGLE SERVICES NOT INSTALL");
            Dialog errorDialog = apiAvailability.getErrorDialog(this, errorCode, REQUEST_SERVICE_ERROR, dialog -> this.finish());
            errorDialog.show();
        } else {
            Log.i(TAG, "GOOGLE SERVICES INSTALL!");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppCompatActivity.RESULT_CANCELED){
            if (REQUEST_TURN_LOCATION == requestCode) {
                Log.i(TAG, "location disable");
                ISharePreference.setNotification(false);
            }
        }
    }
}
