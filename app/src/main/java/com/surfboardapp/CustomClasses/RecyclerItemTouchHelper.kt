package com.surfboardapp.CustomClasses

import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.surfboardapp.Adapter.MessageAdapters


class RecyclerItemTouchHelper(dragDirs: Int, swipeDirs: Int) :
    ItemTouchHelper.SimpleCallback(dragDirs, swipeDirs) {

    lateinit var listener: RecyclerItemTouchHelperListener
    fun RecyclerItemTouchHelper(
        dragDirs: Int,
        swipeDirs: Int,
        listener: RecyclerItemTouchHelperListener
    ) {

        this.listener = listener
    }


    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return true
    }

    override fun onSelectedChanged(
        viewHolder: RecyclerView.ViewHolder?,
        actionState: Int
    ) {
        if (viewHolder != null) {
            if (viewHolder is MessageAdapters.MyMessageViewHolder) {
                val foregroundView: View =
                    viewHolder.lin_my_message
                ItemTouchHelper.Callback.getDefaultUIUtil()
                    .onSelected(foregroundView)
            }

        }
    }


    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        if (viewHolder is MessageAdapters.MyMessageViewHolder) {
            listener.onSwiped(viewHolder, direction, viewHolder.adapterPosition)
        }

    }

    interface RecyclerItemTouchHelperListener {
        fun onSwiped(
            viewHolder: RecyclerView.ViewHolder?,
            direction: Int,
            position: Int
        )
    }
}