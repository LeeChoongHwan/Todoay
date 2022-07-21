package com.todoay.api.auth.email

import com.google.gson.annotations.SerializedName

data class ResponseEmail(
    @SerializedName("result")
    val result: String,
    @SerializedName("code")
    val code: Int,
    @SerializedName("message")
    val message: String
)
