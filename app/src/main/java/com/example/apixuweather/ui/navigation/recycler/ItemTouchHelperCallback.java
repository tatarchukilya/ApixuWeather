package com.example.apixuweather.ui.navigation.recycler;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;

import com.example.apixuweather.utils.SizeConverter;
import com.example.apixuweather.utils.SystemUtils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;

import static com.example.apixuweather.utils.Const.I_TAG;

public class ItemTouchHelperCallback extends ItemTouchHelper.Callback {

    public static final String TAG = I_TAG + ItemTouchHelperCallback.class.getSimpleName();

    private final ItemTouchHelperAdapter mItemTouchHelperAdapter;
    private boolean isAcross = false;


    public ItemTouchHelperCallback(ItemTouchHelperAdapter itemTouchHelperAdapter) {
        mItemTouchHelperAdapter = itemTouchHelperAdapter;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        //return makeMovementFlags(0, ItemTouchHelper.START | ItemTouchHelper.END);
        if (viewHolder instanceof LocationViewHolder) {
            return makeMovementFlags(0, ItemTouchHelper.END);
        } else {
            return makeMovementFlags(0, 0);
        }
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        mItemTouchHelperAdapter.onItemDismiss(viewHolder.itemView, viewHolder.getAdapterPosition());

    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView,
                            @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

        LocationViewHolder holder = (LocationViewHolder) viewHolder;
        c.clipRect(0f, viewHolder.itemView.getTop(), dX, viewHolder.itemView.getBottom());

        AnimatedVectorDrawableCompat mAvdc = holder.getAvdc();

        float margin = SizeConverter.dpToPx(16);
        mAvdc.setBounds(new Rect((int) margin,
                (int) (holder.itemView.getTop() + margin),
                (int) (margin + SizeConverter.dpToPx(24)),
                (int) (holder.itemView.getTop() + SizeConverter.dpToPx(24) + margin)));
        mAvdc.draw(c);

        if (dX > SystemUtils.getDisplayMetric().x / 2 && !holder.isAcross()) {
            Log.i(TAG, "across");
            if (!mAvdc.isRunning()){
                Log.i(TAG, "avdc running: " + mAvdc.isRunning());
                mAvdc.start();
                SystemUtils.vibrate(10);
                holder.setAcross(true);
            }

        } else if (holder.isAcross() && dX < SystemUtils.getDisplayMetric().x / 2) {
            Log.i(TAG, "across back");
            holder.setAcross(false);
        }
    }

    @Override
    public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
    }
}
