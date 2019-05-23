package com.example.apixuweather.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import com.example.apixuweather.R;
import com.example.apixuweather.repo.ISharePreference;
import com.example.apixuweather.service.LocationService;
import com.example.apixuweather.ui.base.BaseFragment;
import com.example.apixuweather.ui.base.FragmentListener;
import com.example.apixuweather.ui.base.LocationActivity;
import com.example.apixuweather.ui.daily.DailyFragment;
import com.example.apixuweather.ui.navigation.NavigationFragment;
import com.example.apixuweather.ui.root.RootFragment;
import com.example.apixuweather.ui.settings.SettingsFragment;
import com.example.apixuweather.utils.SystemUtils;

import org.jetbrains.annotations.Nullable;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import static com.example.apixuweather.repo.ISharePreference.NOTIFICATION_KEY;
import static com.example.apixuweather.utils.Const.I_TAG;

public class MainActivity extends LocationActivity implements FragmentListener, FragmentManager.OnBackStackChangedListener,
        SharedPreferences.OnSharedPreferenceChangeListener {

    public static final String TAG = I_TAG + MainActivity.class.getSimpleName();

    private IToolbar mToolbar;

    private RootFragment mRootFragment;

    private DrawerLayout mDrawerLayout;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ISharePreference.getPreferences().registerOnSharedPreferenceChangeListener(this);
        initToolbar();

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        findViewById(R.id.navigation_view).getLayoutParams().width = displaymetrics.widthPixels;

        mDrawerLayout = findViewById(R.id.drawer_layout);
        mDrawerLayout.setDrawerElevation(0);
        mSwipeRefreshLayout = findViewById(R.id.swipe_refresh);

        mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
                if (slideOffset == 0) {
                    if (getSupportFragmentManager().getBackStackEntryCount() == 0)
                        mRootFragment.removeBlur();
                } else {
                    mRootFragment.addBlur();
                }
                mToolbar.setAlpha(1 - slideOffset);
            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
                mRootFragment.removeBlur();
                NavigationFragment fragment = (NavigationFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_fragment_container);
                if (fragment != null) fragment.hideSearch();
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

        FragmentManager manager = getSupportFragmentManager();
        manager.addOnBackStackChangedListener(this);

        if (manager.findFragmentById(R.id.navigation_fragment_container) == null) {
            manager.beginTransaction().add(R.id.navigation_fragment_container, NavigationFragment.newInstance()).commit();
        }

        if (manager.findFragmentById(R.id.fragment_container) == null) {
            mRootFragment = RootFragment.newInstance();
            startFragment(mRootFragment, false);
        } else {
            mRootFragment = getRootFragment();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");
        setStateFromFragment();
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) mToolbar.setAlpha(0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getSupportFragmentManager().removeOnBackStackChangedListener(this);
        ISharePreference.getPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onBackPressed() {
        Log.i(TAG, "onBackPressed");
        NavigationFragment fragment = (NavigationFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_fragment_container);
        if (fragment != null && fragment.hideSearch()) return;
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            closeDrawer();
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void startFragment(BaseFragment fragment, boolean isAdd) {
        if (fragment instanceof DailyFragment && getSupportFragmentManager().getBackStackEntryCount() > 0) {
            return;
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fragment_container, fragment);
        if (isAdd) {
            transaction.addToBackStack(null);
            mRootFragment.addBlur();
        }
        transaction.commit();
    }

    @Override
    public void setLoadState(boolean isOn) {
        if (mSwipeRefreshLayout.isRefreshing() != isOn) mSwipeRefreshLayout.setRefreshing(isOn);
    }

    @Override
    public void setName(String first, String second) {
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            mToolbar.setName(first, second);
        }
    }

    @Override
    public boolean isDrawerOpen() {
        return mDrawerLayout.isDrawerOpen(GravityCompat.START);
    }

    @Override
    public void onBackStackChanged() {
       /* if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            mRootFragment.removeBlur();
            mRootFragment.setActivityState();
        }*/
        setStateFromFragment();
    }

    @Override
    public void setState(Pair<String, String> title, boolean isRoot) {
        setDrawerEnabled(false);
        setLoadState(false);
        setSwipeRefreshListener(null);
        mToolbar.setState(title.first, title.second, isRoot);
    }

    @Override
    public void setState(Pair<String, String> title, boolean drawerEnabled, SwipeRefreshLayout.OnRefreshListener listener, boolean isRefresh, boolean isRoot) {
        setDrawerEnabled(drawerEnabled);
        setSwipeRefreshListener(listener);
        setLoadState(isRefresh);
        mToolbar.setState(title.first, title.second, isRoot);
    }

    @Override
    public void openDrawer() {
        mDrawerLayout.openDrawer(GravityCompat.START, true);
    }

    @Override
    public void closeDrawer() {
        mDrawerLayout.closeDrawer(GravityCompat.START, true);
    }

    @Override
    public void setSwipeRefreshListener(SwipeRefreshLayout.OnRefreshListener listener) {
        mSwipeRefreshLayout.setOnRefreshListener(listener);
        mSwipeRefreshLayout.setEnabled(listener != null);
        if (mSwipeRefreshLayout.isRefreshing()) mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void setDrawerEnabled(boolean enabled) {
        mDrawerLayout.setDrawerLockMode(enabled ? DrawerLayout.LOCK_MODE_UNDEFINED : DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    private void initToolbar() {
        mToolbar = new IToolbar(this,
                v -> mDrawerLayout.openDrawer(GravityCompat.START),
                v -> startFragment(SettingsFragment.newInstance(), true));
    }

    private void setStateFromFragment() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            if (!isDrawerOpen()){
                mRootFragment.removeBlur();
            }
            mRootFragment.setActivityState();
        } else {
            List<Fragment> fragments = getSupportFragmentManager().getFragments();
            if (!fragments.isEmpty()) {
                Fragment current = fragments.get(fragments.size() - 1);
                if (current instanceof BaseFragment) {
                    ((BaseFragment) current).setActivityState();
                }
            }
        }
    }

    @Nullable
    private RootFragment getRootFragment() {
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            if (fragment instanceof RootFragment) {
                return (RootFragment) fragment;
            }
        }
        return null;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(NOTIFICATION_KEY)) {
            if (ISharePreference.isNotificationOn()) {
                if (!isLocationPermissionGranted()) requestLocationPermission();
            }
        }
    }
}
