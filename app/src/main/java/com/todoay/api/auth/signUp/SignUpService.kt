package com.todoay.api.auth.signUp

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

/**
 * 회원가입(Sign-Up) API 호출 인터페이스
 */
interface SignUpService {
    @POST("/auth/sign-up")
    fun postSignUp(@Body requestBody: RequestSignUp) : Call<ResponseSignUp>
}