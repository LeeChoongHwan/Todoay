package com.todoay.api.domain.auth.password.dto.response

import com.google.gson.annotations.SerializedName
import com.todoay.api.util.response.basic.BasicResponse

data class ModifyPasswordResponse(
    @SerializedName("status")
    override val status: Int
): BasicResponse
