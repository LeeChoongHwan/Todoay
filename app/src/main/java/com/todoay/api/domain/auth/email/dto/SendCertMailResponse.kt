package com.todoay.api.domain.auth.email.dto

import com.google.gson.annotations.SerializedName

data class SendCertMailResponse(
    @SerializedName("status")
    val status: Int
)
