package com.todoay.api.domain.todo.daily.dto.response

import com.google.gson.annotations.SerializedName
import com.todoay.api.domain.hashtag.dto.Hashtag

data class ReadAllDailyTodoResponse(
    @SerializedName("id")
    val id : Long,
    @SerializedName("title")
    val todo : String,
    @SerializedName("categoryId")
    val categoryId : Long,
    @SerializedName("repeatId")
    val repeatId : Long,
    @SerializedName("hashtagInfoDtos")
    val hashtagList : List<Hashtag>,
    @SerializedName("public")
    val isPublic : Boolean,
    @SerializedName("finished")
    val isFinished : Boolean
)