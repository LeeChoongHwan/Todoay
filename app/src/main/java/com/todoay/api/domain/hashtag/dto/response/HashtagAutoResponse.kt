package com.todoay.api.domain.hashtag.dto.response

import com.google.gson.annotations.SerializedName
import com.todoay.api.domain.hashtag.dto.Hashtag

data class HashtagAutoResponse(
    @SerializedName("infos")
    val hashtagList : List<Hashtag>
)
