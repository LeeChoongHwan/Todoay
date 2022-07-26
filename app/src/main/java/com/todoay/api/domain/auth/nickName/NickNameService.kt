package com.todoay.api.domain.auth.nickName

import com.todoay.api.domain.auth.nickName.dto.NickNameResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * 유저 닉네임 관련 API 호출 인터페이스
 */
interface NickNameService {
    @GET("/auth/nickname-exists")
    fun getNicknameExists(@Query("nickname") nickName: String) : Call<NickNameResponse>
}