package com.example.apixuweather.ui.navigation;

import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;

import com.example.apixuweather.IApplication;
import com.example.apixuweather.repo.DataBaseModel;
import com.example.apixuweather.repo.ISharePreference;
import com.example.apixuweather.repo.NetModel;
import com.example.apixuweather.rest.error.ErrorHandler;
import com.example.apixuweather.rest.response.apixu.ForecastResponse;
import com.example.apixuweather.rest.response.apixu.Hour;
import com.example.apixuweather.ui.base.BaseItem;
import com.example.apixuweather.ui.navigation.recycler.LocationItem;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import io.reactivex.Flowable;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;

import static com.example.apixuweather.repo.ISharePreference.LOCATION_KEY;
import static com.example.apixuweather.repo.ISharePreference.TEMP_KEY;
import static com.example.apixuweather.utils.Const.CURRENT_LOCATION_ID;
import static com.example.apixuweather.utils.Const.I_TAG;

public class NavigationViewModel extends ViewModel implements SharedPreferences.OnSharedPreferenceChangeListener {

    public static final String TAG = I_TAG + NavigationViewModel.class.getSimpleName();

    @Inject
    NetModel mNetModel;

    @Inject
    DataBaseModel mDataBase;

    private MutableLiveData<List<BaseItem>> mCurrentWeather = new MutableLiveData<>();
    private MutableLiveData<List<BaseItem>> mSearchResult = new MutableLiveData<>();
    private List<BaseItem> mSearchList = new ArrayList<>();

    private MutableLiveData<Boolean> mLoadState = new MutableLiveData<>();

    private Disposable mSearchDisposable;
    private Disposable mCurrentDisposable;

    public NavigationViewModel() {
        super();
        IApplication.getAppComponent().inject(this);
        ISharePreference.getPreferences().registerOnSharedPreferenceChangeListener(this);
        getData();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        ISharePreference.getPreferences().unregisterOnSharedPreferenceChangeListener(this);
        disposeSearch();
        disposeCurrent();
    }

    public void search(String input) {
        disposeSearch();
        mSearchDisposable = mNetModel.search(input)
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        clearSearchResult();
                        setLoadState(true);
                    }
                })
                .doFinally(() -> setLoadState(false))
                .subscribe(this::addResult, ErrorHandler::handelError);
    }

    /**
     * Search
     */
    public void disposeSearch() {
        if (mSearchDisposable != null) mSearchDisposable.dispose();
    }

    public void clearSearchResult() {
        mSearchList.clear();
        mSearchResult.postValue(mSearchList);
    }

    private void addResult(ForecastResponse response) {
        mSearchList.add(response);
        mSearchResult.postValue(mSearchList);
    }

    public MutableLiveData<List<BaseItem>> getSearchResult() {
        return mSearchResult;
    }

    /**
     * Current
     */


    public MutableLiveData<List<BaseItem>> getCurrentWeather() {
        return mCurrentWeather;
    }

    public void setCurrentWeather(List<BaseItem> items) {
        mCurrentWeather.postValue(items);
    }

    private void disposeCurrent() {
        if (mCurrentDisposable != null) mCurrentDisposable.dispose();
    }

    /**
     * Progress bar state
     */

    public void setLoadState(Boolean isVisible) {
        mLoadState.postValue(isVisible);
    }

    public MutableLiveData<Boolean> getLoadState() {
        return mLoadState;
    }

    public void addLocation(int position) {
        ForecastResponse response = (ForecastResponse) mSearchList.get(position);
        mDataBase.add(response);
        ISharePreference.setLocationId(response.getLocationId());
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        switch (key) {
            case LOCATION_KEY:
                getData();
                break;
            case TEMP_KEY:
                getData();
                break;
        }
    }

    public boolean setLocationId(int position) {
        LocationItem item;
        if (mCurrentWeather.getValue() != null) {
            item = (LocationItem) mCurrentWeather.getValue().get(position);
            if (item.getLocationId().equals(ISharePreference.getLocationId())) {
                return false;
            }
            ISharePreference.setLocationId(item.getLocationId());
        }
        return true;
    }

    public void deleteLocation(String id) {
        mDataBase.delete(id);
        new Handler().postDelayed(() -> {
            if (id.equals(ISharePreference.getLocationId())) {
                ISharePreference.setLocationId(CURRENT_LOCATION_ID);
            }
        }, 100);

    }

    private void getData() {
        mCurrentDisposable = Flowable.zip(mDataBase.getCurrent(), mDataBase.getLastHours(), new BiFunction<List<ForecastResponse>, List<Hour>, List<BaseItem>>() {
            @Override
            public List<BaseItem> apply(List<ForecastResponse> forecastResponses, List<Hour> hours) throws Exception {
                List<BaseItem> result = new ArrayList<>();
                for (ForecastResponse response : forecastResponses) {
                    if (!response.isValidate() && !response.isNotCurrent()) {
                        continue;
                    }
                    for (Hour hour : hours) {
                        if (response.getLocationId().equals(hour.getLocationId())) {
                            if (response.getLocationId().equals(CURRENT_LOCATION_ID)) {
                                result.add(0, new LocationItem(response, hour));
                            } else {
                                result.add(new LocationItem(response, hour));
                            }
                        }
                    }
                }
                return result;
            }
        }).subscribe(this::setCurrentWeather);
    }

}
