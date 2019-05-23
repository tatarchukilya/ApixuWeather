package com.example.apixuweather.ui.base;

import androidx.core.util.Pair;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public interface FragmentListener {

    void setState(Pair<String, String> title, boolean isRoot);
    void setState(Pair<String, String> title, boolean drawerEnabled, SwipeRefreshLayout.OnRefreshListener listener, boolean isRefresh, boolean isRoot);
    void openDrawer();
    void closeDrawer();
    void setSwipeRefreshListener(SwipeRefreshLayout.OnRefreshListener listener);
    void setDrawerEnabled(boolean enabled);
    void startFragment(BaseFragment fragment, boolean isAdd);
    void setLoadState(boolean isOn);
    void setName(String first, String second);
    boolean isDrawerOpen();
}
