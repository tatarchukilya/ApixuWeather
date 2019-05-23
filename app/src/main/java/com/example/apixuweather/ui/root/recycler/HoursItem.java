package com.example.apixuweather.ui.root.recycler;

import com.example.apixuweather.rest.response.apixu.ForecastResponse;
import com.example.apixuweather.rest.response.apixu.Hour;
import com.example.apixuweather.ui.base.BaseItem;

import java.util.ArrayList;
import java.util.List;

import static com.example.apixuweather.utils.Const.I_TAG;

public class HoursItem implements BaseItem {

    public static final String TAG = I_TAG + HoursItem.class.getSimpleName();

    private List<BaseItem> mList;

    @Override
    public int getLayoutType() {
        return HoursViewHolder.LAYOUT;
    }

    public HoursItem(ForecastResponse response, List<Hour> hours) {
        mList = new ArrayList<>();
        if (response != null) {
            mList.add(new DetailNowItem(response.getCurrent()));
            for (Hour hour : hours){
                mList.add(new HourItem(hour, response.getLocation().getTimeZone()));
            }
        }
    }

    public List<BaseItem> getList() {
        return mList;
    }
}
