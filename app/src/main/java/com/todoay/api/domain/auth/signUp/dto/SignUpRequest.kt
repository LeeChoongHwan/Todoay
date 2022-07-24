package com.todoay.api.domain.auth.signUp.dto

import com.google.gson.annotations.SerializedName

data class SignUpRequest(
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("nickName")
    val nickName: String
)