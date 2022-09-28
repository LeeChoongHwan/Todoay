package com.todoay.api.domain.auth.dto.request

import com.google.gson.annotations.SerializedName

data class ModifyPasswordRequest(
    @SerializedName("originPassword")
    val originPassword: String,
    @SerializedName("modifiedPassword")
    val modifiedPassword: String
)
