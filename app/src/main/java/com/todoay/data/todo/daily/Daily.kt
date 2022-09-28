package com.todoay.data.todo.daily

import com.todoay.api.domain.hashtag.dto.Hashtag

data class Daily(
    val id : Long,
    var todo : String,
    var categoryId : Long,
    var repeatId : Long,
    var hashtagList : List<Hashtag>?,
    var isPublic : Boolean,
    var isFinished : Boolean
)
