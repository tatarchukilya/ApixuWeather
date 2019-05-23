package com.example.apixuweather.ui.root.recycler;

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
import butterknife.BindView;

import static com.example.apixuweather.utils.Const.I_TAG;

public class HourViewHolder extends BaseViewHolder<HourItem> implements View.OnTouchListener, View.OnLongClickListener {

    public static final String TAG = I_TAG + HourViewHolder.class.getSimpleName();

    @LayoutRes
    public static final int LAYOUT = R.layout.view_hour;

    private ViewHolderTouchListener mTouchListener;

    @BindView(R.id.temperature)
    TextView mTemp;
    @BindView(R.id.time)
    TextView mTime;
    @BindView(R.id.icon)
    ImageView mIcon;

    public HourViewHolder(@NonNull View itemView, ViewHolderTouchListener listener) {
        super(itemView, listener);
        itemView.setOnTouchListener(this);
        itemView.setOnLongClickListener(this);
        mTouchListener = listener;
    }

    @Override
    public void bind(HourItem model) {
        mTemp.setText(model.getTemp());
        mTime.setText(model.getTime());
        mIcon.setImageDrawable(model.getIcon());
        IconManager.animate(mIcon);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
     /*   if (  event.getAction() == MotionEvent.ACTION_DOWN ){
            mTouchListener.onHolderClick(v, event.getAction(), mHourItem);
            return true;
        }*/

        if (event.getAction() == MotionEvent.ACTION_CANCEL || event.getAction() == MotionEvent.ACTION_UP) {
            mTouchListener.onHolderClick(v, ViewHolderAction.cancel, getAdapterPosition());
        }
        return false;
    }

    @Override
    public boolean onLongClick(View v) {
        v.setTag(TAG);
        mTouchListener.onHolderClick(v, ViewHolderAction.longTouch, getAdapterPosition());
        return true;
    }
}
