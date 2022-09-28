package com.todoay.data.todo.daily

import com.todoay.api.domain.hashtag.dto.Hashtag
import com.todoay.data.category.Category
import java.time.LocalDate
import java.time.LocalDateTime

data class DailyInfo(
    val id : Long,
    val todo : String,
    val isPublic : Boolean,
    val isFinish : Boolean,
    val alarm : Alarm?,
    val time : LocalDateTime?,
    val repeatId : Long,
    val location : String?,
    val partner : String?,
    val date : LocalDate,
    val category : Category,
    val hashtagList : List<Hashtag>?
)