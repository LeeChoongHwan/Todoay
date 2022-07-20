package com.todoay.api.auth.signUp

import android.util.Log
import com.todoay.api.retrofit.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * 유저 회원가입 API 호출 및 응답을 처리하는 클래스.
 * API Interface: SignUpService.kt
 */
class SignUpAPI {

    private val service = RetrofitService.getService().create(SignUpService::class.java)

    /**
     * 유저 회원가입 수행
     */
    fun signUp(_email: String, _password: String, _nickName: String) {
        val requestBody = RequestSignUp(
            email = _email,
            password = _password,
            nickName = _nickName
        )
        service.postSignUp(requestBody)
            .enqueue(object : Callback<ResponseSignUp> {
                override fun onResponse(
                    call: Call<ResponseSignUp>,
                    response: Response<ResponseSignUp>
                ) {
                    if(response.isSuccessful) {
                        val result = response.body()

                        Log.d("sign-up", "$result")
                    }
                }

                override fun onFailure(call: Call<ResponseSignUp>, t: Throwable) {

                    Log.d("sign-up failed", t.message.toString())
                }

            })
    }

}