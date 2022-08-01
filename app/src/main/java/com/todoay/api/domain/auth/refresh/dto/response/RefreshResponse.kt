package com.todoay.api.domain.auth.refresh.dto.response

import com.google.gson.annotations.SerializedName
import com.todoay.api.util.response.basic.BasicResponse

data class RefreshResponse(
    @SerializedName("status")
    override val status: Int,
    @SerializedName("accessToken")
    val accessToken: String,
    @SerializedName("refreshToken")
    val refreshToken: String
): BasicResponse