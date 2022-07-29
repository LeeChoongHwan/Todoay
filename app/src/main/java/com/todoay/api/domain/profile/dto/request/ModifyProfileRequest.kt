package com.todoay.api.domain.profile.dto.request

import com.google.gson.annotations.SerializedName

data class ModifyProfileRequest(
    @SerializedName("nickname")
    val nickname: String,
    @SerializedName("imageUrl")
    val imageUrl: String,
    @SerializedName("introMsg")
    val introMsg: String
)
