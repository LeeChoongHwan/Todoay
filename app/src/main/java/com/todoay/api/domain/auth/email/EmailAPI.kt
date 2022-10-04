package com.todoay.api.domain.auth.email

import android.os.Build
import androidx.annotation.RequiresApi
import com.todoay.api.config.RetrofitService
import com.todoay.api.config.ServiceRepository.AuthServiceRepository.callEmailService
import com.todoay.api.domain.auth.email.dto.response.CheckEmailVerifiedResponse
import com.todoay.api.domain.auth.email.dto.response.EmailExistsResponse
import com.todoay.api.util.response.error.ErrorResponse
import com.todoay.api.util.response.error.FailureResponse
import com.todoay.api.util.response.error.ValidErrorResponse
import com.todoay.api.util.response.success.SuccessResponse
import com.todoay.global.util.PrintUtil.printLog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * 이메일 관련 API 호출 및 응답을 처리하는 클래스.
 *
 * @see EmailService
 */
class EmailAPI {

    companion object {
        private var instance : EmailAPI? = null
        fun getInstance() : EmailAPI {
            return instance ?: synchronized(this) {
                instance ?: EmailAPI().also {
                    instance  = it
                }
            }
        }
    }

    /**
     * 이메일 중복검사 수행
     *
     * @param email 중복검사할 이메일 String 변수
     * @param onResponse
     * @param onErrorResponse
     * @param onFailure
     *
     * @see EmailService.getCheckEmailDuplicate
     */
    fun checkEmailExists(email: String, onResponse: (EmailExistsResponse) -> Unit, onErrorResponse: (ErrorResponse) -> Unit, onFailure: (FailureResponse) -> Unit) {
        callEmailService().getCheckEmailDuplicate(email)
            .enqueue(object : Callback<EmailExistsResponse> {
                override fun onResponse(
                    call: Call<EmailExistsResponse>,
                    response: Response<EmailExistsResponse>
                ) {
                    if (response.isSuccessful) {
                        val emailExistsResponse : EmailExistsResponse = response.body()!!
                        onResponse(emailExistsResponse)
                        printLog("[이메일 중복] - 성공 {$emailExistsResponse}")
                    } else {
                        val errorResponse = RetrofitService.getErrorResponse(response)
                        onErrorResponse(errorResponse)
                        printLog("[이메일 중복] - 실패 {${errorResponse}}")
                    }
                }

                @RequiresApi(Build.VERSION_CODES.O)
                override fun onFailure(call: Call<EmailExistsResponse>, t: Throwable) {
                    val failure = RetrofitService.getFailure(
                        t, "/auth/email-exists"
                    )
                    onFailure(failure)
                    printLog("[SYSTEM ERROR] - 실패 {${failure}}")
                }
            })
    }

    /**
     *
     */
    /**
     * 이메일 인증메일 전송 수행
     *
     * @param email 인증메일 전송할 이메일 String 변수
     * @param onResponse
     * @param onErrorResponse
     * @param onFailure
     *
     * @see EmailService.getSendCertMail
     */
    fun sendCertMail(email: String, onResponse: (SuccessResponse) -> Unit, onErrorResponse: (ValidErrorResponse) -> Unit, onFailure: (FailureResponse) -> Unit) {
        callEmailService().getSendCertMail(email)
            .enqueue(object : Callback<SuccessResponse> {
                override fun onResponse(
                    call: Call<SuccessResponse>,
                    response: Response<SuccessResponse>
                ) {
                    if (response.isSuccessful) {
                        val successResponse  = SuccessResponse(
                            status = response.code()
                        )
                        onResponse(successResponse)
                        printLog("[인증 메일 전송] - 성공 {$successResponse}")
                    } else {
                        val validErrorResponse = RetrofitService.getValidErrorResponse(response)
                        onErrorResponse(validErrorResponse)
                        printLog("[인증 메일 전송] - 실패 {$validErrorResponse}")
                    }
                }

                @RequiresApi(Build.VERSION_CODES.O)
                override fun onFailure(call: Call<SuccessResponse>, t: Throwable) {
                    val failure = RetrofitService.getFailure(
                        t, "/auth/send-mail"
                    )
                    onFailure(failure)
                    printLog("[SYSTEM ERROR] - 실패 {${failure}}")
                }

            })
    }

    /**
     * 이메일 인증 여부 확인 수행
     *
     * @param email 인증 여부 확인할 이메일 String 변수
     * @param onResponse
     * @param onErrorResponse
     * @param onFailure
     *
     * @see EmailService.getCheckEmailVerified
     */
    fun checkEmailVerified(email: String, onResponse: (CheckEmailVerifiedResponse) -> Unit, onErrorResponse: (ErrorResponse) -> Unit, onFailure: (FailureResponse) -> Unit) {
        callEmailService().getCheckEmailVerified(email)
            .enqueue(object : Callback<CheckEmailVerifiedResponse> {
                override fun onResponse(
                    call: Call<CheckEmailVerifiedResponse>,
                    response: Response<CheckEmailVerifiedResponse>
                ) {
                    if(response.isSuccessful) {
                        val checkEmailVerifiedResponse : CheckEmailVerifiedResponse = response.body()!!
                        onResponse(checkEmailVerifiedResponse)
                        printLog("[이메일 인증 여부 확인] - 성공 {$checkEmailVerifiedResponse}")
                    }
                    else {
                        try {
                            val errorResponse = RetrofitService.getErrorResponse(response)
                            onErrorResponse(errorResponse)
                            printLog("[이메일 인증 여부 확인] - 실패 {$errorResponse}")
                        } catch (t : Throwable) {
                            onFailure(call, t)
                        }
                    }
                }

                @RequiresApi(Build.VERSION_CODES.O)
                override fun onFailure(call: Call<CheckEmailVerifiedResponse>, t: Throwable) {
                    val failure = RetrofitService.getFailure(
                        t, "/auth/{email}/email-verified"
                    )
                    onFailure(failure)
                    printLog("[SYSTEM ERROR] - 실패 {${failure}}")
                }

            })
    }

    /**
     * 임시 비밀번호 메일 전송 요청 수행
     *
     * @param email 임시 비밀번호를 전송할 이메일 String 변수
     * @param onResponse
     * @param onErrorResponse
     * @param onFailure
     *
     * @see EmailService.getSendMailForUpdatePassword
     */
    fun sendMailForUpdatePassword(email: String, onResponse: (SuccessResponse) -> Unit, onErrorResponse: (ErrorResponse) -> Unit, onFailure: (FailureResponse) -> Unit) {
        callEmailService().getSendMailForUpdatePassword(email)
            .enqueue(object : Callback<SuccessResponse> {
                override fun onResponse(
                    call: Call<SuccessResponse>,
                    response: Response<SuccessResponse>
                ) {
                    if(response.isSuccessful) {
                        val successResponse = SuccessResponse(
                            status = response.code()
                        )
                        onResponse(successResponse)
                        printLog("[임시 비밀번호 메일 전송 요청] - 성공 {$successResponse}")
                    }
                    else {
                        val errorResponse = RetrofitService.getErrorResponse(response)
                        onErrorResponse(errorResponse)
                        printLog("[임시 비밀번호 메일 전송 요청] - 실패 {$errorResponse}")
                    }
                }

                override fun onFailure(call: Call<SuccessResponse>, t: Throwable) {
                    val failure = RetrofitService.getFailure(
                        t, "/auth/send-mail/update-password"
                    )
                    onFailure(failure)
                    printLog("[SYSTEM ERROR] - 실패 {${failure}}")
                }

            })
    }

}