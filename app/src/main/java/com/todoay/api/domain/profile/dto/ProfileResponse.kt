package com.todoay.api.domain.profile.dto

import com.google.gson.annotations.SerializedName

data class ProfileResponse(
    @SerializedName("email")
    val email: String,
    @SerializedName("nickName")
    val nickName: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("imageUrl")
    val imageUrl: String
)
