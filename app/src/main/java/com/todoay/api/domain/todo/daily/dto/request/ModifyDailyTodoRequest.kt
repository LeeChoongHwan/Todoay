package com.todoay.api.domain.todo.daily.dto.request

import com.google.gson.annotations.SerializedName
import com.todoay.api.domain.hashtag.dto.Hashtag
import java.time.LocalDate
import java.time.LocalDateTime

data class ModifyDailyTodoRequest(
    @SerializedName("title")
    val todo : String,
    @SerializedName("publicBool")
    val isPublic : Boolean,
    @SerializedName("dailyDate")
    val date : LocalDate,
    @SerializedName("alarm")
    val alarm : LocalDateTime?,
    @SerializedName("targetTime")
    val time : LocalDateTime?,
    @SerializedName("place")
    val location : String?,
    @SerializedName("people")
    val partner : String?,
    @SerializedName("categoryId")
    val categoryId : Long,
    @SerializedName("hashtagNames")
    val hashtagList : List<Hashtag>?
)
