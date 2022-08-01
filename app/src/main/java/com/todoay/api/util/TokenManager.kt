package com.todoay.api.util

import android.util.Log
import com.todoay.api.domain.auth.refresh.RefreshAPI
import com.todoay.global.util.TodoayApplication

class TokenManager {
    companion object {
        private val refreshService : RefreshAPI = RefreshAPI()
        fun refreshToken(refreshToken : String) {
            refreshService.refreshTokenToAccessToken(
                refreshToken,
                onResponse = {
                    TodoayApplication.pref.setUser(
                        TodoayApplication.pref.getEmail(),
                        it.accessToken,
                        it.refreshToken
                    )
                    Log.d("RefreshToken", "success refresh token")
                },
                onErrorResponse = {
                    // 다시 로그인 하도록? 401?
                },
                onFailure = {
                    // Toast? 어떻게 해야하지?
                }
            )
        }
    }
}