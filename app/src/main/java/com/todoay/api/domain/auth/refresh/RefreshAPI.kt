package com.todoay.api.domain.auth.refresh

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.todoay.api.config.RetrofitService
import com.todoay.api.config.ServiceRepository.AuthServiceRepository.callRefreshService
import com.todoay.api.domain.auth.refresh.dto.request.RefreshRequest
import com.todoay.api.domain.auth.refresh.dto.response.RefreshResponse
import com.todoay.api.util.response.error.ErrorResponse
import com.todoay.api.util.response.error.FailureResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * RefreshToken 관련 API 호출 및 응답을 처리하는 클래스.
 * API Interface: callRefreshService().kt
 */
class RefreshAPI {

    /**
     * 토큰 재발행 수행
     * [POST]("/auth/refresh")
     */
    fun refreshTokenToAccessToken(refreshToken: String, onResponse: (RefreshResponse) -> Unit, onErrorResponse: (ErrorResponse) -> Unit, onFailure: (FailureResponse) -> Unit) {
        val request = RefreshRequest(
            refreshToken = refreshToken
        )
        callRefreshService().postRefreshToken(request)
            .enqueue(object : Callback<RefreshResponse> {
                override fun onResponse(
                    call: Call<RefreshResponse>,
                    response: Response<RefreshResponse>
                ) {
                    if(response.isSuccessful) {
                        val refreshResponse : RefreshResponse = response.body()!!
                        onResponse(refreshResponse)
                        Log.d("refresh", "refresh - success {$refreshResponse}")
                    }
                    else {
                        val errorResponse = RetrofitService.getErrorResponse(response)
                        onErrorResponse(errorResponse)
                        Log.d("refresh", "refresh - failed {$errorResponse}")
                    }
                }

                @RequiresApi(Build.VERSION_CODES.O)
                override fun onFailure(call: Call<RefreshResponse>, t: Throwable) {
                    val failure = RetrofitService.getFailure(
                        t, "/auth/refresh"
                    )
                    onFailure(failure)
                    Log.d("refresh", "system - failed {$failure}")
                }

            })
    }

}