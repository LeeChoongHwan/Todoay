package com.todoay.api.domain.auth.refresh

import com.todoay.api.domain.auth.refresh.dto.request.RefreshRequest
import com.todoay.api.domain.auth.refresh.dto.response.RefreshResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * RefreshToken 관련 API 호출 인터페이스
 */
interface RefreshService {

    @POST("/auth/refresh")
    fun postRefreshToken(@Body refreshRequest: RefreshRequest) : Call<RefreshResponse>

}