package com.todoay.api.domain.auth.password

import com.todoay.api.domain.auth.password.dto.request.ModifyPasswordRequest
import com.todoay.api.domain.auth.password.dto.response.ModifyPasswordResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.PATCH

/**
 * 유저 패스워드 관련 API 호출 인터페이스
 */
interface ModifyPasswordService {
    @PATCH("/auth/password")
    fun patchModifyPassword(@Body body: ModifyPasswordRequest) : Call<ModifyPasswordResponse>
}