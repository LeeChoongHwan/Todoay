package com.todoay.api.domain.auth.login.dto.response

import com.google.gson.annotations.SerializedName
import com.todoay.api.util.response.basic.BasicResponse

data class LoginResponse(
    @SerializedName("status")
    override val status: Int,
    @SerializedName("accessToken")
    val accessToken: String,
    @SerializedName("refreshToken")
    val refreshToken: String
): BasicResponse
