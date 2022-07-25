package com.todoay.api.domain.auth.email

import com.todoay.api.domain.auth.email.dto.EmailRequest
import com.todoay.api.domain.auth.email.dto.EmailResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * 이메일 관련 API 호출 인터페이스
 */
interface EmailService {

    @GET("/auth/email_duplicate_check")
    fun getCheckEmailDuplicate(@Query("email") email: EmailRequest) : Call<EmailResponse>

    @GET("/auth/mail")
    fun getSendCertMail(@Query("email") email: String) : Call<EmailResponse>

}