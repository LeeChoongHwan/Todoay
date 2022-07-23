package com.todoay.api.domain.auth.email.dto

import com.google.gson.annotations.SerializedName

data class EmailResponse(
    @SerializedName("status")
    val status: Int
)
