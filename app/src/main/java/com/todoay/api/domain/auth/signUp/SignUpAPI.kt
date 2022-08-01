package com.todoay.api.domain.auth.signUp

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.todoay.api.config.RetrofitService
import com.todoay.api.config.ServiceRepository.AuthServiceRepository.callSignUpService
import com.todoay.api.domain.auth.signUp.dto.request.SignUpRequest
import com.todoay.api.domain.auth.signUp.dto.response.SignUpResponse
import com.todoay.api.util.response.error.FailureResponse
import com.todoay.api.util.response.error.ValidErrorResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * 유저 회원가입 API 호출 및 응답을 처리하는 클래스.
 * API Interface: SignUpService.kt
 */
class SignUpAPI {
    
    val TAG = "SIGNUP API"

    /**
     * 유저 회원가입 수행
     * [POST]("/auth/sign-up")
     */
    fun signUp(_email: String, _password: String, _nickname: String, onResponse: (SignUpResponse) -> Unit, onErrorResponse: (ValidErrorResponse) -> Unit, onFailure: (FailureResponse) -> Unit) {
        val request = SignUpRequest(
            email = _email,
            password = _password,
            nickname = _nickname
        )
        callSignUpService().postSignUp(request)
            .enqueue(object : Callback<SignUpResponse> {
                override fun onResponse(
                    call: Call<SignUpResponse>,
                    response: Response<SignUpResponse>
                ) {
                    if(response.isSuccessful) {
                        val signUpResponse = SignUpResponse(
                            status = response.code()
                        )
                        onResponse(signUpResponse)
                        Log.d(TAG, "[회원가입 요청] - 성공 {$signUpResponse}")
                    }
                    else {
                        val validErrorResponse = RetrofitService.getValidErrorResponse(response)
                        onErrorResponse(validErrorResponse)
                        Log.d(TAG, "[회원가입 요청] - 실패 {$validErrorResponse}")
                    }
                }

                @RequiresApi(Build.VERSION_CODES.O)
                override fun onFailure(call: Call<SignUpResponse>, t: Throwable) {
                    val failure = RetrofitService.getFailure(
                        t, "/auth/sign-up"
                    )
                    onFailure(failure)
                    Log.d(TAG, "[SYSTEM ERROR] - 실패 {${failure}}")
                }

            })
    }

}