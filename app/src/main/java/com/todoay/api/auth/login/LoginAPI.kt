package com.todoay.api.auth.login

import android.util.Log
import com.todoay.api.retrofit.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * 로그인 관련 API 호출 및 응답을 처리하는 클래스.
 * API Interface: LoginService.kt
 */
class LoginAPI {

    private val service = RetrofitService.getService().create(LoginService::class.java)

    /**
     * 로그인 수행
     */
    fun login(_email: String, _password: String) {
        val requestBody = RequestLogin(
            email = _email,
            password = _password
        )
        service.postLogin(requestBody)
            .enqueue(object : Callback<ResponseLogin> {
                override fun onResponse(
                    call: Call<ResponseLogin>,
                    response: Response<ResponseLogin>
                ) {
                    if(response.isSuccessful) {
                        val result = response.body()

                        Log.d("login", "login - success{$result}")
                    }
                }

                override fun onFailure(call: Call<ResponseLogin>, t: Throwable) {

                    Log.d("login", "login - failed{${t.message.toString()}}")
                }

            })

    }
}