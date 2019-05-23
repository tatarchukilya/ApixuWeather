package com.example.apixuweather.ui.root.recycler;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.apixuweather.R;
import com.example.apixuweather.repo.IconManager;
import com.example.apixuweather.ui.base.BaseViewHolder;
import com.example.apixuweather.ui.base.ViewHolderTouchListener;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import butterknife.BindView;

public class WeatherNowViewHolder extends BaseViewHolder<WeatherNowItem> {

    @LayoutRes
    public static final int LAYOUT = R.layout.view_weather_now;

    @BindView(R.id.now_temp)
    TextView mTemp;
    @BindView(R.id.feels_like_temp)
    TextView mFeelsLike;
    @BindView(R.id.feels_like_title)
    TextView mFLTitle;
    @BindView(R.id.now_description)
    TextView mDescription;
    @BindView(R.id.now_icon)
    ImageView mIcon;

    public WeatherNowViewHolder(@NonNull View itemView, ViewHolderTouchListener listener) {
        super(itemView, listener);
    }

    @Override
    public void bind(WeatherNowItem model) {
        mTemp.setText(model.getTemp());
        mFeelsLike.setText(model.getFeelsLike());
        mDescription.setText(model.getDescription());
        mIcon.setImageDrawable(model.getIcon());
        IconManager.animate(mIcon);
        mFLTitle.setVisibility(model.getFeelsLike().length() > 0 ? View.VISIBLE : View.INVISIBLE);
    }
}
