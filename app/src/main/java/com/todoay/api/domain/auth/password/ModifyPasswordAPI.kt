package com.todoay.api.domain.auth.password

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.todoay.api.domain.auth.password.dto.ModifyPasswordRequest
import com.todoay.api.domain.auth.password.dto.ModifyPasswordResponse
import com.todoay.api.util.ErrorResponse
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
    fun modifyPassword(_currentPassword: String, _modifyPassword: String, onResponse: (ModifyPasswordResponse) -> Unit, onFailure: (ErrorResponse) -> Unit) {
        val request = ModifyPasswordRequest(
            currentPassword = _currentPassword,
            modifyPassword = _modifyPassword
        )
        service.patchModifyPassword(request)
            .enqueue(object: Callback<ModifyPasswordResponse> {
                override fun onResponse(
                    call: Call<ModifyPasswordResponse>,
                    response: Response<ModifyPasswordResponse>
                ) {
                    if(response.isSuccessful) {
                        val modifyPasswordResponse : ModifyPasswordResponse = response.body()!!
                        onResponse(modifyPasswordResponse)
                        Log.d("modify password", "success {$modifyPasswordResponse}")
                    }
                    else {
                        val errorResponse = RetrofitService.getErrorResponse(response)
                        onFailure(errorResponse)
                        Log.d("modify password", "failed {$errorResponse}")
                    }
                }

                @RequiresApi(Build.VERSION_CODES.O)
                override fun onFailure(call: Call<ModifyPasswordResponse>, t: Throwable) {
                    val errorFailure = RetrofitService.getErrorFailure(
                        t, "비밀번호 변경"
                    )
                    onFailure(errorFailure)
                    Log.d("modify password", "system - failed{${errorFailure}}")
                }

            })
    }

}