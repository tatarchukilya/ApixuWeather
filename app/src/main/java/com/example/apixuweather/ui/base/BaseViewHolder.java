package com.example.apixuweather.ui.base;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.ButterKnife;

public abstract class BaseViewHolder<M extends BaseItem> extends RecyclerView.ViewHolder {

    protected ViewHolderTouchListener mListener;

    public BaseViewHolder(@NonNull View itemView, ViewHolderTouchListener listener) {
        super(itemView);
        mListener = listener;
        ButterKnife.bind(this, itemView);
    }

    public abstract void bind(M model);

}
