package com.todoay.api.domain.todo.daily.dto.response

import com.google.gson.annotations.SerializedName

data class CreateDailyTodoResponse(
    @SerializedName("id")
    val id : Long
)