package com.todoay.api.domain.auth.email

import com.todoay.api.domain.auth.email.dto.response.CheckEmailVerifiedResponse
import com.todoay.api.domain.auth.email.dto.response.EmailExistsResponse
import com.todoay.api.domain.auth.email.dto.response.SendCertMailResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * 이메일 관련 API 호출 인터페이스
 */
interface EmailService {

    @GET("/auth/email-exists")
    fun getCheckEmailDuplicate(@Query("email") email: String) : Call<EmailExistsResponse>

    @GET("/auth/send-mail")
    fun getSendCertMail(@Query("email") email: String) : Call<SendCertMailResponse>

    @GET("/auth/{email}/email-verified")
    fun getCheckEmailVerified(@Path("email") email:String) : Call<CheckEmailVerifiedResponse>

}