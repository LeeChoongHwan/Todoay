package com.todoay.api.util.error

import com.google.gson.annotations.SerializedName

open class ErrorResponse(
    @SerializedName("timestamp")
    val timestamp: String,
    @SerializedName("status")
    val status: Int,
    @SerializedName("error")
    val error: String,
    @SerializedName("code")
    val code: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("path")
    val path: String
) {
    override fun toString(): String {
        return "{ErrorResponse(" +
                "timestamp=$timestamp, " +
                "status=$status, " +
                "error=$error, " +
                "code=$code, " +
                "message=$message, " +
                "path=$path)}"
    }
}