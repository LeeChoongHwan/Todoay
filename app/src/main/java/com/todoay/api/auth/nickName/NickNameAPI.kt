package com.todoay.api.auth.nickName

import android.util.Log
import com.todoay.api.retrofit.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * 유저 닉네임 관련 API 호출 및 응답을 처리하는 클래스.
 * API Interface: NickNameService.kt
 */
class NickNameAPI {

    private val service = RetrofitService.getService().create(NickNameService::class.java)

    /**
     * 유저 닉네임 중복확인 수행
     */
    fun checkNickNameDuplicate(_nickName: String) {
        val requestBody = RequestNickName(
            nickName = _nickName
        )
        service.postCheckNickNameDuplicate(requestBody)
            .enqueue(object : Callback<ResponseNickName> {
                override fun onResponse(
                    call: Call<ResponseNickName>,
                    response: Response<ResponseNickName>
                ) {
                    if(response.isSuccessful) {
                        val result = response.body()

                        Log.d("nickname", "check nickname duplicate - success{$result}")
                    }
                }

                override fun onFailure(call: Call<ResponseNickName>, t: Throwable) {

                    Log.d("nickname", "check nickname duplicate - failed{${t.message.toString()}")
                }

            })
    }
}