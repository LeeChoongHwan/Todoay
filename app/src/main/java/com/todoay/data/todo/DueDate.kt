package com.todoay.data.todo

import com.todoay.api.domain.hashtag.dto.Hashtag
import java.time.LocalDate

data class DueDate(
    val id: Int,
    val todo: String,
    val dueDate: LocalDate,
    val priority: String,
    val hashtagList: List<Hashtag>?
)
