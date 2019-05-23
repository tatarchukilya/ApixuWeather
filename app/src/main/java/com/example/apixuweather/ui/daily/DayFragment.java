package com.example.apixuweather.ui.daily;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.example.apixuweather.R;
import com.example.apixuweather.ui.base.BaseAdapter;
import com.example.apixuweather.utils.IDateFormatter;

import org.threeten.bp.Instant;
import org.threeten.bp.OffsetDateTime;
import org.threeten.bp.ZoneOffset;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.example.apixuweather.utils.Const.I_TAG;


public class DayFragment extends Fragment {

    public static final String TAG = I_TAG + DayFragment.class.getSimpleName();

    private static final String ARG_DATE = "date_1";

    private long mDate;

    private DayViewModel mViewModel;

    private RecyclerView mRecyclerView;
    private BaseAdapter mAdapter;

    private TextView mDayOfWeek;
    private TextView mDateView;

    private View mRootView;

    public static DayFragment newInstance(Long date){
        Bundle args = new Bundle();
        args.putLong(ARG_DATE, date);
        DayFragment fragment = new DayFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDate = getArguments() != null ? getArguments().getLong(ARG_DATE) : 0;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_day, container, false);
        mRootView.setVisibility(View.INVISIBLE);
        mRecyclerView = mRootView.findViewById(R.id.recycler_day);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mAdapter = new BaseAdapter();
        mRecyclerView.setAdapter(mAdapter);

        mDayOfWeek = mRootView.findViewById(R.id.day_of_week);
        mDateView = mRootView.findViewById(R.id.date);

        return mRootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(DayViewModel.class);
        mViewModel.getDbData(mDate);
        mViewModel.getData().observe(this, baseItems -> {
            OffsetDateTime dateTime = Instant.ofEpochSecond(mDate).atOffset(ZoneOffset.UTC);
            mDayOfWeek.setText(dateTime.format(IDateFormatter.dayOfWeek()));
            mDateView.setText(dateTime.format(IDateFormatter.dayAndMonth()));
            mAdapter.setList(baseItems);
            mRootView.setVisibility(View.VISIBLE);
        });
       /* ViewGroup.LayoutParams params = new LinearLayout.LayoutParams(SystemUtils.getDisplayMetric().x, )
        mRootView.*/
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
