package com.todoay.api.auth.password

import android.util.Log
import com.todoay.api.retrofit.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * 유저 비밀번호 관련 API 호출 및 응답을 처리하는 클래스.
 * API Interface: ModifyPasswordService.kt
 */
class ModifyPasswordAPI {

    private val service = RetrofitService.getService().create(ModifyPasswordService::class.java)

    /**
     * 유저 비밀번호 변경 수행
     */
    fun modifyPassword(_currentPassword: String, _modifyPassword: String) {
        val requestBody = RequestModifyPassword(
            currentPassword = _currentPassword,
            modifyPassword = _modifyPassword
        )
        service.patchModifyPassword(requestBody)
            .enqueue(object: Callback<ResponseModifyPassword> {
                override fun onResponse(
                    call: Call<ResponseModifyPassword>,
                    response: Response<ResponseModifyPassword>
                ) {
                    if(response.isSuccessful) {
                        val result = response.body()

                        Log.d("modify password", "success{$result}")
                    }
                }

                override fun onFailure(call: Call<ResponseModifyPassword>, t: Throwable) {

                    Log.d("modify password", "failed{${t.message.toString()}")
                }

            })
    }

}