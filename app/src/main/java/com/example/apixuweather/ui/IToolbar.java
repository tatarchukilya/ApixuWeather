package com.example.apixuweather.ui;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.apixuweather.R;
import com.example.apixuweather.repo.IconManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.Toolbar;
import androidx.transition.TransitionManager;

import org.jetbrains.annotations.Contract;

import static com.example.apixuweather.utils.Const.I_TAG;

public class IToolbar {

    public static final String TAG = I_TAG + IToolbar.class.getSimpleName();

    private AppCompatActivity mActivity;
    private Toolbar mToolbar;

    private ImageView mShowSettings;
    private ImageView mMenuArrow;

    private ViewGroup mTitle;
    private TextView mFirst;
    private TextView mSecond;

    private boolean isRoot = true;

    private View.OnClickListener mLeftListener;
    private View.OnClickListener mOnBackPressed;

    public IToolbar(@NonNull AppCompatActivity activity, View.OnClickListener left, View.OnClickListener settings) {

        mActivity = activity;

        mToolbar = activity.findViewById(R.id.toolbar);
        activity.setSupportActionBar(mToolbar);

        mShowSettings = activity.findViewById(R.id.settings);
        mMenuArrow = activity.findViewById(R.id.burger_arrow);

        mTitle = activity.findViewById(R.id.title_toolbar);
        mFirst = activity.findViewById(R.id.first);
        mSecond = activity.findViewById(R.id.second);

        mLeftListener = left;
        mOnBackPressed = v -> mActivity.onBackPressed();

        mMenuArrow.setOnClickListener(left);
        mShowSettings.setOnClickListener(settings);
    }

    public void setState(final String first, final String second, boolean isroot) {
        setTitle(first, second);
        if (isRoot != isroot) {
            isRoot = isroot;
            mShowSettings.setVisibility(isRoot ? View.VISIBLE : View.INVISIBLE);
            mMenuArrow.setImageDrawable(AppCompatResources.getDrawable(mActivity, isRoot ? R.drawable.ic_menu_24dp : R.drawable.ic_arrow_back_24dp));
            mMenuArrow.setOnClickListener(isRoot ? mLeftListener : mOnBackPressed);
            IconManager.animate(mMenuArrow);
        }
    }

    public void setName(String first, String second) {
        setTitle(first, second);
    }

    public void setAlpha(float alpha) {
        if (mToolbar.getAlpha() != alpha)
            mToolbar.setAlpha(alpha);
    }

    private void setTitle(String first, String second) {
       /* Transition changeBounds = new ChangeBounds();
        changeBounds.setDuration(300);
        changeBounds.setInterpolator(new AccelerateDecelerateInterpolator());*/
        TransitionManager.beginDelayedTransition(mTitle);
        mFirst.setText(first);
        mSecond.setText(second);
        mSecond.setVisibility(isStringEmpty(second) ? View.GONE : View.VISIBLE);

    }

    @Contract("null -> true")
    private boolean isStringEmpty(String s) {
        return s == null || s.length() == 0;
    }
}
