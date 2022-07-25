package com.todoay.api.util

/**
 * API 통신 실패 시 발생하는 Exception에 대한 정보
 */
data class Failure(
    /**
     * 예외 상황 발생 시간
     */
    val timestamp: String,
    /**
     * 예외 상황
     */
    val exception: Throwable,
    /**
     * 예외 상황 설명
     */
    val code: String,
    /**
     * 예외 상황 경로
     */
    val path: String
)