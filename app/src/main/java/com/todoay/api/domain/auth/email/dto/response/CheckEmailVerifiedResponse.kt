package com.todoay.api.domain.auth.email.dto.response

import com.google.gson.annotations.SerializedName
import com.todoay.api.util.response.basic.BasicResponse

data class CheckEmailVerifiedResponse(
    @SerializedName("status")
    override val status: Int,
    @SerializedName("emailVerified")
    val emailVerified: Boolean
): BasicResponse
