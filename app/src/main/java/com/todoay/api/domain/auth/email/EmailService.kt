package com.todoay.api.domain.auth.email

import com.todoay.api.domain.auth.email.dto.response.CheckEmailVerifiedResponse
import com.todoay.api.domain.auth.email.dto.response.EmailExistsResponse
import com.todoay.api.domain.auth.email.dto.response.SendCertMailResponse
import com.todoay.api.domain.auth.email.dto.response.SendMailUpdatePasswordResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * 이메일 관련 API 호출 인터페이스
 */
interface EmailService {

    /**
     * 이메일 중복검사
     */
    @GET("/auth/email-exists")
    fun getCheckEmailDuplicate(@Query("email") email: String) : Call<EmailExistsResponse>

    /**
     * 이메일 인증메일 전송 요청
     */
    @GET("/auth/send-mail")
    fun getSendCertMail(@Query("email") email: String) : Call<SendCertMailResponse>

    /**
     * 이메일 인증여부 확인
     */
    @GET("/auth/{email}/email-verified")
    fun getCheckEmailVerified(@Path("email") email:String) : Call<CheckEmailVerifiedResponse>

    /**
     * 임시 비밀번호 메일 전송 요청
     */
    @GET("/auth/send-mail/update-password")
    fun getSendMailForUpdatePassword(@Query("email") email: String) : Call<SendMailUpdatePasswordResponse>



}