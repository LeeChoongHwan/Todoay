package com.todoay.api.util.response.error

/**
 * API 통신 실패 시 발생하는 Exception 정보
 */
data class FailureResponse(
    /**
     * Exception 발생 시간
     */
    val timestamp: String,
    /**
     * Exception 상황
     */
    val exception: Throwable,
    /**
     * Exception 상황 설명
     */
    val code: String,
    /**
     * Exception 상황 api 경로
     */
    val path: String
)