package com.todoay.view.setting.category.util

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.todoay.view.setting.category.interfaces.ItemTouchHelperListener

class ItemTouchHelperCallback(private val listener : ItemTouchHelperListener) : ItemTouchHelper.Callback() {

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        val dragFlag = ItemTouchHelper.UP or ItemTouchHelper.DOWN
//        val swipeFlag = ItemTouchHelper.START or ItemTouchHelper.END // NOT USED
        return makeMovementFlags(dragFlag, 0)
    }

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        return listener.onItemMove(viewHolder.adapterPosition, target.adapterPosition)
    }

    // NOT USED
    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
//        listener.onItemSwipe(viewHolder.adapterPosition)
        return
    }

    override fun isLongPressDragEnabled(): Boolean {
        return true
    }

}