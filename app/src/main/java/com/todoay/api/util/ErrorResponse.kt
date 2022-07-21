package com.todoay.api.util

import com.google.gson.annotations.SerializedName

data class ErrorResponse(
    @SerializedName("timestamp")
    val timestamp: String,
    @SerializedName("status")
    val status: Int,
    @SerializedName("error")
    val error: String,
    @SerializedName("code")
    val code: String,
    @SerializedName("path")
    val path: String
)