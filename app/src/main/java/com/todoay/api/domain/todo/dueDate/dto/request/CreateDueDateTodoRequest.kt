package com.todoay.api.domain.todo.dueDate.dto.request

import com.google.gson.annotations.SerializedName
import com.todoay.api.domain.hashtag.dto.HashtagDto
import java.time.LocalDate

data class CreateDueDateTodoRequest(
    @SerializedName("title")
    val todo : String,
    @SerializedName("description")
    val description : String,
    @SerializedName("publicBool")
    val isPublic : Boolean,
    @SerializedName("dueDate")
    val dueDate : LocalDate,
    @SerializedName("importance")
    val priority : String,
    @SerializedName("hashtagNames")
    val hashtagList : List<HashtagDto>
)
