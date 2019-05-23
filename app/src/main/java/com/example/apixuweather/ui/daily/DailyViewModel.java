package com.example.apixuweather.ui.daily;

import android.os.Handler;

import com.example.apixuweather.IApplication;
import com.example.apixuweather.repo.DataBaseModel;

import java.util.List;

import javax.inject.Inject;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import io.reactivex.disposables.Disposable;

import static com.example.apixuweather.utils.Const.I_TAG;

public class DailyViewModel extends ViewModel {

    public static final String TAG = I_TAG + DailyViewModel.class.getSimpleName();

    @Inject
    DataBaseModel mDataBase;

    private MutableLiveData<List<Long>> mDateList = new MutableLiveData<>();

    private Disposable mDbDisposable;

    public DailyViewModel() {
        IApplication.getAppComponent().inject(this);
        getDates();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }

    private void getDates(){
        dispose();
        new Handler().postDelayed(() -> mDbDisposable = mDataBase.getDate().subscribe(this::setDateList), 600);
        //mDbDisposable = mDataBase.getDate().subscribe(this::setDateList);
    }

    private void setDateList(List<Long> dateList) {
        mDateList.postValue(dateList);
    }

    public MutableLiveData<List<Long>> getDateList() {
        return mDateList;
    }

    private void dispose(){
        if (mDbDisposable != null) mDbDisposable.dispose();
    }
}
