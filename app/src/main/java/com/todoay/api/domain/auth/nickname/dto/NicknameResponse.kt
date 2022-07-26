package com.todoay.api.domain.auth.nickname.dto

import com.google.gson.annotations.SerializedName

data class NicknameResponse(
    @SerializedName("status")
    val status: Int,
    @SerializedName("nicknameExists")
    val nicknameExist: Boolean
)
