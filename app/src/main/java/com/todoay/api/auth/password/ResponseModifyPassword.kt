package com.todoay.api.auth.password

import com.google.gson.annotations.SerializedName

data class ResponseModifyPassword(
    @SerializedName("result")
    val result: String,
    @SerializedName("code")
    val code: Int,
    @SerializedName("message")
    val message: String
)
