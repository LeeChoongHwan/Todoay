package com.todoay.api.auth.nickName

import com.google.gson.annotations.SerializedName

data class RequestNickName(
    @SerializedName("nickName")
    val nickName: String
)
