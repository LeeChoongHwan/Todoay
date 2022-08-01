package com.todoay.api.config

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.gson.Gson
import com.todoay.api.config.RetrofitURL.ipAddress
import com.todoay.api.util.TokenManager
import com.todoay.api.util.response.error.ErrorResponse
import com.todoay.api.util.response.error.FailureResponse
import com.todoay.api.util.response.error.ValidErrorResponse
import com.todoay.global.util.TodoayApplication
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.ConnectException
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import java.time.LocalDateTime

/**
 * RetrofitService는 API 호출을 위해 Retrofit 라이브러리를 활용하여
 * 싱글톤 객체를 생성하고 관리하기 위한 클래스이다.
 */
object RetrofitService {

    private val baseURL = "http://${ipAddress}:8080"
    private var retrofitService: Retrofit? = null
    private var okHttpClient: OkHttpClient? = null

    /**
     * 토큰의 여부에 따라 okHttpClient와 RetrofitService를 초기화하기 위해
     * 싱글톤 객체를 null로 변경하는 메소드
     *
     * 사용: 로그인 또는 로그아웃 시
     */
    fun refresh() {
        Log.d("Retrofit", "RetrofitService - refresh() called")
        okHttpClient = null
        retrofitService = null
    }

    /**
     * Retrofit OkHttp 인터셉터 싱글톤 객체 초기화
     */
    private fun getClient() : OkHttpClient {
        if(okHttpClient == null) {
            // 토큰이 없는 경우 -> Header 에 토큰을 넣지 않음.
            if(TodoayApplication.pref.getAccessToken() == "") {
                Log.d("Retrofit", "RetrofitService - getClient() called Not Exist Token")
                okHttpClient = OkHttpClient.Builder()
                    .addInterceptor(HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    })
                    .build()
            }
            // 토큰이 있는 경우 -> Header 에 토큰을 추가함.
            else {
                Log.d("Retrofit", "RetrofitService - getClient() called Exist Token")
                okHttpClient = OkHttpClient.Builder()
                    .addInterceptor(HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    })
                    .addInterceptor(TokenInterceptor())
                    .build()
            }
        }
        return okHttpClient!!
    }

    /**
     * OkHttp3의 인터셉트를 통해 Header에 Token을 추가해주는 작업을 수행.
     */
    private class TokenInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            var accessToken = TodoayApplication.pref.getAccessToken()
            val request = chain.request().newBuilder()
                .addHeader("X-AUTH-TOKEN", accessToken)
                .build()
            val response = chain.proceed(request)
            when(response.code) {
                HttpURLConnection.HTTP_UNAUTHORIZED -> {
                    TokenManager.refreshToken(TodoayApplication.pref.getRefreshToken())
                    accessToken = TodoayApplication.pref.getAccessToken()
                    if(accessToken!="") {
                        val newRequest = chain.request().newBuilder()
                            .addHeader("X-AUTH-TOKEN", accessToken)
                            .build()
                        response.close()
                        Log.d("Retrofit", "RetrofitService - interceptor() called for refresh Token $accessToken")
                        return chain.proceed(newRequest)
                    }
                }
            }
            Log.d("Retrofit", "RetrofitService - interceptor() called for set Token $accessToken")
            return response
        }
    }

    /**
     * Retrofit 객체 호출
     */
    fun getService(): Retrofit {
        if(retrofitService == null) {
            Log.d("Retrofit", "RetrofitService - getService() called" )
            retrofitService = Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(getClient())
                .build()
        }
        return retrofitService!!
    }

    /**
     * API의 onResponse 호출 이후 response가 성공이 아닐 경우
     * ErrorResponse 객체를 초기화하여 리턴하기 위함.
     */
    fun <T> getErrorResponse(response: retrofit2.Response<T>) : ErrorResponse {
        val gsonError: ErrorResponse = Gson().fromJson(response.errorBody()!!.charStream(), ErrorResponse::class.java)
        Log.d("ErrorResponse", "Gson: {$gsonError}")
        return ErrorResponse(
            timestamp = gsonError.timestamp,
            status = gsonError.status,
            error = gsonError.error,
            code = gsonError.code,
            message = gsonError.message,
            path = gsonError.path
        )
    }

    /**
     * API의 onResponse 호출 이후 response가 성공이 아닐 경우
     * 서버의 유효성 검증에 대한 ValidErrorResponse 객체를 초기화하여 리턴하기 위함.
    */
    fun <T> getValidErrorResponse(response: retrofit2.Response<T>) : ValidErrorResponse {
        val gsonError : ValidErrorResponse = Gson().fromJson(response.errorBody()!!.charStream(), ValidErrorResponse::class.java)
        Log.d("ValidErrorResponse", "Gson: {$gsonError}")
        return ValidErrorResponse(
            timestamp = gsonError.timestamp,
            status = gsonError.status,
            error = gsonError.error,
            code = gsonError.code,
            message = gsonError.message,
            path = gsonError.path,
            details = gsonError.details
        )
    }

    /**
     * API 통신을 실패한 경우
     * API의 onFailure 호출되어 Failure 객체를 초기화하여 리턴하기 위함.
     * 예외 상황이 ConnectException인 경우 네트워크 연결 실패로 간주.
     * 예외 상황이 SocketTimeoutException인 경우 서버 연결 실패로 간주.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun getFailure(t: Throwable, path: String) : FailureResponse {
        var code = "서비스 이용 불가...\n고객센터로 문의해주세요"
        if(t is ConnectException) {
            code = "네트워크 연결을 확인해주세요"
        }
        else if(t is SocketTimeoutException) {
            code = "서버와의 연결이 불안정합니다..\n고객센터로 문의해주세요"
        }
        return FailureResponse(
            timestamp = LocalDateTime.now().toString(),
            exception = t,
            code = code,
            path = "$path"
        )
    }
}