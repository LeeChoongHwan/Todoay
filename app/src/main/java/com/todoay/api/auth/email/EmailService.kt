package com.todoay.api.auth.email

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * 이메일 관련 API 호출 인터페이스
 */
interface EmailService {

    @GET("/auth/email_duplicate_check/{email}")
    fun getCheckEmailDuplicate(@Path("email") email: RequestEmail) : Call<ResponseEmail>

    @GET("/auth/email/{email}")
    fun getSendCertMail(@Path("email") email: RequestEmail) : Call<ResponseEmail>

}