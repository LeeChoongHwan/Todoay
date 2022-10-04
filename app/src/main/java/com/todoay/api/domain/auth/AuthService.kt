package com.todoay.api.domain.auth

import com.todoay.api.domain.auth.dto.request.DeleteAuthRequest
import com.todoay.api.domain.auth.dto.request.LoginRequest
import com.todoay.api.domain.auth.dto.response.LoginResponse
import com.todoay.api.domain.auth.dto.response.NicknameResponse
import com.todoay.api.domain.auth.dto.request.ModifyPasswordRequest
import com.todoay.api.domain.auth.dto.request.RefreshRequest
import com.todoay.api.domain.auth.dto.response.RefreshResponse
import com.todoay.api.domain.auth.dto.request.SignUpRequest
import com.todoay.api.util.response.success.SuccessResponse
import okhttp3.internal.http.hasBody
import retrofit2.Call
import retrofit2.http.*

interface AuthService {

    /**
     * 회원가입
     *
     * @param requestBody SignUpRequest(email, password, nickname)
     * @return SuccessResponse or ErrorResponse
     */
    @POST("/auth/sign-up")
    fun postSignUp(@Body requestBody: SignUpRequest) : Call<SuccessResponse>

    /**
     * 로그인
     *
     * @param userData LoginRequest(email, password)
     * @return LoginResponse(accessToken, refreshToken) or ErrorResponse
     */
    @POST("/auth/login")
    fun postLogin(@Body userData: LoginRequest) : Call<LoginResponse>

    /**
     * 비밀번호 변경
     *
     * @param request ModifyPasswordRequest(originPassword, modifiedPassword)
     * @return SuccessResponse or ErrorResponse
     */
    @PATCH("/auth/password")
    fun patchModifyPassword(@Body request: ModifyPasswordRequest) : Call<SuccessResponse>

    /**
     * Access Token Refresh using in Refresh Token
     *
     * @param refreshRequest RefreshRequest(refreshToken)
     * @return RefreshResponse(accessToken, refreshToken) or ErrorResponse
     */
    @POST("/auth/refresh")
    fun postRefreshToken(@Body refreshRequest: RefreshRequest) : Call<RefreshResponse>

    /**
     * 닉네임 중복확인
     *
     * @param nickname 닉네임 String 변수
     * @return NicknameResponse(nicknameExist)
     */
    @GET("/auth/nickname-exists")
    fun getNicknameExists(@Query("nickname") nickname: String) : Call<NicknameResponse>

    /**
     * 계정 삭제(회원 탈퇴)
     *
     * @param request DeleteAuthRequest(password)
     * @return SuccessResponse or ErrorResponse
     */
    @HTTP(method = "DELETE", path = "/auth/my", hasBody = true)
    fun deleteAuth(@Body request : DeleteAuthRequest) : Call<SuccessResponse>
}