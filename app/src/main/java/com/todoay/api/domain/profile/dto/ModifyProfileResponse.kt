package com.todoay.api.domain.profile.dto

import com.google.gson.annotations.SerializedName

data class ModifyProfileResponse(
    @SerializedName("result")
    val result: String,
    @SerializedName("code")
    val code: Int,
    @SerializedName("message")
    val message: String
)
