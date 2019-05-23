package com.example.apixuweather.ui.base;

import android.os.Bundle;

import com.example.apixuweather.R;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import static com.example.apixuweather.utils.Const.I_TAG;

public abstract class BaseActivity extends AppCompatActivity {

    public static final String TAG = I_TAG + BaseActivity.class.getSimpleName();

    @LayoutRes
    protected abstract int getLayoutResId();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());
    }
}
