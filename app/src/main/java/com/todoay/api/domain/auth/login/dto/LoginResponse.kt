package com.todoay.api.domain.auth.login.dto

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("result")
    val result: String,
    @SerializedName("code")
    val code: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("accessToken")
    val accessToken: String,
    @SerializedName("refreshToken")
    val refreshToken: String?,
    @SerializedName("nickName")
    val nickName: String
)
