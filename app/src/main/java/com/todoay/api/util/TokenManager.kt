package com.todoay.api.util

import android.util.Log
import com.todoay.api.domain.auth.refresh.RefreshAPI
import com.todoay.global.util.TodoayApplication
import okhttp3.Request
import okhttp3.Response

class TokenManager {
    companion object {
        private val refreshService : RefreshAPI = RefreshAPI()
        fun refreshToken(refreshToken: String) {
            Log.d("Token", "[토큰 요청] Refresh Token 요청")
            refreshService.refreshTokenToAccessToken(
                refreshToken,
                onResponse = {
                    TodoayApplication.pref.setUser(
                        TodoayApplication.pref.getEmail(),
                        it.accessToken,
                        it.refreshToken
                    )
                    Log.d("Token", "[토큰 요청] Refresh Token 요청 성공")
                },
                onErrorResponse = {
                    // 401 JWT 에러 -> 다시 로그인 하도록?
                    // 404 토큰 DB 조회 실패 에러 -> 다시 로그인 하도록?
                }
            )
        }
    }
}