package com.example.apixuweather.ui.navigation.recycler;

import android.util.Log;
import android.view.View;

import com.example.apixuweather.ui.base.BaseAdapter;
import com.example.apixuweather.ui.base.ViewHolderAction;
import com.example.apixuweather.ui.base.ViewHolderTouchListener;

import static com.example.apixuweather.utils.Const.I_TAG;


public class LocationAdapter extends BaseAdapter implements ItemTouchHelperAdapter {

    public static final String TAG = I_TAG + LocationAdapter.class.getSimpleName();

    public LocationAdapter(ViewHolderTouchListener listener) {
        super(listener);
    }

    @Override
    public void onItemDismiss(View view, int position) {
        Log.i(TAG, "position: " + position);
        String id = ((LocationItem)getList().get(position)).getLocationId();
        remove(position);
        mListener.onHolderClick(view, ViewHolderAction.delete, id);
    }
}
