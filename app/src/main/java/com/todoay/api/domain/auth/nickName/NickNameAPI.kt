package com.todoay.api.domain.auth.nickName

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.todoay.api.domain.auth.nickName.dto.NickNameRequest
import com.todoay.api.domain.auth.nickName.dto.NickNameResponse
import com.todoay.api.util.ErrorResponse
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
    fun checkNickNameDuplicate(_nickName: String, onResponse: (NickNameResponse) -> Unit, onFailure: (ErrorResponse) -> Unit) {
        val request = NickNameRequest(
            nickName = _nickName
        )
        service.postCheckNickNameDuplicate(request)
            .enqueue(object : Callback<NickNameResponse> {
                override fun onResponse(
                    call: Call<NickNameResponse>,
                    response: Response<NickNameResponse>
                ) {
                    if(response.isSuccessful) {
                        val nickNameResponse : NickNameResponse = response.body()!!
                        onResponse(nickNameResponse)
                        Log.d("nickname", "check nickname duplicate - success {$nickNameResponse}")
                    }
                    else {
                        val errorResponse = RetrofitService.getErrorResponse(response)
                        onFailure(errorResponse)
                        Log.d("nickname", "check nickname duplicate - failed {$errorResponse}")
                    }
                }

                @RequiresApi(Build.VERSION_CODES.O)
                override fun onFailure(call: Call<NickNameResponse>, t: Throwable) {
                    val errorFailure = RetrofitService.getErrorFailure(
                        t, "닉네임 중복확인"
                    )
                    onFailure(errorFailure)
                    Log.d("nickname", "check nickname duplicate - failed{${errorFailure}}")
                }

            })
    }
}