package com.todoay.api.domain.todo.daily.dto.response

import com.google.gson.annotations.SerializedName
import com.todoay.api.domain.category.dto.CategoryDto
import com.todoay.api.domain.hashtag.dto.HashtagDto

data class ReadAllDailyTodoResponse(
    @SerializedName("id")
    val id : Int,
    @SerializedName("title")
    val todo : String,
    @SerializedName("categoryInfoDto")
    val categoryList : List<CategoryDto>,
    @SerializedName("hashtagInfoDtos")
    val hashtagList : List<HashtagDto>
)