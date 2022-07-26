package com.todoay.api.domain.auth.login

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.todoay.api.domain.auth.login.dto.LoginRequest
import com.todoay.api.domain.auth.login.dto.LoginResponse
import com.todoay.api.util.error.ErrorResponse
import com.todoay.api.config.RetrofitService
import com.todoay.api.util.error.Failure
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response as Response

/**
 * 로그인 관련 API 호출 및 응답을 처리하는 클래스.
 * API Interface: LoginService.kt
 */
class LoginAPI {

    private val service: LoginService = RetrofitService.getService().create(LoginService::class.java)

    /**
     * 로그인 수행
     */
    fun login(_email: String, _password: String, onResponse: (LoginResponse) -> Unit, onErrorResponse: (ErrorResponse) -> Unit, onFailure: (Failure) -> Unit) {
        val request = LoginRequest(
            email = _email,
            password = _password
        )
        service.postLogin(request)
            .enqueue(object : Callback<LoginResponse> {
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    // 로그인 성공
                    if(response.isSuccessful) {
                        val loginResponse : LoginResponse = response.body()!!
                        onResponse(loginResponse)
                        Log.d("login", "login - success {${loginResponse}}")
                    }
                    // 로그인 실패
                    else {
                        val errorResponse = RetrofitService.getErrorResponse(response)
                        onErrorResponse(errorResponse)
                        Log.d("login", "login - failed {${errorResponse}}")
                    }
                }

                @RequiresApi(Build.VERSION_CODES.O)
                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    val failure = RetrofitService.getFailure(
                        t,  "/auth/sign-in"
                    )
                    onFailure(failure)
                    Log.d("login", "system - failed {${failure}}")
                }
            })
    }
}