package com.todoay.api.auth.password

import com.google.gson.annotations.SerializedName

data class RequestModifyPassword(
    @SerializedName("currentPassword")
    val currentPassword: String,
    @SerializedName("modifyPassword")
    val modifyPassword: String
)
