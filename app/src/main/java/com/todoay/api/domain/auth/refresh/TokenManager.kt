package com.todoay.api.domain.auth.refresh

import com.todoay.MainActivity
import com.todoay.TodoayApplication
import com.todoay.api.domain.auth.refresh.dto.response.RefreshResponse
import com.todoay.api.util.response.error.ErrorResponse
import com.todoay.global.util.Utils.Companion.printLog
import java.net.HttpURLConnection
import java.time.LocalDateTime

class TokenManager {
    companion object {
        private val refreshService : RefreshAPI = RefreshAPI()
        fun refreshToken(refreshToken: String, onResponse: (RefreshResponse) -> Unit, onErrorResponse: (ErrorResponse) -> Unit) {
            printLog("[토큰 요청] Refresh Token 요청")
            refreshService.refreshTokenToAccessToken(
                refreshToken,
                onResponse = {
                    /* Response 토큰 정상 세팅 */
                    if(it.accessToken != "" && it.refreshToken != "") {
                        TodoayApplication.pref.setUser(
                            TodoayApplication.pref.getEmail(),
                            it.accessToken,
                            it.refreshToken
                        )
                        printLog("[토큰 요청] Refresh Token 요청 성공")
                        onResponse(it)
                    }
                    /* Response 토큰 비정상(없음) 처리 */
                    else {
                        printLog("[토큰 요청] RefreshToken 요청 실패")
                        MainActivity.mainAct.logout("다시 로그인 해주세요")
                        onErrorResponse(
                            ErrorResponse(
                                timestamp = LocalDateTime.now().toString(),
                                status = HttpURLConnection.HTTP_BAD_REQUEST,
                                error = "BAD_REQUEST",
                                code = "TOKEN_NOT_FOUND_FROM_SERVER",
                                message = "토큰을 서버로부터 가져오지 못하였습니다.",
                                path = "/auth/refresh"
                            )
                        )
                    }
                },
                onErrorResponse = {
                    /* 400 JWT 에러 & 404 토큰 DB 조회 실패 에러 */
                    printLog("[토큰 만료] Refresh Token 만료")
                    MainActivity.mainAct.logout("다시 로그인 해주세요")
                    onErrorResponse(it)
                }
            )
        }
    }
}