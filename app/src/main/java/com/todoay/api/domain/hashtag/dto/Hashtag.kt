package com.todoay.api.domain.hashtag.dto

import com.google.gson.annotations.SerializedName

data class Hashtag(
    @SerializedName("name")
    val name : String
) {
    override fun equals(other: Any?): Boolean = (other is Hashtag) && name == other.name
}
