package com.example.apixuweather.ui.navigation.recycler;

import android.view.MotionEvent;
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
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;
import butterknife.BindView;

import static com.example.apixuweather.utils.Const.I_TAG;

public class LocationViewHolder extends BaseViewHolder<LocationItem> implements View.OnClickListener {

    public static final String TAG = I_TAG + LocationViewHolder.class.getSimpleName();

    @LayoutRes
    public static final int LAYOUT = R.layout.view_location;

    @BindView(R.id.first_name)
    TextView mName;
    @BindView(R.id.temperature)
    TextView mTemp;
    @BindView(R.id.icon)
    ImageView mIcon;

    private boolean isAcross;
    private AnimatedVectorDrawableCompat mAvdc;

    public LocationViewHolder(@NonNull View itemView, ViewHolderTouchListener listener) {
        super(itemView, listener);
        itemView.setTag(TAG);
        itemView.setOnClickListener(this);
        mAvdc = AnimatedVectorDrawableCompat.create(itemView.getContext(), R.drawable.ic_delete_24dp);

    }

    @Override
    public void bind(LocationItem model) {
        int color = itemView.getResources().getColor(model.getColor());
        mName.setText(model.getFirstName());
        mName.setTextColor(color);
        mTemp.setText(model.getTemp());
        mTemp.setTextColor(color);
        mIcon.setImageDrawable(model.getIcon());
        IconManager.animate(mIcon);

        isAcross = false;
        mAvdc.stop();

    }

    @Override
    public void onClick(View v) {
        mListener.onHolderClick(v, ViewHolderAction.select, getAdapterPosition());
    }

    public boolean isAcross() {
        return isAcross;
    }

    public void setAcross(boolean across) {
        isAcross = across;
    }

    public AnimatedVectorDrawableCompat getAvdc() {
        return mAvdc;
    }
}
