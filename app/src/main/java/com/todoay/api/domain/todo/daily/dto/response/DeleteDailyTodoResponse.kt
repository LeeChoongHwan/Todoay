package com.todoay.api.domain.todo.daily.dto.response

import com.google.gson.annotations.SerializedName

data class DeleteDailyTodoResponse(
    @SerializedName("id")
    val id : Long
)
