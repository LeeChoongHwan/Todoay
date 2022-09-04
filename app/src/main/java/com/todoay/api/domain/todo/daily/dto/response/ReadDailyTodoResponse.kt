package com.todoay.api.domain.todo.daily.dto.response

import com.google.gson.annotations.SerializedName
import com.todoay.api.domain.category.dto.CategoryDto
import com.todoay.api.domain.hashtag.dto.Hashtag
import java.time.LocalDate
import java.time.LocalDateTime

data class ReadDailyTodoResponse(
    @SerializedName("id")
    val id : Int,
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
    @SerializedName("category")
    val categoryList : List<CategoryDto>,
    @SerializedName("hashtagNames")
    val hashtagList : List<Hashtag>,
    @SerializedName("public")
    val isPublic : Boolean,
    @SerializedName("finished")
    val isFinish : Boolean
)