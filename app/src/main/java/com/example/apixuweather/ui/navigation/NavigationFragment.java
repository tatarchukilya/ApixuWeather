package com.example.apixuweather.ui.navigation;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.apixuweather.R;
import com.example.apixuweather.ui.base.BaseAdapter;
import com.example.apixuweather.ui.base.FragmentListener;
import com.example.apixuweather.ui.base.ViewHolderAction;
import com.example.apixuweather.ui.base.ViewHolderTouchListener;
import com.example.apixuweather.ui.navigation.recycler.CurrentLocationViewHolder;
import com.example.apixuweather.ui.navigation.recycler.ItemTouchHelperCallback;
import com.example.apixuweather.ui.navigation.recycler.LocationAdapter;
import com.example.apixuweather.ui.navigation.recycler.LocationItem;
import com.example.apixuweather.ui.navigation.recycler.LocationViewHolder;
import com.example.apixuweather.ui.navigation.recycler.SearchViewHolder;
import com.example.apixuweather.utils.SystemUtils;
import com.jakewharton.rxbinding2.support.v7.widget.RxSearchView;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.Disposable;

import static com.example.apixuweather.utils.Const.I_TAG;

public class NavigationFragment extends Fragment implements ViewHolderTouchListener {

    public static final String TAG = I_TAG + NavigationFragment.class.getSimpleName();

    private static final String ARG_IS_SEARCH = "is_search";

    private View mRootView;

    private NavigationViewModel mViewModel;
    private FragmentListener mFragmentListener;

    private Unbinder mUnBinder;
    private Disposable mSearchDisposable;

    private boolean isSearch;

    private BaseAdapter mAdapterSearch;
    private LocationAdapter mAdapterLocation;

    ItemTouchHelperCallback mTouchHelperCallback;
    ItemTouchHelper mItemTouchHelper;

    @BindView(R.id.navigation_recycler)
    RecyclerView mRecyclerView;
    @BindView(R.id.search)
    SearchView mSearchView;
    @BindView(R.id.navigation_progress)
    ProgressBar mProgressBar;
    @BindView(R.id.close_drawer)
    ImageView mCloseDrawer;

    @NotNull
    @Contract(" -> new")
    public static NavigationFragment newInstance() {
        return new NavigationFragment();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mFragmentListener = (FragmentListener) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            isSearch = savedInstanceState.getBoolean(ARG_IS_SEARCH);
        } else {
            isSearch = false;
        }
        mAdapterSearch = new BaseAdapter(this);
        mAdapterLocation = new LocationAdapter(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_navigation, container, false);
        mUnBinder = ButterKnife.bind(this, mRootView);

        mRecyclerView.setAdapter(isSearch ? mAdapterSearch : mAdapterLocation);

        mTouchHelperCallback = new ItemTouchHelperCallback(mAdapterLocation);
        mItemTouchHelper = new ItemTouchHelper(mTouchHelperCallback);
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);

        mCloseDrawer.setOnClickListener(v -> ((AppCompatActivity) mFragmentListener).onBackPressed());

        mSearchView.setOnCloseListener(() -> setSearchVisible(false));
        mSearchView.setOnSearchClickListener(v -> setSearchVisible(true));
     /*   View v = mSearchView.findViewById(androidx.appcompat.R.id.search_plate);
        v.setBackgroundColor(getResources().getColor(R.color.transparent));*/

        mSearchDisposable = RxSearchView.queryTextChanges(mSearchView)
                .skipInitialValue()
                .doOnNext(charSequence -> mViewModel.disposeSearch())
                .doOnNext(charSequence -> mAdapterSearch.clear())
                .debounce(300, TimeUnit.MILLISECONDS)
                .filter(charSequence -> charSequence.length() > 1)
                .map(CharSequence::toString)
                .subscribe(s -> mViewModel.search(s));
        return mRootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(NavigationViewModel.class);
    }

    @Override
    public void onResume() {
        super.onResume();
        mViewModel.getLoadState().observe(this, aBoolean -> mProgressBar.setVisibility(aBoolean ? View.VISIBLE : View.GONE));
        mViewModel.getSearchResult().observe(this, forecastResponses -> mAdapterSearch.setList(forecastResponses));
        mViewModel.getCurrentWeather().observe(this, baseItems -> mAdapterLocation.setList(baseItems));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mUnBinder.unbind();
        if (mSearchDisposable != null) mSearchDisposable.dispose();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mFragmentListener = null;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(ARG_IS_SEARCH, isSearch);
    }

    @Override
    public void onHolderClick(View view, ViewHolderAction action, int position) {
        String tag = (String) view.getTag();
        if (tag.equals(SearchViewHolder.TAG)) {
            mViewModel.addLocation(position);
            SystemUtils.hideKeyboardFrom(requireContext(), mRootView);
            new Handler().postDelayed(() -> mFragmentListener.closeDrawer(), 300);
            return;
        }

        if (tag.equals(CurrentLocationViewHolder.TAG)) {
            if (mViewModel.setLocationId(position)) {
                new Handler().postDelayed(() -> mFragmentListener.closeDrawer(), 300);
            }
        }

        if (tag.equals(LocationViewHolder.TAG)) {
            if (action == ViewHolderAction.select) {
                if (mViewModel.setLocationId(position)) {
                    new Handler().postDelayed(() -> mFragmentListener.closeDrawer(), 300);
                }
            }
        }
    }

    @Override
    public void onHolderClick(View view, ViewHolderAction action, String id) {
        new Handler().postDelayed(() -> mViewModel.deleteLocation(id), 200);
    }

    @Contract("_ -> param1")
    private boolean setSearchVisible(boolean visible) {
        if (visible) {
            mRecyclerView.setAdapter(mAdapterSearch);
        } else {
            mRecyclerView.setAdapter(mAdapterLocation);
            mViewModel.clearSearchResult();
        }
        isSearch = visible;
        return visible;
    }

    public boolean hideSearch() {
        if (!mSearchView.isIconified()) {
            mSearchView.setQuery("", false);
            mSearchView.setIconified(true);
            return true;
        }
        return false;
    }
}
