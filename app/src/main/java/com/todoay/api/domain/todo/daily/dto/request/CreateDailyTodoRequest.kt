package com.todoay.api.domain.todo.daily.dto.request

import com.google.gson.annotations.SerializedName
import com.todoay.api.domain.hashtag.dto.Hashtag
import java.time.LocalDate
import java.time.LocalDateTime

data class CreateDailyTodoRequest(
    @SerializedName("title")
    val todo : String,
    @SerializedName("alarm")
    val alarm : LocalDateTime?,
    @SerializedName("targetTime")
    val time : LocalDateTime?,
    @SerializedName("place")
    val location : String?,
    @SerializedName("people")
    val partner : String?,
    @SerializedName("dailyDate")
    val date : LocalDate,
    @SerializedName("categoryId")
    val categoryId : Long,
    @SerializedName("hashtagNames")
    val hashtagList : List<Hashtag>?,
    @SerializedName("public")
    val isPublic : Boolean
)
