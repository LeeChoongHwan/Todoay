package com.todoay.api.domain.auth.login

import android.os.Build
import androidx.annotation.RequiresApi
import com.todoay.api.config.RetrofitService
import com.todoay.api.config.ServiceRepository.AuthServiceRepository.callLoginService
import com.todoay.api.domain.auth.login.dto.request.LoginRequest
import com.todoay.api.domain.auth.login.dto.response.LoginResponse
import com.todoay.api.util.response.error.ErrorResponse
import com.todoay.api.util.response.error.FailureResponse
import com.todoay.global.util.Utils.Companion.printLog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * 로그인 관련 API 호출 및 응답을 처리하는 클래스.
 * API Interface: callLoginService().kt
 */
class LoginAPI {
    
    /**
     * 로그인 수행
     * [POST]("/auth/login")
     */
    fun login(_email: String, _password: String, onResponse: (LoginResponse) -> Unit, onErrorResponse: (ErrorResponse) -> Unit, onFailure: (FailureResponse) -> Unit) {
        val request = LoginRequest(
            email = _email,
            password = _password
        )
        callLoginService().postLogin(request)
            .enqueue(object : Callback<LoginResponse> {
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    // 로그인 성공
                    if(response.isSuccessful) {
                        val loginResponse : LoginResponse = response.body()!!
                        onResponse(loginResponse)
                        printLog("[로그인] - 성공 {${loginResponse}}")
                    }
                    // 로그인 실패
                    else {
                        val errorResponse = RetrofitService.getErrorResponse(response)
                        onErrorResponse(errorResponse)
                        printLog("[로그인] - 실패 {${errorResponse}}")
                    }
                }

                @RequiresApi(Build.VERSION_CODES.O)
                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    val failure = RetrofitService.getFailure(
                        t,  "/auth/login"
                    )
                    onFailure(failure)
                    printLog("[SYSTEM ERROR] - 실패 {${failure}}")
                }
            })
    }
}