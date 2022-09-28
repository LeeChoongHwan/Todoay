package com.todoay.view.todo.common.interfaces

interface TodoInfoChangedStateResult {
    fun isChangedState(isModified : Boolean, isDeleted : Boolean)
}