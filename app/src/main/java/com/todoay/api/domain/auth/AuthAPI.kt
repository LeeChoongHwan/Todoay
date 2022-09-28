package com.todoay.api.domain.auth

import android.os.Build
import androidx.annotation.RequiresApi
import com.todoay.api.config.RetrofitService
import com.todoay.api.config.ServiceRepository
import com.todoay.api.config.ServiceRepository.AuthServiceRepository.callAuthServiceWithToken
import com.todoay.api.config.ServiceRepository.AuthServiceRepository.callAuthServiceWithoutToken
import com.todoay.api.domain.auth.dto.request.*
import com.todoay.api.domain.auth.dto.response.LoginResponse
import com.todoay.api.domain.auth.dto.response.RefreshResponse
import com.todoay.api.domain.auth.dto.response.NicknameResponse
import com.todoay.api.util.response.error.ErrorResponse
import com.todoay.api.util.response.error.FailureResponse
import com.todoay.api.util.response.error.ValidErrorResponse
import com.todoay.api.util.response.success.SuccessResponse
import com.todoay.global.util.PrintUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Auth(유저 인증) 관련 API 호출 및 응답을 처리하는 클래스
 *
 * @see AuthService
 */
class AuthAPI {

    companion object{
        private var instance : AuthAPI? = null
        fun getInstance() : AuthAPI {
            return instance ?: synchronized(this) {
                instance ?: AuthAPI().also {
                    instance  = it
                }
            }
        }
    }

    /**
     * 유저 회원가입 수행
     * @see AuthService.postSignUp
     */
    fun signUp(request: SignUpRequest, onResponse: (SuccessResponse) -> Unit, onErrorResponse: (ValidErrorResponse) -> Unit, onFailure: (FailureResponse) -> Unit) {
        callAuthServiceWithoutToken().postSignUp(request)
            .enqueue(object : Callback<SuccessResponse> {
                override fun onResponse(
                    call: Call<SuccessResponse>,
                    response: Response<SuccessResponse>
                ) {
                    if(response.isSuccessful) {
                        val successResponse = SuccessResponse(
                            status = response.code()
                        )
                        onResponse(successResponse)
                        PrintUtil.printLog("[회원가입 요청] - 성공 {$successResponse}")
                    }
                    else {
                        val validErrorResponse = RetrofitService.getValidErrorResponse(response)
                        onErrorResponse(validErrorResponse)
                        PrintUtil.printLog("[회원가입 요청] - 실패 {$validErrorResponse}")
                    }
                }

                @RequiresApi(Build.VERSION_CODES.O)
                override fun onFailure(call: Call<SuccessResponse>, t: Throwable) {
                    val failure = RetrofitService.getFailure(
                        t, "/auth/sign-up"
                    )
                    onFailure(failure)
                    PrintUtil.printLog("[SYSTEM ERROR] - 실패 {${failure}}")
                }

            })
    }

