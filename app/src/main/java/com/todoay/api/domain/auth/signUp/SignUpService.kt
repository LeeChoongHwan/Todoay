package com.todoay.api.domain.auth.signUp

import com.todoay.api.domain.auth.signUp.dto.request.SignUpRequest
import com.todoay.api.domain.auth.signUp.dto.response.SignUpResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * 회원가입(Sign-Up) API 호출 인터페이스
 */
interface SignUpService {
    @POST("/auth/sign-up")
    fun postSignUp(@Body requestBody: SignUpRequest) : Call<SignUpResponse>
}