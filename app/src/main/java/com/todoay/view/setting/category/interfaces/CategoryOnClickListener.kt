package com.todoay.view.setting.category.interfaces

import com.todoay.data.category.Category

interface CategoryOnClickListener {
    fun onClick(category : Category)
    fun isChangedOrderIndex(isChanged : Boolean)
}