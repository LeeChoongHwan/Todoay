package com.todoay.api.domain.hashtag.dto.response

import com.google.gson.annotations.SerializedName
import com.todoay.api.domain.hashtag.dto.HashtagDto

data class HashtagAutoResponse(
    @SerializedName("infos")
    val hashtagList : List<HashtagDto>
)
