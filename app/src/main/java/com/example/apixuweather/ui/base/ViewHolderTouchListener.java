package com.example.apixuweather.ui.base;

import android.view.View;

public interface ViewHolderTouchListener {

    void onHolderClick(View view, ViewHolderAction action, int position);
    void onHolderClick(View view, ViewHolderAction action, String id);
}
