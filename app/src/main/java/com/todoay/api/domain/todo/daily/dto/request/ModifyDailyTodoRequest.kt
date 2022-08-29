package com.todoay.api.domain.todo.daily.dto.request

import com.google.gson.annotations.SerializedName
import com.todoay.api.domain.hashtag.dto.HashtagDto
import java.time.LocalDate
import java.time.LocalDateTime

data class ModifyDailyTodoRequest(
    @SerializedName("title")
    val todo : String,
    @SerializedName("alarm")
    val alarm : LocalDateTime,
    @SerializedName("targetTime")
    val time : LocalDateTime,
    @SerializedName("place")
    val location : String,
    @SerializedName("people")
    val partner : String,
    @SerializedName("dailyDate")
    val date : LocalDate,
    @SerializedName("categoryId")
    val categoryId : Int,
    @SerializedName("hashtagNames")
    val hashtagList : List<HashtagDto>,
    @SerializedName("public")
    val isPublic : Boolean,
    @SerializedName("finished")
    val isFinish : Boolean
)
