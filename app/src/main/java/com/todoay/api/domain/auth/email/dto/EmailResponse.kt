package com.todoay.api.domain.auth.email.dto

import com.google.gson.annotations.SerializedName

data class EmailResponse(
    @SerializedName("result")
    val result: String,
    @SerializedName("code")
    val code: Int,
    @SerializedName("message")
    val message: String
)
