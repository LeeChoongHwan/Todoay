package com.todoay.api.domain.auth.email.dto

import com.google.gson.annotations.SerializedName

data class EmailRequest(
    @SerializedName("email")
    val email: String
)
