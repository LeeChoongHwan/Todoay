package com.todoay.api.domain.auth.email

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.todoay.api.domain.auth.email.dto.EmailRequest
import com.todoay.api.domain.auth.email.dto.EmailResponse
import com.todoay.api.config.RetrofitService
import com.todoay.api.util.ErrorResponse
import com.todoay.api.util.Failure
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * 이메일 관련 API 호출 및 응답을 처리하는 클래스.
 * API Interface: EmailService.kt
 */
class EmailAPI {

    private val service = RetrofitService.getService().create(EmailService::class.java)

    /**
     * 이메일 중복확인 수행
     */
    fun checkEmailDuplicate(_email: String, onResponse: (EmailResponse) -> Unit, onErrorResponse: (ErrorResponse) -> Unit ,onFailure: (Failure) -> Unit) {
        val request = EmailRequest(
            email = _email
        )
        service.getCheckEmailDuplicate(request)
            .enqueue(object : Callback<EmailResponse> {
                override fun onResponse(
                    call: Call<EmailResponse>,
                    response: Response<EmailResponse>
                ) {
                    if (response.isSuccessful) {
                        val emailResponse = EmailResponse(
                            status = response.code()
                        )
                        onResponse(emailResponse)
                        Log.d("email", "check email duplicate - success {$emailResponse}")
                    } else {
                        val errorResponse = RetrofitService.getErrorResponse(response)
                        onErrorResponse(errorResponse)
                        Log.d("email", "check email duplicate - failed {${errorResponse}}")
                    }
                }

                @RequiresApi(Build.VERSION_CODES.O)
                override fun onFailure(call: Call<EmailResponse>, t: Throwable) {
                    val failure = RetrofitService.getFailure(
                        t, "/auth/email_duplicate_check"
                    )
                    onFailure(failure)
                    Log.d("email", "system - failed {${failure}}")
                }
            })
    }

    /**
     * 이메일 인증메일 전송 수행
     */
    fun sendCertMail(_email: String, onResponse: (EmailResponse) -> Unit, onErrorResponse: (ErrorResponse) -> Unit ,onFailure: (Failure) -> Unit) {
        val request = EmailRequest(
            email = _email
        )
        service.getSendCertMail(request.email)
            .enqueue(object : Callback<EmailResponse> {
                override fun onResponse(
                    call: Call<EmailResponse>,
                    response: Response<EmailResponse>
                ) {
                    if (response.isSuccessful) {
                        val emailResponse = EmailResponse(
                            status= response.code()
                        )
                        onResponse(emailResponse)
                        Log.d("email", "send cert mail - success {$emailResponse}")
                    } else {
                        val errorResponse = RetrofitService.getValidErrorResponse(response)
                        onErrorResponse(errorResponse)
                        Log.d("email", "send cert mail - failed {$errorResponse}")
                    }
                }

                @RequiresApi(Build.VERSION_CODES.O)
                override fun onFailure(call: Call<EmailResponse>, t: Throwable) {
                    val failure = RetrofitService.getFailure(
                        t, "/auth/mail"
                    )
                    onFailure(failure)
                    Log.d("email", "system - failed {${failure}}")
                }

            })
    }
}