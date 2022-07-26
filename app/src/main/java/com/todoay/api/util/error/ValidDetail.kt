package com.todoay.api.util.error

import com.google.gson.annotations.SerializedName

data class ValidDetail(
    @SerializedName("code")
    val code: String,
    @SerializedName("field")
    val field: String,
    @SerializedName("rejectedValue")
    val rejectedValue: Any,
    @SerializedName("message")
    val message: String
)
