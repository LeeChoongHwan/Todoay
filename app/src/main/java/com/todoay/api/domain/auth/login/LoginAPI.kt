package com.todoay.api.domain.auth.login

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.todoay.api.domain.auth.login.dto.LoginRequest
import com.todoay.api.domain.auth.login.dto.LoginResponse
import com.todoay.api.util.ErrorResponse
import com.todoay.api.retrofit.RetrofitService
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
     * onResponse: 통신에 성공한 경우
     * onFailure: 통신에 실패한 경우
     */
    fun login(_email: String, _password: String, onResponse: (LoginResponse) -> Unit, onFailure: (ErrorResponse) -> Unit) {
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
                        onFailure(errorResponse)
                        Log.d("login", "login - failed {${errorResponse}}")
                    }
                }

                @RequiresApi(Build.VERSION_CODES.O)
                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    val errorFailure = RetrofitService.getErrorFailure(
                        t, "로그인"
                    )
                    onFailure(errorFailure)
                    Log.d("login", "system - failed {${errorFailure}}")
                }
            })
    }
}