package com.todoay.api.domain.auth.nickName.dto

import com.google.gson.annotations.SerializedName

data class NickNameRequest(
    @SerializedName("nickName")
    val nickName: String
)
