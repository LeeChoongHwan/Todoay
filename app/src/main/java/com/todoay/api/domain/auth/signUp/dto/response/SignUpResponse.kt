package com.todoay.api.domain.auth.signUp.dto.response

import com.google.gson.annotations.SerializedName
import com.todoay.api.util.response.basic.BasicResponse

data class SignUpResponse(
    @SerializedName("status")
    override val status: Int,
): BasicResponse
