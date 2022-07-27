package com.todoay.api.config

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.gson.Gson
import com.todoay.api.config.RetrofitURL.ipAddress
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
     * Retrofit OkHttp 인터셉터 싱글톤 객체 초기화
     */
    private fun getClient() : OkHttpClient {
        Log.d("Retrofit", "RetrofitService - getClient() called")
        if(okHttpClient == null) {
            // AccessToken이 없을 경우
            if(TodoayApplication.pref.getAccessToken() == "") {
                Log.d("Retrofit", "RetrofitService - Token isn't ${TodoayApplication.pref.getAccessToken()}")
                okHttpClient = OkHttpClient.Builder()
                    .addInterceptor(HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    })
                    .build()
            }
            // AccessToken이 없는 경우
            else {
                Log.d("Retrofit", "RetrofitService - Token is ${TodoayApplication.pref.getAccessToken()}")
                okHttpClient = OkHttpClient.Builder()
                    .addInterceptor(HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    })
                    .addInterceptor(HeaderInterceptor(TodoayApplication.pref.getAccessToken()))
                    .build()
            }
        }
        return okHttpClient!!
    }

    /**
     * OkHttp3의 인터셉트를 통해 Header에 Token을 추가해주는 작업을 수행.
     */
    private class HeaderInterceptor constructor(private val token: String) : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            Log.d("Retrofit", "RetrofitService - interceptor() called for add Token in Header $token")
            return chain.proceed(
                chain.request().newBuilder()
                    .addHeader("X-AUTH-TOKEN", token)
                    .build()
            )
        }
    }

    /**
     * Retrofit 객체 호출
     */
    fun getService(): Retrofit {
        Log.d("Retrofit", "RetrofitService - getService() called" )
        if(retrofitService == null) {
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
        val gson = Gson()
        var gsonError: ErrorResponse = gson.fromJson(response.errorBody()!!.charStream(), ErrorResponse::class.java)
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
     * 서버의 유효성 검증에 대한 ValidErrorReponse 객체를 초기화하여 리턴하기 위함.
    */
    fun <T> getValidErrorResponse(response: retrofit2.Response<T>) : ValidErrorResponse {
        val gson = Gson()
        val gsonError : ValidErrorResponse = gson.fromJson(response.errorBody()!!.charStream(), ValidErrorResponse::class.java)
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