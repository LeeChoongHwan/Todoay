package com.todoay.api.config

import com.google.gson.Gson
import com.todoay.TodoayApplication
import com.todoay.api.domain.auth.refresh.TokenManager
import com.todoay.global.util.PrintUtil.printLog
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import java.net.HttpURLConnection

class TokenInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var accessToken = TodoayApplication.pref.getAccessToken()
        val request = newRequestWithAccessToken(chain.request(), accessToken)
        var response = chain.proceed(request)
        if(response.code == HttpURLConnection.HTTP_UNAUTHORIZED) {
            printLog("[토큰 만료] Access Token 만료")
            response.close()
            /*
            Refresh Token 요청.
            onResponse():
                RefreshToken 유효성 성공인 경우 Refresh Token을 서버로부터 받아와서 다시 request를 요청한다.
            onErrorResponse():
                RefreshToken 유효성 실패인 경우 TokenManager.refreshToken()안에서 LoginFragment를 호출하고,
                api 호출된 fragment에는 ErrorResponse를 전달한다.
             */
            TokenManager.refreshToken(
                TodoayApplication.pref.getRefreshToken(),
                onResponse = {
                    val newAccessToken = TodoayApplication.pref.getAccessToken()
                    response = chain.proceed(newRequestWithAccessToken(request, newAccessToken))
                },
                onErrorResponse = {
                    response = Response.Builder()
                        .code(it.status)
                        .protocol(Protocol.HTTP_2)
                        .body(ResponseBody.create("application/json".toMediaType(), "${Gson().toJson(it)}"))
                        .message(it.code!!)
                        .request(chain.request())
                        .build()
                })
        }
        return response
    }

    private fun newRequestWithAccessToken(request: Request, accessToken: String) : Request {
        printLog("[토큰 세팅] Access Token 헤더 세팅")
        return request.newBuilder()
            .header("X-AUTH-TOKEN", accessToken)
            .build()
    }
}