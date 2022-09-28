package com.todoay.api.domain.todo.daily.dto.request

import com.google.gson.annotations.SerializedName

data class DailyRepeatRequest(
    @SerializedName("repeatType")
    val repeatType : String,
    @SerializedName("duration")
    val duration : String,
    @SerializedName("repeat")
    val repeat : Int
)
