package com.todoay.api.domain.profile.dto.response

import com.google.gson.annotations.SerializedName

data class ProfileResponse(
    @SerializedName("email")
    val email: String,
    @SerializedName("nickname")
    val nickname: String,
    @SerializedName("imageUrl")
    val imageUrl: String,
    @SerializedName("introMsg")
    val introMsg: String
)
