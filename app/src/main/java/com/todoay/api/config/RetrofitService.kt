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

    private const val baseURL = "http://${ipAddress}:8080"
    // 토큰이 없어도 되는 Retrofit
    private var retrofitServiceWithoutToken: Retrofit? = null
    private var okHttpClientWithoutToken: OkHttpClient? = null
    // 토큰이 있어야 하는 Retrofit
    private var retrofitServiceWithToken: Retrofit? = null
    private var okHttpClientWithToken: OkHttpClient? = null
    // 토큰이 있어야 하는 Retrofit
    private var retrofitServiceRefreshToken: Retrofit? = null
    private var okHttpClientRefreshToken: OkHttpClient? = null

    /**
     * Retrofit 객체 초기화 및 호출
     * 토큰이 필요 없음으로 Header에 토큰을 삽입하지 않음.
     */
    fun getServiceWithoutToken(): Retrofit {
        if(retrofitServiceWithoutToken == null) {
            Log.d("Retrofit", "RetrofitService - getServiceWithoutToken() called" )
            retrofitServiceWithoutToken = Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(getClientWithoutToken())
                .build()
        }
        return retrofitServiceWithoutToken!!
    }

    /**
     * Retorfit 객체 초기화 및 호출
     * 토큰이 필요함으로 Header에 토큰을 삽입함.
     */
    fun getServiceWithToken() : Retrofit {
        if(retrofitServiceWithToken == null) {
            Log.d("Retrofit", "RetrofitService - getServiceWithToken() called" )
            retrofitServiceWithToken = Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(getClientWithToken())
                .build()
        }
        return retrofitServiceWithToken!!
    }

    /**
     * Retrofit OkHttp Client 객체 초기화 및 호출
     * 토큰이 필요 없음으로 Header에 토큰을 삽입하지 않음.
     */
    private fun getClientWithoutToken() : OkHttpClient {
        if(okHttpClientWithoutToken == null) {
            Log.d("Retrofit", "RetrofitService - getClientWithoutToken() called")
            okHttpClientWithoutToken = OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                })
                .build()
        }
        return okHttpClientWithoutToken!!
    }

    /**
     * Retrofit OkHttp Client 객체 초기화 및 호출
     * 토큰이 필요함으로 Hedaer에 토큰을 삽입함.
     */
    private fun getClientWithToken() : OkHttpClient {
        if(okHttpClientWithToken == null) {
            Log.d("Retrofit", "RetrofitService - getClientWithToken() called")
            okHttpClientWithToken = OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                })
                .addInterceptor(TokenInterceptor())
                .build()
        }
        return okHttpClientWithToken!!
    }

    /**
     * Retorfit 객체 초기화 및 호출
     * 토큰이 필요함으로 Header에 토큰을 삽입함.
     */
    fun getServiceRefreshToken() : Retrofit {
        if(retrofitServiceRefreshToken == null) {
            Log.d("Retrofit", "RetrofitService - getServiceRefreshToken() called" )
            retrofitServiceRefreshToken = Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(getClientRefreshToken())
                .build()
        }
        return retrofitServiceRefreshToken!!
    }

    /**
     * Retrofit OkHttp Client 객체 초기화 및 호출
     * 토큰이 필요함으로 Hedaer에 토큰을 삽입함.
     */
    private fun getClientRefreshToken() : OkHttpClient {
        if(okHttpClientRefreshToken == null) {
            Log.d("Retrofit", "RetrofitService - getClientRefreshToken() called")
            okHttpClientRefreshToken = OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                })
//                .addInterceptor(RefreshTokenInterceptor())
                .build()
        }
        return okHttpClientRefreshToken!!
    }


    /**
     * OkHttp3의 인터셉트를 통해 Header에 Token을 추가해주는 작업을 수행.
     */
    private class TokenInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            while(true) {
                var accessToken = TodoayApplication.pref.getAccessToken()
                val request = chain.request().newBuilder()
                    .addHeader("X-AUTH-TOKEN", accessToken)
                    .build()
                var response = chain.proceed(request)
                when(response.code) {
                    HttpURLConnection.HTTP_UNAUTHORIZED -> {
                        TokenManager.refreshToken(TodoayApplication.pref.getRefreshToken())
                        response.close()
                    }
                    else -> {
                        Log.d("Retrofit", "RetrofitService - interceptor() called for set Token in Header")
                        return response
                    }
                }
            }
        }
    }

    /**
     * OkHttp3의 인터셉트를 통해 Header에 Token을 추가해주는 작업을 수행.
     */
    private class RefreshTokenInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            var refreshToken = TodoayApplication.pref.getRefreshToken()
            val request = chain.request().newBuilder()
                .addHeader("X-AUTH-TOKEN", refreshToken)
                .build()
            var response = chain.proceed(request)
            Log.d("Retrofit", "RetrofitService - interceptor() called for set Token in Header")
            return response
        }
    }


    /**
     * API의 onResponse 호출 이후 response가 실패할 경우
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
     * API의 onResponse 호출 이후 response가 실패할 경우
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