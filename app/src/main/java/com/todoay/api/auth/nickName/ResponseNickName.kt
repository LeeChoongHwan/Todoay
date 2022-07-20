package com.todoay.api.auth.nickName

import com.google.gson.annotations.SerializedName

data class ResponseNickName(
    @SerializedName("result")
    val result: String,
    @SerializedName("code")
    val code: Int,
    @SerializedName("message")
    val message: String
)
