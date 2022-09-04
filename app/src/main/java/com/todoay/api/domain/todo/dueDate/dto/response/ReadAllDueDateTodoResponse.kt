package com.todoay.api.domain.todo.dueDate.dto.response

import com.google.gson.annotations.SerializedName
import com.todoay.api.domain.hashtag.dto.Hashtag
import java.time.LocalDate

data class ReadAllDueDateTodoResponse(
    @SerializedName("id")
    val id : Int,
    @SerializedName("title")
    val todo : String,
    @SerializedName("dueDate")
    val dueDate : LocalDate,
    @SerializedName("importance")
    val priority : String,
    @SerializedName("hashtagInfos")
    val hashtagList : List<Hashtag>?
)
