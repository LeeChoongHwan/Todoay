package com.todoay.api.domain.auth.signUp.dto

import com.google.gson.annotations.SerializedName

data class SignUpResponse(
    @SerializedName("result")
    val result: String,
    @SerializedName("code")
    val code: Int,
    @SerializedName("accessToken")
    val accessToken: String,
    @SerializedName("refreshToken")
    val refreshToken: String?,
    @SerializedName("nickName")
    val nickName: String

)
