package com.todoay.api.domain.profile.dto.request

import com.google.gson.annotations.SerializedName

data class ModifyProfileRequest(
    @SerializedName("nickName")
    val nickName: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("imageUrl")
    val imageUrl: String
)
