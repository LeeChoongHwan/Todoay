package com.todoay.api.domain.auth.nickName.dto

import com.google.gson.annotations.SerializedName

data class NickNameResponse(
    @SerializedName("result")
    val result: String,
    @SerializedName("code")
    val code: Int,
    @SerializedName("message")
    val message: String
)
