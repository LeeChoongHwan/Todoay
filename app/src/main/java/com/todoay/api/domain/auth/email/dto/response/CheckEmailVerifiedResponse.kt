package com.todoay.api.domain.auth.email.dto.response

import com.google.gson.annotations.SerializedName

data class CheckEmailVerifiedResponse(
    @SerializedName("emailVerified")
    val emailVerified: Boolean
)
