package com.todoay.api.domain.auth.email

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.todoay.api.config.RetrofitService
import com.todoay.api.config.ServiceRepository.AuthServiceRepository.emailService
import com.todoay.api.domain.auth.email.dto.response.CheckEmailVerifiedResponse
import com.todoay.api.domain.auth.email.dto.response.EmailExistsResponse
import com.todoay.api.domain.auth.email.dto.response.SendCertMailResponse
import com.todoay.api.util.response.error.ErrorResponse
import com.todoay.api.util.response.error.FailureResponse
import com.todoay.api.util.response.error.ValidErrorResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * 이메일 관련 API 호출 및 응답을 처리하는 클래스.
 * API Interface: EmailService.kt
 */
class EmailAPI {

    /**
     * 이메일 중복확인 수행
     * [GET]("/auth/email-exists")
     */
    fun checkEmailExists(email: String, onResponse: (EmailExistsResponse) -> Unit, onErrorResponse: (ErrorResponse) -> Unit, onFailure: (FailureResponse) -> Unit) {
        emailService.getCheckEmailDuplicate(email)
            .enqueue(object : Callback<EmailExistsResponse> {
                override fun onResponse(
                    call: Call<EmailExistsResponse>,
                    response: Response<EmailExistsResponse>
                ) {
                    if (response.isSuccessful) {
                        val emailExistsResponse : EmailExistsResponse = response.body()!!
                        onResponse(emailExistsResponse)
                        Log.d("email", "check email exists - success {$emailExistsResponse}")
                    } else {
                        val errorResponse = RetrofitService.getErrorResponse(response)
                        onErrorResponse(errorResponse)
                        Log.d("email", "check email exists - failed {${errorResponse}}")
                    }
                }

                @RequiresApi(Build.VERSION_CODES.O)
                override fun onFailure(call: Call<EmailExistsResponse>, t: Throwable) {
                    val failure = RetrofitService.getFailure(
                        t, "/auth/email-exists"
                    )
                    onFailure(failure)
                    Log.d("email", "system - failed {${failure}}")
                }
            })
    }

    /**
     * 이메일 인증메일 전송 수행
     * [GET]("/auth/send-mail")
     */
    fun sendCertMail(email: String, onResponse: (SendCertMailResponse) -> Unit, onErrorResponse: (ValidErrorResponse) -> Unit, onFailure: (FailureResponse) -> Unit) {
        emailService.getSendCertMail(email)
            .enqueue(object : Callback<SendCertMailResponse> {
                override fun onResponse(
                    call: Call<SendCertMailResponse>,
                    response: Response<SendCertMailResponse>
                ) {
                    if (response.isSuccessful) {
                        val sendCertMailResponse  = SendCertMailResponse(
                            status = response.code()
                        )
                        onResponse(sendCertMailResponse)
                        Log.d("email", "send cert mail - success {$sendCertMailResponse}")
                    } else {
                        val validErrorResponse = RetrofitService.getValidErrorResponse(response)
                        onErrorResponse(validErrorResponse)
                        Log.d("email", "send cert mail - failed {$validErrorResponse}")
                    }
                }

                @RequiresApi(Build.VERSION_CODES.O)
                override fun onFailure(call: Call<SendCertMailResponse>, t: Throwable) {
                    val failure = RetrofitService.getFailure(
                        t, "/auth/send-mail"
                    )
                    onFailure(failure)
                    Log.d("email", "system - failed {${failure}}")
                }

            })
    }

    /**
     * 이메일 인증 여부 확인 수행
     * [GET]("/auth/{email}/email-verified")
     */
    fun checkEmailVerified(email: String, onResponse: (CheckEmailVerifiedResponse) -> Unit, onErrorResponse: (ErrorResponse) -> Unit, onFailure: (FailureResponse) -> Unit) {
        emailService.getCheckEmailVerified(email)
            .enqueue(object : Callback<CheckEmailVerifiedResponse> {
                override fun onResponse(
                    call: Call<CheckEmailVerifiedResponse>,
                    response: Response<CheckEmailVerifiedResponse>
                ) {
                    if(response.isSuccessful) {
                        val checkEmailVerifiedResponse : CheckEmailVerifiedResponse = response.body()!!
                        onResponse(checkEmailVerifiedResponse)
                        Log.d("email", "check email verified - success {$checkEmailVerifiedResponse}")
                    }
                    else {
                        val errorResponse = RetrofitService.getErrorResponse(response)
                        onErrorResponse(errorResponse)
                        Log.d("email", "check email verified - failed {$errorResponse}")
                    }
                }

                @RequiresApi(Build.VERSION_CODES.O)
                override fun onFailure(call: Call<CheckEmailVerifiedResponse>, t: Throwable) {
                    val failure = RetrofitService.getFailure(
                        t, "/auth/{email}/email-verified"
                    )
                    onFailure(failure)
                    Log.d("email", "system - failed {${failure}}")
                }

            })
    }
}