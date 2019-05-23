package com.example.apixuweather.ui.root;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.example.apixuweather.IApplication;
import com.example.apixuweather.R;
import com.example.apixuweather.repo.DataBaseModel;
import com.example.apixuweather.repo.ISharePreference;
import com.example.apixuweather.repo.IconManager;
import com.example.apixuweather.repo.NetModel;
import com.example.apixuweather.rest.error.ErrorHandler;
import com.example.apixuweather.rest.response.apixu.DataLocation;
import com.example.apixuweather.rest.response.apixu.ForecastResponse;
import com.example.apixuweather.rest.response.apixu.Forecastday;

import com.example.apixuweather.ui.base.BaseItem;
import com.example.apixuweather.ui.root.recycler.DailyItem;
import com.example.apixuweather.ui.root.recycler.HoursItem;
import com.example.apixuweather.ui.root.recycler.WeatherNowItem;
import com.example.apixuweather.utils.SystemUtils;


import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import androidx.core.util.Pair;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

import static com.example.apixuweather.repo.ISharePreference.NOTIFICATION_KEY;
import static com.example.apixuweather.utils.Const.I_TAG;

public class RootViewModel extends AndroidViewModel implements SharedPreferences.OnSharedPreferenceChangeListener {

    public static final String TAG = I_TAG + RootViewModel.class.getSimpleName();

    @Inject
    DataBaseModel mDataBaseModel;

    @Inject
    NetModel mNetModel;

    private BroadcastReceiver mNetReceiver;


    private MutableLiveData<Pair<String, String>> mAddressLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> mLoadStateMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<List<BaseItem>> mItems = new MutableLiveData<>();

    private Disposable mDbDisposable;
    private CompositeDisposable mRefreshDisposable;

    public RootViewModel(Application application) {
        super(application);
        IApplication.getAppComponent().inject(this);
        mRefreshDisposable = new CompositeDisposable();
        ISharePreference.getPreferences().registerOnSharedPreferenceChangeListener(this);
        getForecast();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        unregisterNetBroadcast();
        disposableDb();
        mRefreshDisposable.dispose();
    }

    private void disposableDb() {
        if (mDbDisposable != null) mDbDisposable.dispose();
    }

    private void setItems(List<BaseItem> items) {
        mItems.postValue(items);
    }

    public MutableLiveData<List<BaseItem>> getItems() {
        return mItems;
    }

    private void setAddress(@NotNull List<ForecastResponse> responses) {
        if (!responses.isEmpty()) {
            mAddressLiveData.postValue(responses.get(0).getLocation().getAddress());
        } else {
            mAddressLiveData.postValue(new Pair<>(" ", " "));
        }
    }

    public MutableLiveData<Pair<String, String>> getAddress() {
        return mAddressLiveData;
    }

    private void getForecast() {
        String id = ISharePreference.getLocationId();
        disposableDb();
        mDbDisposable = Flowable.zip(
                mDataBaseModel.getForecastResponse(id).doOnNext(this::setAddress),
                mDataBaseModel.getNext24(id),
                mDataBaseModel.getDaily(id),
                (forecastResponses, hours, forecastdays) -> {
                    List<BaseItem> result = new ArrayList<>();

                    if (forecastResponses.isEmpty() || !forecastResponses.get(0).isValidate()) {
                        result.add(new WeatherNowItem("", "", IApplication.getInstance().getResources().getString(R.string.no_data), IconManager.getNA()));
                        return result;
                    } else {
                        ForecastResponse response = forecastResponses.get(0);
                        result.add(response.isRelevance() ? new WeatherNowItem(response.getCurrent()) : new WeatherNowItem(hours.get(0)));
                        result.add(new HoursItem(forecastResponses.get(0), hours));
                        for (Forecastday forecastday : forecastdays) {
                            result.add(new DailyItem(forecastday));
                        }
                    }
                    return result;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::setItems, ErrorHandler::handelError);
    }

    public void refresh() {
        mRefreshDisposable.add(mDataBaseModel.getLocation(ISharePreference.getLocationId())
                .toObservable()
                .flatMap(new Function<DataLocation, Observable<ForecastResponse>>() {
                    @Override
                    public Observable<ForecastResponse> apply(DataLocation location) throws Exception {
                        Log.i(TAG, location.getFirstName());
                        return mNetModel.refresh(location);
                    }
                })
                .doFinally(() -> setLoadState(false))
                .subscribe(response -> mDataBaseModel.update(response), new errorHandler()));
    }

    public void refreshOld() {
        mRefreshDisposable.add(mDataBaseModel.getNotFresh()
                .doOnSubscribe(disposable -> setLoadState(true))
                .flatMap(response -> mNetModel.refresh(response.getDataLocation()))
                .doFinally(() -> setLoadState(false))
                .subscribe(response -> mDataBaseModel.update(response), new errorHandler()));
    }

    public MutableLiveData<Boolean> getLoadState() {
        return mLoadStateMutableLiveData;
    }

    public void setLoadState(Boolean state) {
        mLoadStateMutableLiveData.postValue(state);
    }

    private void registerNetBroadcast() {
        Log.i(TAG, "register broadcast");
        try {
            if (mNetReceiver == null) {
                mNetReceiver = new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        Log.i(TAG, "broadcast onReceive");
                        if (SystemUtils.isConnected(getApplication())) {
                            refreshOld();
                            getApplication().unregisterReceiver(mNetReceiver);
                        }
                    }
                };
                getApplication().registerReceiver(mNetReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
            }
        } catch (IllegalArgumentException e) {
            Log.i(TAG, "receiver is already registered", e);
        }
    }

    private void unregisterNetBroadcast() {
        try {
            if (mNetReceiver != null) {
                getApplication().unregisterReceiver(mNetReceiver);
                mNetReceiver = null;
            }
        } catch (IllegalArgumentException e) {
            Log.e(TAG, "receiver is not registered ", e);
        }
    }

    private class errorHandler implements Consumer<Throwable> {

        @Override
        public void accept(Throwable throwable) throws Exception {
            int resId = ErrorHandler.handelError(throwable);
            new Handler(Looper.getMainLooper()).post(() -> Toast.makeText(getApplication(), resId, Toast.LENGTH_SHORT).show());
            if (resId == R.string.no_network_connection) {
                registerNetBroadcast();
            }
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        switch (key) {
            case NOTIFICATION_KEY:
                return;
            default:
                getForecast();
        }
    }
}
