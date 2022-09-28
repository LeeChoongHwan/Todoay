package com.todoay.api.domain.auth.dto.response

import com.google.gson.annotations.SerializedName

data class NicknameResponse(
    @SerializedName("nicknameExists")
    val nicknameExist: Boolean
)
