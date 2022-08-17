package com.todoay.api.domain.auth.nickname

import android.os.Build
import androidx.annotation.RequiresApi
import com.todoay.api.config.RetrofitService
import com.todoay.api.config.ServiceRepository.AuthServiceRepository.callNicknameService
import com.todoay.api.domain.auth.nickname.dto.response.NicknameResponse
import com.todoay.api.util.response.error.FailureResponse
import com.todoay.api.util.response.error.ValidErrorResponse
import com.todoay.global.util.Utils.Companion.printLog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * 유저 닉네임 관련 API 호출 및 응답을 처리하는 클래스.
 * API Interface: callNicknameService().kt
 */
class NicknameAPI {

    /**
     * 유저 닉네임 중복확인 수행
     * [GET]("/auth/nickname-exists")
     */
    fun checkNicknameExists(nickname: String, onResponse: (NicknameResponse) -> Unit, onErrorResponse: (ValidErrorResponse) -> Unit, onFailure: (FailureResponse) -> Unit) {
        callNicknameService().getNicknameExists(nickname)
            .enqueue(object : Callback<NicknameResponse> {
                override fun onResponse(
                    call: Call<NicknameResponse>,
                    response: Response<NicknameResponse>
                ) {
                    if(response.isSuccessful) {
                        val nickNameResponse : NicknameResponse = response.body()!!
                        onResponse(nickNameResponse)
                        printLog("[닉네임 중복확인] - 성공 {$nickNameResponse}")
                    }
                    else {
                        val errorResponse = RetrofitService.getValidErrorResponse(response)
                        onErrorResponse(errorResponse)
                        printLog("[닉네임 중복확인] - 실패 {$errorResponse}")
                    }
                }

                @RequiresApi(Build.VERSION_CODES.O)
                override fun onFailure(call: Call<NicknameResponse>, t: Throwable) {
                    val failure = RetrofitService.getFailure(
                        t, "/auth/nickname-exists"
                    )
                    onFailure(failure)
                    printLog("[SYSTEM ERROR] - 실패 {${failure}}")
                }

            })
    }
}