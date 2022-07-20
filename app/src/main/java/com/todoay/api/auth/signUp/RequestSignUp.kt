package com.todoay.api.auth.signUp

import com.google.gson.annotations.SerializedName

data class RequestSignUp(
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("nickName")
    val nickName: String
)
