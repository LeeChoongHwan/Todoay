package com.todoay.api.auth.email

import android.util.Log
import com.todoay.api.retrofit.RetrofitService
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
    fun checkEmailDuplicate(_email: String) {
        val requestParams = RequestEmail(
            email = _email
        )
        service.getCheckEmailDuplicate(requestParams)
            .enqueue(object : Callback<ResponseEmail> {
                override fun onResponse(
                    call: Call<ResponseEmail>,
                    response: Response<ResponseEmail>
                ) {
                    if(response.isSuccessful) {
                        val result = response.body()

                        Log.d("email", "check email duplicate - success{$result}")
                    }
                }

                override fun onFailure(call: Call<ResponseEmail>, t: Throwable) {

                    Log.d("email", "check email duplicate - failed{${t.message.toString()}}")
                }

            })
    }

    /**
     * 이메일 인증메일 전송 수행
     */
    fun sendCertMail(_email: String) {
        val requestParams = RequestEmail(
            email = _email
        )
        service.getSendCertMail(requestParams)
            .enqueue(object : Callback<ResponseEmail> {
                override fun onResponse(
                    call: Call<ResponseEmail>,
                    response: Response<ResponseEmail>
                ) {
                    if(response.isSuccessful) {
                        val result = response.body()

                        Log.d("email", "send cert mail - success{$result}")
                    }
                }

                override fun onFailure(call: Call<ResponseEmail>, t: Throwable) {

                    Log.d("email", "send cert mail - failed{${t.message.toString()}")
                }

            })
    }
}