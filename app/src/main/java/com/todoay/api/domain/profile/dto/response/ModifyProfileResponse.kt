package com.todoay.api.domain.profile.dto.response

import com.google.gson.annotations.SerializedName
import com.todoay.api.util.response.basic.BasicResponse

data class ModifyProfileResponse(
    @SerializedName("status")
    override val status: Int
): BasicResponse
