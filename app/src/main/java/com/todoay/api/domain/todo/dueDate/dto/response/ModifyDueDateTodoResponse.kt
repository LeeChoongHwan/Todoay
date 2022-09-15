package com.todoay.api.domain.todo.dueDate.dto.response

import com.google.gson.annotations.SerializedName

data class ModifyDueDateTodoResponse(
    @SerializedName("id")
    val id : Long
)
