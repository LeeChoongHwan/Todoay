package com.todoay.api.auth.login

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * 로그인 API 호출 인터페이스
 */
interface LoginService {
    @POST("/auth/sign-in")
    fun postLogin(@Body userData: RequestLogin) : Call<ResponseLogin>
}