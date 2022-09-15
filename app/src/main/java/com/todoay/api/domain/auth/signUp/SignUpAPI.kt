package com.todoay.api.domain.auth.signUp

import android.os.Build
import androidx.annotation.RequiresApi
import com.todoay.api.config.RetrofitService
import com.todoay.api.config.ServiceRepository.AuthServiceRepository.callSignUpService
import com.todoay.api.domain.auth.signUp.dto.request.SignUpRequest
import com.todoay.api.util.response.error.FailureResponse
import com.todoay.api.util.response.error.ValidErrorResponse
import com.todoay.api.util.response.success.SuccessResponse
import com.todoay.global.util.PrintUtil.printLog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * 유저 회원가입 API 호출 및 응답을 처리하는 클래스.
 *
 * @see SignUpService
 */
class SignUpAPI {

    companion object {
        private var instance : SignUpAPI? = null
        fun getInstance() : SignUpAPI {
            return instance ?: synchronized(this) {
                instance ?: SignUpAPI().also {
                    instance  = it
                }
            }
        }
    }
    
    /**
     * 유저 회원가입 수행
     * @see SignUpService.postSignUp
     */
    fun signUp(request: SignUpRequest, onResponse: (SuccessResponse) -> Unit, onErrorResponse: (ValidErrorResponse) -> Unit, onFailure: (FailureResponse) -> Unit) {
        callSignUpService().postSignUp(request)
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
                        printLog("[회원가입 요청] - 성공 {$successResponse}")
                    }
                    else {
                        val validErrorResponse = RetrofitService.getValidErrorResponse(response)
                        onErrorResponse(validErrorResponse)
                        printLog("[회원가입 요청] - 실패 {$validErrorResponse}")
                    }
                }

                @RequiresApi(Build.VERSION_CODES.O)
                override fun onFailure(call: Call<SuccessResponse>, t: Throwable) {
                    val failure = RetrofitService.getFailure(
                        t, "/auth/sign-up"
                    )
                    onFailure(failure)
                    printLog("[SYSTEM ERROR] - 실패 {${failure}}")
                }

            })
    }

}