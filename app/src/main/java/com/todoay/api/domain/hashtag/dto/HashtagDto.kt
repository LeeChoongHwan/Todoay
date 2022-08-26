package com.todoay.api.domain.hashtag.dto

import com.google.gson.annotations.SerializedName

data class HashtagDto(
    @SerializedName("name")
    val name : String
)
