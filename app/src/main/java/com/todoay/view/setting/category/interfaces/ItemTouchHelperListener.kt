package com.todoay.view.setting.category.interfaces

interface ItemTouchHelperListener {
    fun onItemMove(fromPosition : Int, toPosition : Int) : Boolean
    fun onItemSwipe(position : Int)
}