    /**
     * 로그인 수행
     * [POST]("/auth/login")
     */
    fun login(request: LoginRequest, onResponse: (LoginResponse) -> Unit, onErrorResponse: (ErrorResponse) -> Unit, onFailure: (FailureResponse) -> Unit) {
        callAuthServiceWithoutToken().postLogin(request)
            .enqueue(object : Callback<LoginResponse> {
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    // 로그인 성공
                    if(response.isSuccessful) {
                        val loginResponse : LoginResponse = response.body()!!
                        onResponse(loginResponse)
                        PrintUtil.printLog("[로그인] - 성공 {${loginResponse}}")
                    }
                    // 로그인 실패
                    else {
                        val errorResponse = RetrofitService.getErrorResponse(response)
                        onErrorResponse(errorResponse)
                        PrintUtil.printLog("[로그인] - 실패 {${errorResponse}}")
                    }
                }

                @RequiresApi(Build.VERSION_CODES.O)
                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    val failure = RetrofitService.getFailure(
                        t,  "/auth/login"
                    )
                    onFailure(failure)
                    PrintUtil.printLog("[SYSTEM ERROR] - 실패 {${failure}}")
                }
            })
    }

    /**
     * 유저 비밀번호 변경 수행
     * [PATCH]("/auth/password")
     */
    fun modifyPassword(request: ModifyPasswordRequest, onResponse: (SuccessResponse) -> Unit, onErrorResponse: (ErrorResponse) -> Unit, onFailure: (FailureResponse) -> Unit) {
        callAuthServiceWithToken().patchModifyPassword(request)
            .enqueue(object: Callback<SuccessResponse> {
                override fun onResponse(
                    call: Call<SuccessResponse>,
                    response: Response<SuccessResponse>
                ) {
                    if(response.isSuccessful) {
                        val successResponse = SuccessResponse(
                            status = response.code()
                        )
                        onResponse(successResponse)
                        PrintUtil.printLog("[비밀번호 변경] - 성공 {$successResponse}")
                    }
                    else {
                        val errorResponse = RetrofitService.getErrorResponse(response)
                        onErrorResponse(errorResponse)
                        PrintUtil.printLog("[비밀번호 변경] - 실패 {$errorResponse}")
                    }
                }

                @RequiresApi(Build.VERSION_CODES.O)
                override fun onFailure(call: Call<SuccessResponse>, t: Throwable) {
                    val failure = RetrofitService.getFailure(
                        t,  "/auth/password"
                    )
                    onFailure(failure)
                    PrintUtil.printLog("[SYSTEM ERROR] - 실패 {${failure}}")
                }

            })
    }

    /**
     * 토큰 재발행 수행
     * [POST]("/auth/refresh")
     */
    fun refreshTokenToAccessToken(request: RefreshRequest, onResponse: (RefreshResponse) -> Unit, onErrorResponse: (ErrorResponse) -> Unit) {
        /* execute()를 통한 동기성 처리 */
        val response = callAuthServiceWithoutToken().postRefreshToken(request).execute()
        if(response.isSuccessful) {
            val refreshResponse : RefreshResponse = response.body()!!
            onResponse(refreshResponse)
        }
        else {
            val errorResponse = RetrofitService.getErrorResponse(response)
            onErrorResponse(errorResponse)
        }
    }


    /**
     * 유저 닉네임 중복확인 수행
     * [GET]("/auth/nickname-exists")
     */
    fun checkNicknameExists(nickname: String, onResponse: (NicknameResponse) -> Unit, onErrorResponse: (ValidErrorResponse) -> Unit, onFailure: (FailureResponse) -> Unit) {
        callAuthServiceWithoutToken().getNicknameExists(nickname)
            .enqueue(object : Callback<NicknameResponse> {
                override fun onResponse(
                    call: Call<NicknameResponse>,
                    response: Response<NicknameResponse>
                ) {
                    if(response.isSuccessful) {
                        val nickNameResponse : NicknameResponse = response.body()!!
                        onResponse(nickNameResponse)
                        PrintUtil.printLog("[닉네임 중복확인] - 성공 {$nickNameResponse}")
                    }
                    else {
                        val errorResponse = RetrofitService.getValidErrorResponse(response)
                        onErrorResponse(errorResponse)
                        PrintUtil.printLog("[닉네임 중복확인] - 실패 {$errorResponse}")
                    }
                }

                @RequiresApi(Build.VERSION_CODES.O)
                override fun onFailure(call: Call<NicknameResponse>, t: Throwable) {
                    val failure = RetrofitService.getFailure(
                        t, "/auth/nickname-exists"
                    )
                    onFailure(failure)
                    PrintUtil.printLog("[SYSTEM ERROR] - 실패 {${failure}}")
                }

            })
    }

    /**
     * 계정 삭제
     * [DELETE]("/auth/my")
     */
    fun withdrawal(request: DeleteAuthRequest, onResponse: (SuccessResponse) -> Unit, onErrorResponse: (ErrorResponse) -> Unit, onFailure: (FailureResponse) -> Unit) {
        callAuthServiceWithToken().deleteAuth(request)
            .enqueue(object : Callback<SuccessResponse> {
                override fun onResponse(
                    call: Call<SuccessResponse>,
                    response: Response<SuccessResponse>
                ) {
                    if(response.isSuccessful) {
                        val successResponse = SuccessResponse(response.code())
                        onResponse(successResponse)
                        PrintUtil.printLog("[계정 삭제] - 성공 {$successResponse}")
                    }
                    else {
                        val errorResponse = RetrofitService.getErrorResponse(response)
                        onErrorResponse(errorResponse)
                        PrintUtil.printLog("[계정 삭제] - 실패 {$errorResponse}")
                    }
                }

                override fun onFailure(call: Call<SuccessResponse>, t: Throwable) {
                    val failure = RetrofitService.getFailure(
                        t, "/auth/my"
                    )
                    onFailure(failure)
                    PrintUtil.printLog("[SYSTEM ERROR] - 실패 {${failure}}")
                }

            })
    }

}