package com.todoay.view.setting.category.interfaces

import com.todoay.data.category.Category

interface ModifiedCategoryResult {
    fun isModified(isResult: Boolean, modifiedCategory : Category)
}