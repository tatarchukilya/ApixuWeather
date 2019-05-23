package com.example.apixuweather.ui.root.recycler;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.apixuweather.R;
import com.example.apixuweather.ui.base.BaseViewHolder;
import com.example.apixuweather.ui.base.ViewHolderAction;
import com.example.apixuweather.ui.base.ViewHolderTouchListener;
import com.example.apixuweather.ui.root.RootFragment;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import butterknife.BindView;

import static com.example.apixuweather.utils.Const.I_TAG;

public class DailyViewHolder extends BaseViewHolder<DailyItem> implements View.OnClickListener {

    public static final String TAG = I_TAG + DailyViewHolder.class.getSimpleName();

    @LayoutRes
    public static final int LAYOUT = R.layout.view_daily;

    private ViewHolderTouchListener mTouchListener;

    @BindView(R.id.daily_temp_max)
    TextView mTempMax;
    @BindView(R.id.daily_temp_min)
    TextView mTempMin;
    @BindView(R.id.daily_day_of_week)
    TextView mDayOfWeek;
    @BindView(R.id.daily_date)
    TextView mDate;
    @BindView(R.id.daily_icon)
    ImageView mIcon;
    @BindView(R.id.divider)
    View mDivider;

    public DailyViewHolder(@NonNull View itemView, ViewHolderTouchListener listener) {
        super(itemView, listener);
        itemView.setOnClickListener(this);
        mTouchListener = listener;
    }

    @Override
    public void bind(DailyItem model) {
        mTempMax.setText(model.getTempMax());
        mTempMin.setText(model.getTempMin());
        mDayOfWeek.setText(model.getDayOfWeek());
        mDate.setText(model.getDate());
        mIcon.setImageDrawable(model.getIcon());
        mDivider.setVisibility(getAdapterPosition() == 2 ? View.INVISIBLE : View.VISIBLE) ;
    }

    @Override
    public void onClick(View v) {
        v.setTag(TAG);
        mTouchListener.onHolderClick(v, ViewHolderAction.select, getAdapterPosition());
    }
}
