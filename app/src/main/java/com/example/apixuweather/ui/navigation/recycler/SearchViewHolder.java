package com.example.apixuweather.ui.navigation.recycler;

import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.apixuweather.R;
import com.example.apixuweather.repo.IconManager;
import com.example.apixuweather.rest.response.apixu.ForecastResponse;
import com.example.apixuweather.ui.base.BaseViewHolder;
import com.example.apixuweather.ui.base.ViewHolderAction;
import com.example.apixuweather.ui.base.ViewHolderTouchListener;

import androidx.annotation.NonNull;
import butterknife.BindView;

import static com.example.apixuweather.utils.Const.I_TAG;

public class SearchViewHolder extends BaseViewHolder<ForecastResponse> implements View.OnClickListener {

    public static final String TAG = I_TAG + SearchViewHolder.class.getSimpleName();

    public static final int LAYOUT = R.layout.view_search;

    @BindView(R.id.first_name)
    TextView mFirstName;
    @BindView(R.id.second_name)
    TextView mSecondName;
    @BindView(R.id.temperature)
    TextView mTemperature;
    @BindView(R.id.icon)
    ImageView mIcon;

    public SearchViewHolder(@NonNull View itemView, ViewHolderTouchListener listener) {
        super(itemView, listener);
        itemView.setOnClickListener(this);
    }

    @Override
    public void bind(ForecastResponse model) {
        mFirstName.setText(model.getLocation().getName());
        mSecondName.setText(model.getLocation().getRegion());
        mTemperature.setText(model.getCurrent().getTemp());
        mIcon.setImageDrawable(IconManager.getDrawable(model.getCurrent().getIconName(true)));
        IconManager.animate(mIcon);
    }

    @Override
    public void onClick(View v) {
        v.setTag(TAG);
        setLarge();
        mListener.onHolderClick(v, ViewHolderAction.select, getAdapterPosition());
    }

    private void setLarge(){
        mFirstName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        mTemperature.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
    }
}
