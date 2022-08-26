package com.todoay.api.domain.hashtag.dto.response

import com.google.gson.annotations.SerializedName
import com.todoay.api.domain.hashtag.dto.HashtagDto

data class HashtagResponse(
    @SerializedName("hasNext")
    val hasNext : Boolean,
    @SerializedName("nextPageNum")
    val nextPageNum : Int,
    @SerializedName("infos")
    val hashtagList : List<HashtagDto>
)
