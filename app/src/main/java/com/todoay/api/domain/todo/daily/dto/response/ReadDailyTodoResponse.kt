package com.todoay.api.domain.todo.daily.dto.response

import com.google.gson.annotations.SerializedName
import com.todoay.api.domain.category.dto.CategoryInfoDto
import com.todoay.api.domain.hashtag.dto.Hashtag
import com.todoay.data.category.Category
import java.time.LocalDate
import java.time.LocalDateTime

data class ReadDailyTodoResponse(
    @SerializedName("id")
    val id : Long,
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
    @SerializedName("category")
    val categoryInfoDto : CategoryInfoDto,
    @SerializedName("hashtagNames")
    val hashtagList : List<Hashtag>?,
    @SerializedName("public")
    val isPublic : Boolean,
    @SerializedName("finished")
    val isFinish : Boolean
)