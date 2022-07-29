package com.todoay.api.domain.auth.refresh.dto.request

import com.google.gson.annotations.SerializedName

data class RefreshRequest (
    @SerializedName("refreshToken")
    val refreshToken: String
)