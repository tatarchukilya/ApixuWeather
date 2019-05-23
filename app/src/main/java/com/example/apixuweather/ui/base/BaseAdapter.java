package com.example.apixuweather.ui.base;

import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import static com.example.apixuweather.utils.Const.I_TAG;

public class BaseAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    public final String TAG = I_TAG + getClass().getSimpleName();

    private List<BaseItem> mList;
    private ViewHolderTypeFactory mHolderFactory;
    protected ViewHolderTouchListener mListener;

    public BaseAdapter() {
        mList = new ArrayList<>();
        mHolderFactory = new ViewHolderTypeFactory();
    }

    public BaseAdapter(ViewHolderTouchListener listener) {
        mList = new ArrayList<>();
        mHolderFactory = new ViewHolderTypeFactory();
        mListener = listener;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        return mHolderFactory.createViewHolder(viewGroup, position  , mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int viewType) {
        holder.bind(mList.get(viewType));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mList.get(position).getLayoutType();
    }

    public void setList(List<BaseItem> list) {
        mList = list;
        notifyDataSetChanged();
    }

    public void clear(){
        mList.clear();
        notifyDataSetChanged();
    }

    public List<BaseItem> getList() {
        return mList;
    }

    public void remove(int position){
        mList.remove(position);
        notifyItemRemoved(position);
    }
}
