package com.todoay.api.domain.auth.password.dto.request

import com.google.gson.annotations.SerializedName

data class ModifyPasswordRequest(
    @SerializedName("currentPassword")
    val currentPassword: String,
    @SerializedName("modifyPassword")
    val modifyPassword: String
)
