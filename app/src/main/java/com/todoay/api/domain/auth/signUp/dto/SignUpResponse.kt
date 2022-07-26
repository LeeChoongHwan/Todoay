package com.todoay.api.domain.auth.signUp.dto

import com.google.gson.annotations.SerializedName

data class SignUpResponse(
    @SerializedName("status")
    val status: Int,
)
