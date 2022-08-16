package com.todoay.api.util

import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.todoay.MainActivity
import com.todoay.api.domain.auth.refresh.RefreshAPI
import com.todoay.global.util.TodoayApplication
import kotlin.concurrent.thread

class TokenManager {
    companion object {
        private val refreshService : RefreshAPI = RefreshAPI()
        fun refreshToken(refreshToken: String) : Boolean {
            Log.d("Token", "[토큰 요청] Refresh Token 요청")
            return refreshService.refreshTokenToAccessToken(
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
                    // 400 JWT 에러 -> 다시 로그인 하도록?
                    // 404 토큰 DB 조회 실패 에러 -> 다시 로그인 하도록?
                    val handler = Handler(Looper.getMainLooper())
                    handler.postDelayed({
                        val act = MainActivity()
                        val intent = Intent(act.applicationContext, MainActivity::class.java)
                        act.startActivity(intent)
                    }, 0)
                }
            )
        }
    }
}