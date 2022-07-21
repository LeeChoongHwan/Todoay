package com.todoay.api.retrofit

import android.util.Log
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * RetrofitService는 API 호출을 위해 Retrofit 라이브러리를 활용하여
 * 싱글톤 객체를 생성하고 관리하기 위한 클래스이다.
 */
object RetrofitService {

    // 임시로 서버와 api 통신하기 위해 서버 실행한 컴퓨터의 IP 주소 기입. 추후 aws 주소 입력 예정...
    // 실행할 때마다 각자 수정하도록...
    private val tempIPAddress = "192.168.0.242"

    private val baseURL = "http://${tempIPAddress}:8080"
    private var retrofitService: Retrofit? = null
    private var okHttpClient: OkHttpClient? = null

    /**
     * Retrofit OkHttp 인터셉터 싱글톤 객체 초기화
     */
    private fun getClient() : OkHttpClient {
        Log.d("Retrofit", "RetrofitService - getClient() called")
        if(okHttpClient == null) {
            okHttpClient = OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                })
                .addInterceptor(HeaderInterceptor("token example"))
                .build()
        }
        return okHttpClient!!
    }

    /**
     * OkHttp3의 인터셉트를 통해 Header에 Token을 추가해주는 작업을 수행.
     */
    private class HeaderInterceptor constructor(private val token: String) : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            Log.d("Retrofit", "RetrofitService - interceptor() called")
            return chain.proceed(
                chain.request().newBuilder()
                    .addHeader("Token", token)
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

}