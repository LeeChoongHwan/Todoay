package com.todoay.data.todo.dueDate

import com.todoay.api.domain.hashtag.dto.Hashtag
import java.time.LocalDate

data class DueDateInfo(
    val id: Long,
    val todo: String,
    val description : String?,
    val hashtagList: List<Hashtag>?,
    val dueDate: LocalDate,
    val priority: String,
    val isPublic : Boolean,
    val isFinish : Boolean
)
