package com.example.apixuweather.ui.navigation.recycler;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.apixuweather.R;
import com.example.apixuweather.repo.IconManager;
import com.example.apixuweather.ui.base.BaseViewHolder;
import com.example.apixuweather.ui.base.ViewHolderAction;
import com.example.apixuweather.ui.base.ViewHolderTouchListener;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import butterknife.BindView;

import static com.example.apixuweather.utils.Const.I_TAG;

public class CurrentLocationViewHolder extends BaseViewHolder<LocationItem> implements View.OnClickListener {

    public static final String TAG = I_TAG + CurrentLocationViewHolder.class.getSimpleName();

    @LayoutRes
    public static final int LAYOUT = R.layout.view_location_current;

    @BindView(R.id.first_name)
    TextView mFirstName;
    @BindView(R.id.second_name)
    TextView mSecondName;
    @BindView(R.id.temperature)
    TextView mTemp;
    @BindView(R.id.icon)
    ImageView mIcon;

    public CurrentLocationViewHolder(@NonNull View itemView, ViewHolderTouchListener listener) {
        super(itemView, listener);
        itemView.setTag(TAG);
        itemView.setOnClickListener(this);
    }

    @Override
    public void bind(LocationItem model) {
        int color = itemView.getResources().getColor(model.getColor());
        mFirstName.setText(model.getFirstName());
        mFirstName.setTextColor(color);
        mSecondName.setText(model.getSecondName());
        mTemp.setText(model.getTemp());
        mTemp.setTextColor(color);
        mIcon.setImageDrawable(model.getIcon());
        IconManager.animate(mIcon);
    }

    @Override
    public void onClick(View v) {
        mListener.onHolderClick(v, ViewHolderAction.select, getAdapterPosition());
    }
}
