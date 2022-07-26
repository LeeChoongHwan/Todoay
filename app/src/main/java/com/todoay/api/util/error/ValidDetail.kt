package com.todoay.api.util.error

import com.google.gson.annotations.SerializedName

data class ValidDetail(
    /**
     * Valid Error 코드
     */
    @SerializedName("code")
    val code: String,
    /**
     * Valid Error Entity
     */
    @SerializedName("field")
    val field: String,
    /**
     * Valid Error Value
     */
    @SerializedName("rejectedValue")
    val rejectedValue: Any,
    /**
     * Valid Error 메시지
     */
    @SerializedName("message")
    val message: String
)
