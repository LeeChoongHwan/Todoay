package com.todoay.api.domain.todo.dueDate.dto.response

import com.google.gson.annotations.SerializedName
import com.todoay.api.domain.hashtag.dto.Hashtag
import java.time.LocalDate

data class ReadDueDateTodoResponse(
    @SerializedName("id")
    val id : Long,
    @SerializedName("title")
    val todo : String,
    @SerializedName("description")
    val description : String,
    @SerializedName("hashtagInfoDtos")
    val hashtagList : List<Hashtag>,
    @SerializedName("dueDate")
    val dueDate : LocalDate,
    @SerializedName("importance")
    val priority : String,
    @SerializedName("public")
    val isPublic : Boolean,
    @SerializedName("finished")
    val isFinish : Boolean
)
