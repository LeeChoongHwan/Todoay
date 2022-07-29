package com.todoay.api.domain.auth.nickname.dto.response

import com.google.gson.annotations.SerializedName
import com.todoay.api.util.response.basic.BasicResponse

data class NicknameResponse(
    @SerializedName("status")
    override val status: Int,
    @SerializedName("nicknameExists")
    val nicknameExist: Boolean
): BasicResponse
