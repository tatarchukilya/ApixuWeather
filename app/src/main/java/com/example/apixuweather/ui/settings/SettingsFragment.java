package com.example.apixuweather.ui.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.example.apixuweather.R;
import com.example.apixuweather.repo.ISharePreference;
import com.example.apixuweather.ui.base.BaseAdapter;
import com.example.apixuweather.ui.base.BaseFragment;
import com.example.apixuweather.ui.base.BaseItem;
import com.example.apixuweather.ui.base.BaseViewHolder;
import com.example.apixuweather.ui.base.FragmentListener;
import com.example.apixuweather.ui.settings.recycler.NotificationItem;
import com.example.apixuweather.ui.settings.recycler.NotificationViewHolder;
import com.example.apixuweather.ui.settings.recycler.PressureSettingsItem;
import com.example.apixuweather.ui.settings.recycler.TempSettingsItem;
import com.example.apixuweather.ui.settings.recycler.WindSettingsItem;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.RecyclerView;

import static com.example.apixuweather.repo.ISharePreference.NOTIFICATION_KEY;
import static com.example.apixuweather.utils.Const.I_TAG;

public class SettingsFragment extends BaseFragment{

    private static final String TAG = I_TAG + SettingsFragment.class.getSimpleName();

    public static SettingsFragment newInstance() {
        Bundle args = new Bundle();
        SettingsFragment fragment = new SettingsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private FragmentListener mFragmentListener;

    private RecyclerView mRecyclerView;
    private SettingsAdapter mAdapter;
    private List<BaseItem> mItems;

    @Override
    public void init(View view) {
        super.init(view);
        mItems = new ArrayList<>();
        mItems.add(new NotificationItem());
        mItems.add(new TempSettingsItem());
        mItems.add(new WindSettingsItem());
        mItems.add(new PressureSettingsItem());

        mAdapter = new SettingsAdapter();
        mAdapter.setList(mItems);
        mRecyclerView = view.findViewById(R.id.recycler_vertical);
        mRecyclerView.setAdapter(mAdapter);

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mFragmentListener = (FragmentListener) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mFragmentListener = null;
    }


    @Override
    public void setActivityState() {
        mFragmentListener.setState(new Pair<>(getResources().getString(R.string.settings), null), false);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.recycler_view_vertical;
    }

    private class SettingsAdapter extends BaseAdapter{

        @Override
        public void onViewRecycled(@NonNull BaseViewHolder holder) {
            super.onViewRecycled(holder);
            if (holder instanceof NotificationViewHolder){
                ((NotificationViewHolder) holder).unbind();
            }
        }
    }
}
