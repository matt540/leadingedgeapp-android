package com.surfboardapp.CustomClasses;

import android.graphics.Canvas;
import android.view.View;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.surfboardapp.Adapter.MessageAdapters;


public class RecyclerItemTouchHelper1 extends ItemTouchHelper.SimpleCallback {
    private RecyclerItemTouchHelperListener listener;

    public RecyclerItemTouchHelper1(int dragDirs, int swipeDirs, RecyclerItemTouchHelperListener listener) {
        super(dragDirs, swipeDirs);
        this.listener = listener;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return true;
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if (viewHolder != null) {
            if (viewHolder instanceof MessageAdapters.MyMessageViewHolder) {
                final View foregroundView = ((MessageAdapters.MyMessageViewHolder) viewHolder).getLin_my_message();

                getDefaultUIUtil().onSelected(foregroundView);
            }

        }
    }

    @Override
    public void onChildDrawOver(Canvas c, RecyclerView recyclerView,
                                RecyclerView.ViewHolder viewHolder, float dX, float dY,
                                int actionState, boolean isCurrentlyActive) {

        if (viewHolder != null) {
            if (viewHolder instanceof MessageAdapters.MyMessageViewHolder) {
                final View foregroundView = ((MessageAdapters.MyMessageViewHolder) viewHolder).getLin_my_message();
                getDefaultUIUtil().onDrawOver(c, recyclerView, foregroundView, dX, dY,
                        actionState, isCurrentlyActive);
            }
        }
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        if (viewHolder != null) {
            if (viewHolder instanceof MessageAdapters.MyMessageViewHolder) {
                final View foregroundView = ((MessageAdapters.MyMessageViewHolder) viewHolder).getLin_my_message();
                getDefaultUIUtil().clearView(foregroundView);
            }
        }
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView,
                            RecyclerView.ViewHolder viewHolder, float dX, float dY,
                            int actionState, boolean isCurrentlyActive) {

        if (viewHolder != null) {
            if (viewHolder instanceof MessageAdapters.MyMessageViewHolder) {
                final View foregroundView = ((MessageAdapters.MyMessageViewHolder) viewHolder).getLin_my_message();

                getDefaultUIUtil().onDraw(c, recyclerView, foregroundView, dX, dY,
                        actionState, isCurrentlyActive);
            }
        }
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        listener.onSwiped(viewHolder, direction, viewHolder.getAdapterPosition());
    }

    @Override
    public int convertToAbsoluteDirection(int flags, int layoutDirection) {
        return super.convertToAbsoluteDirection(flags, layoutDirection);
    }

    public interface RecyclerItemTouchHelperListener {
        void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position);
    }
}
