package com.example.apixuweather.ui.root.recycler;

import android.view.View;

import com.example.apixuweather.R;
import com.example.apixuweather.ui.base.BaseAdapter;
import com.example.apixuweather.ui.base.BaseViewHolder;
import com.example.apixuweather.ui.base.ViewHolderTouchListener;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

public class HoursViewHolder extends BaseViewHolder<HoursItem> {

    //public static final String TAG = I_TAG + HoursViewHolder.class.getSimpleName();

    @BindView(R.id.recycler_next_24)
    RecyclerView mRecyclerView;

    private ViewHolderTouchListener mTouchListener;

    @LayoutRes
    public static final int LAYOUT = R.layout.view_hours;

    public HoursViewHolder(@NonNull View itemView, ViewHolderTouchListener listener) {
        super(itemView, listener);
        mTouchListener = listener;
    }

    @Override
    public void bind(HoursItem model) {
        BaseAdapter adapter = new BaseAdapter(mTouchListener);
        adapter.setList(model.getList());
        mRecyclerView.setAdapter(adapter);
    }
}
