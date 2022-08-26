package com.todoay.api.config

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.gson.Gson
import com.todoay.MainActivity
import com.todoay.TodoayApplication
import com.todoay.api.config.RetrofitURL.ipAddress
import com.todoay.api.domain.auth.refresh.TokenManager
import com.todoay.api.util.response.error.ErrorResponse
import com.todoay.api.util.response.error.FailureResponse
import com.todoay.api.util.response.error.ValidErrorResponse
import com.todoay.global.util.Utils.Companion.printLog
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
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

    /**
     * Retrofit 객체 초기화 및 호출
     * 토큰이 필요 없음으로 Header에 토큰을 삽입하지 않음.
     */
    fun getServiceWithoutToken(): Retrofit {
        if(retrofitServiceWithoutToken == null) {
            printLog("[Retrofit 서비스 생성] RetrofitServiceWithoutToken 생성")
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
            printLog("[Retrofit 서비스 생성] RetrofitServiceWithToken 생성")
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
     * OkHttp3의 인터셉트를 통해 Header에 Token을 추가해주는 작업을 수행.
     */
    internal class TokenInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            var accessToken = TodoayApplication.pref.getAccessToken()
            val request = newRequestWithAccessToken(chain.request(), accessToken)
            var response = chain.proceed(request)
            if(response.code == HttpURLConnection.HTTP_UNAUTHORIZED) {
                printLog("[토큰 만료] Access Token 만료")
                /*
                Refresh Token 요청.
                onResponse():
                    RefreshToken 유효성 성공인 경우 Refresh Token을 서버로부터 받아와서 다시 request를 요청한다.
                onErrorResponse():
                    RefreshToken 유효성 실패인 경우 TokenManager.refreshToken()안에서 LoginFragment를 호출하고,
                    api 호출된 fragment에는 ErrorResponse를 전달한다.
                 */
                response.close()
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
        printLog("[Error Response 발생] {$errorResponse}")
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
        printLog("[Valid Error Response 발생] {$validErrorResponse}")
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
        printLog("[Failure 발생] {$failureResponse}")
        return failureResponse
    }
}