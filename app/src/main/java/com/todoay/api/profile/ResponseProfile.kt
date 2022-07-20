package com.todoay.api.profile

import com.google.gson.annotations.SerializedName

data class ResponseProfile(
    @SerializedName("email")
    val email: String,
    @SerializedName("nickName")
    val nickName: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("imageUrl")
    val imageUrl: String
)
