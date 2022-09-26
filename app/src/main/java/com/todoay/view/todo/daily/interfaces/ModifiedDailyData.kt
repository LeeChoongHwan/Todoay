package com.todoay.view.todo.daily.interfaces

import com.todoay.data.todo.daily.DailyInfo

interface ModifiedDailyData {
    fun isModified(isModified: Boolean, modifiedData : DailyInfo)
}