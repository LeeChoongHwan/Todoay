package com.todoay.api.domain.auth.dto.request

import com.google.gson.annotations.SerializedName

data class RefreshRequest (
    @SerializedName("refreshToken")
    val refreshToken: String
)