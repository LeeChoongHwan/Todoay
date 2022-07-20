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

    private val baseURL = "https://localhost:8080"
    private lateinit var retrofitService: Retrofit
    private lateinit var okHttpClient: OkHttpClient

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
        return okHttpClient
    }

    /**
     * OkHttp3의 인터셉트를 통해 Header에 Token을 추가해주는 작업을 수행.
     */
    private class HeaderInterceptor constructor(private val token: String) : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
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
        return retrofitService
    }

}