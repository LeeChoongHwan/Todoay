package com.todoay.data.todo.daily

import com.todoay.api.domain.hashtag.dto.Hashtag
import com.todoay.data.category.Category
import java.time.LocalDate
import java.time.LocalDateTime

data class DailyInfo(
    val id : Long,
    val todo : String,
    val alarm : Alarm?,
    val time : LocalDateTime?,
    val location : String?,
    val partner : String?,
    val date : LocalDate,
    val category : Category,
    val hashtagList : List<Hashtag>?,
    val isPublic : Boolean,
    val isFinish : Boolean
)