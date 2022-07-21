package com.todoay.api.domain.auth.password.dto

import com.google.gson.annotations.SerializedName

data class ModifyPasswordResponse(
    @SerializedName("result")
    val result: String,
    @SerializedName("code")
    val code: Int,
    @SerializedName("message")
    val message: String
)
