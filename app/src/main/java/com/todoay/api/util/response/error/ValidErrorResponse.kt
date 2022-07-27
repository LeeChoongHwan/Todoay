package com.todoay.api.util.response.error

import com.google.gson.annotations.SerializedName

class ValidErrorResponse(
    timestamp: String,
    status: Int,
    error: String,
    code: String?,
    message: String?,
    path: String,
    /**
     * Valid Error Details List
     */
    @SerializedName("details")
    val details: List<ValidDetail>
) : ErrorResponse(
    timestamp,
    status,
    error,
    code,
    message,
    path) {
    override fun toString(): String {
        return super.toString() + "\n" +
                "ErrorResponse.Details{(${details}"
    }
}