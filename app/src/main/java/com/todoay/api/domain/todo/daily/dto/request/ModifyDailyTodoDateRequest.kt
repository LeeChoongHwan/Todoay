package com.todoay.api.domain.todo.daily.dto.request

import com.google.gson.annotations.SerializedName
import java.time.LocalDate

data class ModifyDailyTodoDateRequest(
    @SerializedName("dailyDate")
    val date : LocalDate
)
