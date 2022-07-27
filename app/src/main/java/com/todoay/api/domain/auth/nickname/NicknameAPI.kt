package com.todoay.api.domain.auth.nickname

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.todoay.api.config.RetrofitService
import com.todoay.api.config.ServiceRepository.AuthServiceRepository.nicknameService
import com.todoay.api.domain.auth.nickname.dto.response.NicknameResponse
import com.todoay.api.util.response.error.ErrorResponse
import com.todoay.api.util.response.error.FailureResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * 유저 닉네임 관련 API 호출 및 응답을 처리하는 클래스.
 * API Interface: NickNameService.kt
 */
class NicknameAPI {

    /**
     * 유저 닉네임 중복확인 수행
     */
    fun checkNicknameExists(nickname: String, onResponse: (NicknameResponse) -> Unit, onErrorResponse: (ErrorResponse) -> Unit, onFailure: (FailureResponse) -> Unit) {
        nicknameService.getNicknameExists(nickname)
            .enqueue(object : Callback<NicknameResponse> {
                override fun onResponse(
                    call: Call<NicknameResponse>,
                    response: Response<NicknameResponse>
                ) {
                    if(response.isSuccessful) {
                        val nickNameResponse : NicknameResponse = response.body()!!
                        onResponse(nickNameResponse)
                        Log.d("nickname", "check nickname exists - success {$nickNameResponse}")
                    }
                    else {
                        val errorResponse = RetrofitService.getErrorResponse(response)
                        onErrorResponse(errorResponse)
                        Log.d("nickname", "check nickname exists - failed {$errorResponse}")
                    }
                }

                @RequiresApi(Build.VERSION_CODES.O)
                override fun onFailure(call: Call<NicknameResponse>, t: Throwable) {
                    val failure = RetrofitService.getFailure(
                        t, "/auth/nickname-exists"
                    )
                    onFailure(failure)
                    Log.d("nickname", "system - failed {${failure}}")
                }

            })
    }
}