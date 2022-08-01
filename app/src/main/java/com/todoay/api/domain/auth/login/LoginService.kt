package com.todoay.api.domain.auth.login

import com.todoay.api.domain.auth.login.dto.request.LoginRequest
import com.todoay.api.domain.auth.login.dto.response.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * 로그인 API 호출 인터페이스
 */
interface LoginService {
    @POST("/auth/login")
    fun postLogin(@Body userData: LoginRequest) : Call<LoginResponse>
}