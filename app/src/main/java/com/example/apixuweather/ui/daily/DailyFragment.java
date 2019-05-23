package com.example.apixuweather.ui.daily;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.example.apixuweather.R;
import com.example.apixuweather.ui.base.BaseFragment;
import com.example.apixuweather.ui.base.FragmentListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import static com.example.apixuweather.utils.Const.I_TAG;

public class DailyFragment extends BaseFragment {

    public static final String TAG = I_TAG + DailyFragment.class.getSimpleName();

    private static final String ARG_DATE = "date";

    public DailyViewModel mViewModel;

    public FragmentListener mFragmentListener;

    private FragmentStatePagerAdapter mAdapter;
    private Long mCurrentDate;

    private ViewPager mViewPager;
    private List<Long> mDateList;

    public static DailyFragment newInstance(long date) {
        Bundle args = new Bundle();
        args.putLong(ARG_DATE, date);
        DailyFragment fragment = new DailyFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mFragmentListener = (FragmentListener) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDateList = new ArrayList<>();
        if (savedInstanceState != null) {
            mCurrentDate = savedInstanceState.getLong(ARG_DATE);
        } else {
            mCurrentDate = getArguments() != null ? getArguments().getLong(ARG_DATE) : 0;
        }
    }

    @Override
    public void init(View view) {
        super.init(view);
        FragmentManager manager = getChildFragmentManager();
        mAdapter = new FragmentStatePagerAdapter(manager) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                return DayFragment.newInstance(mDateList.get(position));
            }

            @Override
            public int getCount() {
                return mDateList.size();
            }
        };
        mViewPager = view.findViewById(R.id.view_pager);
        mViewPager.setAdapter(mAdapter);
        //mViewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mCurrentDate = mDateList.get(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(DailyViewModel.class);
        mViewModel = ViewModelProviders.of(this).get(DailyViewModel.class);
        mViewModel.getDateList().observe(this, this::setDateList);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        try {
            outState.putLong(ARG_DATE, mDateList.get(mViewPager.getCurrentItem()));
        } catch (IndexOutOfBoundsException e){
            outState.putLong(ARG_DATE, mCurrentDate);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mFragmentListener = null;
    }

    @Override
    public void setActivityState() {
        mFragmentListener.setState(new Pair<>(getResources().getString(R.string.forecast_for_10_days), null), false);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_daily;
    }
    private void setDateList(List<Long> dateList) {
        mDateList = dateList;
        mAdapter.notifyDataSetChanged();
        for (int i = 0; i < mDateList.size(); i++) {
            if (mDateList.get(i).equals(mCurrentDate)) {
                mViewPager.setCurrentItem(i, false);
            }
        }
    }

}
