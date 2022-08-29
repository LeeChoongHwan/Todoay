package com.todoay.api.domain.auth.refresh.dto.response

import com.google.gson.annotations.SerializedName

data class RefreshResponse(
    @SerializedName("accessToken")
    val accessToken: String,
    @SerializedName("refreshToken")
    val refreshToken: String
)