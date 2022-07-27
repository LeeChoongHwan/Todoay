package com.todoay.api.domain.auth.email.dto.response

import com.google.gson.annotations.SerializedName
import com.todoay.api.util.response.basic.BasicResponse

data class SendCertMailResponse(
    @SerializedName("status")
    override val status: Int
) : BasicResponse
