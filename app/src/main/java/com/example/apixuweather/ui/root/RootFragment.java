package com.example.apixuweather.ui.root;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.apixuweather.R;
import com.example.apixuweather.repo.ISharePreference;
import com.example.apixuweather.ui.base.BaseAdapter;
import com.example.apixuweather.ui.base.BaseFragment;
import com.example.apixuweather.ui.base.BaseItem;
import com.example.apixuweather.ui.base.FragmentListener;
import com.example.apixuweather.ui.base.ViewHolderAction;
import com.example.apixuweather.ui.base.ViewHolderTouchListener;
import com.example.apixuweather.ui.daily.DailyFragment;
import com.example.apixuweather.ui.root.recycler.DailyItem;
import com.example.apixuweather.ui.root.recycler.DailyViewHolder;
import com.example.apixuweather.ui.root.recycler.HourItem;
import com.example.apixuweather.ui.root.recycler.HourViewHolder;
import com.example.apixuweather.ui.root.recycler.HoursItem;
import com.example.apixuweather.utils.BlurBuilder;
import com.example.apixuweather.utils.Screenshot;
import com.example.apixuweather.utils.SystemUtils;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.util.Pair;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.transition.TransitionManager;

import static com.example.apixuweather.utils.Const.I_TAG;

public class RootFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, ViewHolderTouchListener {

    public static final String TAG = I_TAG + RootFragment.class.getSimpleName();

    private static final int REQUEST_DIALOG = 4;
    private static final String DIALOG_TAG = "RootDialogTag";

    public static final String ARG_BLUR = "arg_blur";
    private boolean isBlur = false;

    private RootViewModel mViewModel;

    private RecyclerView mRecyclerView;

    private BaseAdapter mAdapter;

    private View mRootView;

    private ConstraintLayout mConstraintLayout;

    private View mBlur;

    private View mFloatView;

    public static RootFragment newInstance() {

        Bundle args = new Bundle();

        RootFragment fragment = new RootFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private Pair<String, String> mAddress = new Pair<>("", "");
    private boolean isLoad = false;

    private FragmentListener mFragmentListener;

    @Override
    public void setActivityState() {
        mFragmentListener.setState(mAddress, true, this, isLoad, true);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_root;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            isBlur = savedInstanceState.getBoolean(ARG_BLUR);
        }
    }

    @Override
    public void init(View view) {
        super.init(view);
        mRootView = view;
        mRecyclerView = view.findViewById(R.id.root_recycler);
        mAdapter = new BaseAdapter(this);
        mRecyclerView.setAdapter(mAdapter);

        mBlur = View.inflate(getActivity(), R.layout.blur, null);

        mConstraintLayout = view.findViewById(R.id.constraint_view);
        mConstraintLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        mFloatView = View.inflate(getActivity(), R.layout.view_hour_detail, null);
        // Параметры FloatView
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                (int) ((isLandscape() ? 240 : 145) * getResources().getDisplayMetrics().density), ViewGroup.LayoutParams.WRAP_CONTENT);
        mFloatView.setLayoutParams(params);

        mRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (isBlur) {
                    addBlur();
                }
                mRootView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mFragmentListener = (FragmentListener) context;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(RootViewModel.class);
    }

    @Override
    public void onResume() {
        super.onResume();
        mViewModel.refreshOld();
        mViewModel.getItems().observe(this, this::setForecast);
        mViewModel.getAddress().observe(this, this::setAddress);
        mViewModel.getLoadState().observe(this, this::setLoadState);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mFragmentListener = null;
    }

    @Override
    public void onRefresh() {
        mViewModel.refresh();
    }

    private void setAddress(@NotNull Pair<String, String> address) {
        mAddress = address;
        mFragmentListener.setName(address.first, address.second);
    }

    private void setForecast(@NotNull List<BaseItem> items) {
        if (items.size() > 1 && !ISharePreference.isRootDialogShow()) {
            showDialog();
        }
        mAdapter.setList(items);
    }

    private void setLoadState(boolean isLoad) {
        this.isLoad = isLoad;
        mFragmentListener.setLoadState(isLoad);
    }

    @Override
    public void onHolderClick(View view, ViewHolderAction action, int position) {
        if (view.getTag() == null) return;

        // Если прогноз на час, то выводим float view
        if (view.getTag() == HourViewHolder.TAG) {
            HourItem item = (HourItem) ((HoursItem) mAdapter.getList().get(1)).getList().get(position);
            if (action == ViewHolderAction.longTouch && mConstraintLayout.getViewById(R.id.float_view) == null) {
                TransitionManager.beginDelayedTransition(mConstraintLayout);
                addBlur();
                bindFloatView(item);
                addFloatView(view);
            }

            if (action == ViewHolderAction.cancel) {
                mConstraintLayout.removeView(mBlur);
                mConstraintLayout.removeView(mFloatView);
            }
        }


        if (view.getTag() == DailyViewHolder.TAG && action == ViewHolderAction.select) {
            mFragmentListener.startFragment(DailyFragment.newInstance(((DailyItem) mAdapter.getList().get(position)).getDateEpoch()), true);
        }
    }

    @Override
    public void onHolderClick(View view, ViewHolderAction action, String id) {

    }

    private void bindFloatView(@NonNull HourItem item) {
        // Заполняем поля view
        ((ImageView) mFloatView.findViewById(R.id.float_icon)).setImageDrawable(item.getIcon());
        ((TextView) mFloatView.findViewById(R.id.float_description)).setText(item.getDescription());
        ((TextView) mFloatView.findViewById(R.id.temp_float)).setText(item.getTemp());

        if (item.getWindSpeed().equals("0")) {
            ((TextView) mFloatView.findViewById(R.id.wind_speed)).setText(getResources().getString(R.string.calm));
        } else {
            ((TextView) mFloatView.findViewById(R.id.wind_speed)).setText(item.getWindSpeed());
            ((TextView) mFloatView.findViewById(R.id.wind_speed_units)).setText(getResources().getString(item.getWindUnit()));
            ((TextView) mFloatView.findViewById(R.id.wind_direction)).setText(getResources().getString(item.getWindDir()));
        }

        ((TextView) mFloatView.findViewById(R.id.pressure)).setText(item.getPressure());
        ((TextView) mFloatView.findViewById(R.id.pressure_units)).setText(getResources().getString(item.getPressureUnit()));

        ((TextView) mFloatView.findViewById(R.id.humidity)).setText(item.getHumidity());
    }

    private void addFloatView(@NonNull View view) {
        mConstraintLayout.addView(mFloatView);
        ConstraintSet set = new ConstraintSet();
        set.clone(mConstraintLayout);
        set.connect(mFloatView.getId(), ConstraintSet.START, mConstraintLayout.getId(), ConstraintSet.START, 32);
        set.connect(mFloatView.getId(), ConstraintSet.END, mConstraintLayout.getId(), ConstraintSet.END, 32);
        set.connect(mFloatView.getId(), ConstraintSet.TOP, mConstraintLayout.getId(), ConstraintSet.TOP, 16);
        set.connect(mFloatView.getId(), ConstraintSet.BOTTOM, mConstraintLayout.getId(), ConstraintSet.BOTTOM, 16);
        int[] coord = new int[2];
        view.getLocationOnScreen(coord);
    /*    Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);*/
        Point size = SystemUtils.getDisplayMetric();
        float bais = (float) size.x / 2 < coord[0] ? 0.01f : 0.99f;
        set.setHorizontalBias(mFloatView.getId(), bais);
        set.applyTo(mConstraintLayout);
    }

    public void addBlur() {
        if (mConstraintLayout.findViewById(R.id.blur) == null) {
            ((ImageView) mBlur.findViewById(R.id.blur)).setImageBitmap(BlurBuilder.blur(getActivity(), Screenshot.takescreenshot(mConstraintLayout)));
            mConstraintLayout.addView(mBlur);
            ConstraintSet set = new ConstraintSet();
            set.clone(mConstraintLayout);
            set.constrainHeight(mBlur.getId(), mConstraintLayout.getHeight());
            set.constrainWidth(mBlur.getId(), mConstraintLayout.getWidth());
            set.connect(mBlur.getId(), ConstraintSet.TOP, mConstraintLayout.getId(), ConstraintSet.TOP);
            set.applyTo(mConstraintLayout);
        }
    }

    public void removeBlur() {
        if (mConstraintLayout.findViewById(R.id.blur) != null) {
            mConstraintLayout.removeView(mBlur);
        }
    }

    private boolean isLandscape() {
        return getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    private void showDialog() {
        RootDialog dialog = new RootDialog();
        dialog.setTargetFragment(this, REQUEST_DIALOG);
        dialog.show(requireActivity().getSupportFragmentManager(), DIALOG_TAG);
        ISharePreference.setRootDialogShowed();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(ARG_BLUR, mConstraintLayout.findViewById(R.id.blur) != null);
    }
}
