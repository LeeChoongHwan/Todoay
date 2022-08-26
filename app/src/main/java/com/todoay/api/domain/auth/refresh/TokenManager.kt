package com.todoay.api.domain.auth.refresh

import com.todoay.MainActivity
import com.todoay.TodoayApplication
import com.todoay.api.domain.auth.refresh.dto.request.RefreshRequest
import com.todoay.api.domain.auth.refresh.dto.response.RefreshResponse
import com.todoay.api.util.response.error.ErrorResponse
import com.todoay.global.util.Utils.Companion.printLog
import java.net.HttpURLConnection
import java.time.LocalDateTime

/**
 * TokenManager 클래스는 AccessToken 만료 시 RefreshToken을 이용하여 Token 재발급을 하도록 하는 클래스이다.
 * 유효한 Refresh Token을 이용한 정상적인 Token 재발급 시
 * 서버로부터 받아온 AccessToken, RefreshToken을 디바이스에 다시 저장한다.
 * 하지만, 유효하지 않은 Refresh Token 또는 기타 이유로 인한 서버의 ErrorResponse에 대해서는
 * MainActivity에서 로그아웃을 진행하고, 로그인 페이지로 이동하도록 한다.
 *
 * @see com.todoay.api.config.RetrofitService.TokenInterceptor.intercept
 * @see RefreshAPI
 */
class TokenManager {
    companion object {
        private val refreshService : RefreshAPI = RefreshAPI()
        fun refreshToken(refreshToken: String, onResponse: (RefreshResponse) -> Unit, onErrorResponse: (ErrorResponse) -> Unit) {
            printLog("[토큰 요청] Refresh Token 요청")
            val request = RefreshRequest(refreshToken)

            refreshService.refreshTokenToAccessToken(
                request,
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