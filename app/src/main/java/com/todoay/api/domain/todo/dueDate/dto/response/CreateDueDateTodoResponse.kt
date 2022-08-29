package com.todoay.api.domain.todo.dueDate.dto.response

import com.google.gson.annotations.SerializedName

data class CreateDueDateTodoResponse(
    @SerializedName("id")
    val id : Int
)
