package com.todoay.api.config

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.todoay.MainActivity
import com.todoay.api.config.RetrofitURL.amazonUrl
import com.todoay.api.config.gson.LocalDateConverter
import com.todoay.api.config.gson.LocalDateTimeConverter
import com.todoay.api.util.response.error.ErrorResponse
import com.todoay.api.util.response.error.FailureResponse
import com.todoay.api.util.response.error.ValidErrorResponse
import com.todoay.global.util.PrintUtil.printLog
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * RetrofitService는 API 호출을 위해 Retrofit 라이브러리를 활용하여
 * 싱글톤 객체를 생성하고 관리하기 위한 클래스이다.
 */
object RetrofitService {

//    private const val baseUrl = "http://${ipAddress}:8080"
    private const val baseUrl = "http://$amazonUrl"
    // 토큰이 없어도 되는 Retrofit
    private var retrofitServiceWithoutToken: Retrofit? = null
    private var okHttpClientWithoutToken: OkHttpClient? = null
    // 토큰이 있어야 하는 Retrofit
    private var retrofitServiceWithToken: Retrofit? = null
    private var okHttpClientWithToken: OkHttpClient? = null

    /**
     * Retrofit 객체 초기화 및 호출
     * 토큰이 필요 없음으로 Header에 토큰을 삽입하지 않음.
     */
    fun getServiceWithoutToken(): Retrofit {
        if(retrofitServiceWithoutToken == null) {
            printLog("[Retrofit 서비스 생성] RetrofitServiceWithoutToken 생성")
            retrofitServiceWithoutToken = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(NullOnEmptyConverterFactory())
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
            printLog("[Retrofit 서비스 생성] RetrofitServiceWithToken 생성")
            val gson = GsonBuilder()
                .registerTypeAdapter(LocalDate::class.java, LocalDateConverter.LocalDateSerializer())
                .registerTypeAdapter(LocalDate::class.java, LocalDateConverter.LocalDateDeserializer())
                .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeConverter.LocalDateTimeSerializer())
                .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeConverter.LocalDateTimeDeserializer())
                .create()
            retrofitServiceWithToken = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(NullOnEmptyConverterFactory())
                .addConverterFactory(GsonConverterFactory.create(gson))
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
            printLog("[Retrofit 클라이언트 생성] ClientWithoutToken 생성")
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
            printLog("[Retrofit 클라이언트 생성] ClientWithToken 생성")
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
     * API의 onResponse 호출 이후 response가 실패할 경우
     * ErrorResponse 객체를 초기화하여 리턴하기 위함.
     */
    fun <T> getErrorResponse(response: retrofit2.Response<T>) : ErrorResponse {
        val gsonError: ErrorResponse = Gson().fromJson(response.errorBody()!!.charStream(), ErrorResponse::class.java)
        val errorResponse = ErrorResponse(
            timestamp = gsonError.timestamp,
            status = gsonError.status,
            error = gsonError.error,
            code = gsonError.code,
            message = gsonError.message,
            path = gsonError.path
        )
        return errorResponse
    }

    /**
     * API의 onResponse 호출 이후 response가 실패할 경우
     * 서버의 유효성 검증에 대한 ValidErrorResponse 객체를 초기화하여 리턴하기 위함.
    */
    fun <T> getValidErrorResponse(response: retrofit2.Response<T>) : ValidErrorResponse {
        val gsonError : ValidErrorResponse = Gson().fromJson(response.errorBody()!!.charStream(), ValidErrorResponse::class.java)
        val validErrorResponse = ValidErrorResponse(
            timestamp = gsonError.timestamp,
            status = gsonError.status,
            error = gsonError.error,
            code = gsonError.code,
            message = gsonError.message,
            path = gsonError.path,
            details = gsonError.details
        )
        return validErrorResponse
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
        if(t is ConnectException)
            code = "네트워크 연결을 확인해주세요"
        else if(t is SocketTimeoutException)
            code = "서버와의 연결이 불안정합니다..\n고객센터로 문의해주세요"
        val failureResponse = FailureResponse(
            timestamp = LocalDateTime.now().toString(),
            exception = t,
            code = code,
            path = "$path"
        )
        MainActivity.mainAct.showLongToast(code)
        return failureResponse
    }
}