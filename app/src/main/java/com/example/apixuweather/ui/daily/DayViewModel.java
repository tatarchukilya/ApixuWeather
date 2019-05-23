package com.example.apixuweather.ui.daily;

import android.os.Handler;

import com.example.apixuweather.IApplication;
import com.example.apixuweather.repo.DataBaseModel;
import com.example.apixuweather.ui.base.BaseItem;

import java.util.List;

import javax.inject.Inject;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import io.reactivex.disposables.Disposable;

import static com.example.apixuweather.utils.Const.I_TAG;

public class DayViewModel extends ViewModel {

    public static final String TAG = I_TAG + DayViewModel.class.getSimpleName();

    @Inject
    DataBaseModel mDataBase;

    private Disposable mDbDisposable;

    private MutableLiveData<List<BaseItem>> mData = new MutableLiveData<>();

    public DayViewModel() {
        IApplication.getAppComponent().inject(this);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        dispose();
    }

    void getDbData(long date){
        dispose();
        mDbDisposable = mDataBase.getDayData(date).subscribe(this::setData);
    }

    private void dispose(){
        if (mDbDisposable != null) mDbDisposable.dispose();
    }

    private void setData(List<BaseItem> list) {
        mData.postValue(list);
    }

    public MutableLiveData<List<BaseItem>> getData() {
        return mData;
    }
}
