package com.todoay.api.domain.auth.nickName.dto

import com.google.gson.annotations.SerializedName

data class NickNameResponse(
    @SerializedName("status")
    val status: Int,
    @SerializedName("nicknameExists")
    val nicknameExist: Boolean
)
