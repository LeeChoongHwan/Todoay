package com.todoay.api.util.response.error

import com.google.gson.annotations.SerializedName

open class ErrorResponse(
    /**
     * Error 발생 시간
     */
    @SerializedName("timestamp")
    val timestamp: String,
    /**
     * HTTP Status Code
     */
    @SerializedName("status")
    val status: Int,
    /**
     * Error Status Code 이름
     */
    @SerializedName("error")
    val error: String?,
    /**
     * Error 코드
     */
    @SerializedName("code")
    val code: String?,
    /**
     * Error 메시지
     */
    @SerializedName("message")
    val message: String?,
    /**
     * Error api 경로
     */
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