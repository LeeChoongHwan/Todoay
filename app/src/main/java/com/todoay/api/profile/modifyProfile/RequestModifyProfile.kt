package com.todoay.api.profile.modifyProfile

import com.google.gson.annotations.SerializedName

data class RequestModifyProfile(
    @SerializedName("nickName")
    val nickName: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("imageUrl")
    val imageUrl: String
)
