package com.todoay.api.auth.email

import com.google.gson.annotations.SerializedName

data class RequestEmail(
    @SerializedName("email")
    val email: String
)
