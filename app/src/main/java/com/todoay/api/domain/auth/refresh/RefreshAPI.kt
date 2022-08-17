package com.todoay.api.domain.auth.refresh

import com.todoay.api.config.RetrofitService
import com.todoay.api.config.ServiceRepository.AuthServiceRepository.callRefreshService
import com.todoay.api.domain.auth.refresh.dto.request.RefreshRequest
import com.todoay.api.domain.auth.refresh.dto.response.RefreshResponse
import com.todoay.api.util.response.error.ErrorResponse

/**
 * RefreshToken 관련 API 호출 및 응답을 처리하는 클래스.
 * API Interface: callRefreshService().kt
 */
class RefreshAPI {

    /**
     * 토큰 재발행 수행
     * [POST]("/auth/refresh")
     */
    fun refreshTokenToAccessToken(refreshToken: String, onResponse: (RefreshResponse) -> Unit, onErrorResponse: (ErrorResponse) -> Unit) {
        val request = RefreshRequest(
            refreshToken = refreshToken
        )
        /* execute()를 통한 동기성 처리 */
        val response = callRefreshService().postRefreshToken(request).execute()
        if(response.isSuccessful) {
            val refreshResponse : RefreshResponse = response.body()!!
            onResponse(refreshResponse)
        }
        else {
            val errorResponse = RetrofitService.getErrorResponse(response)
            onErrorResponse(errorResponse)
        }
    }

}