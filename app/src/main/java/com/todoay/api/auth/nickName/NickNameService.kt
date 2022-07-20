package com.todoay.api.auth.nickName

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * 유저 닉네임 관련 API 호출 인터페이스
 */
interface NickNameService {
    @POST("/auth/nickname-duplicate-check")
    fun postCheckNickNameDuplicate(@Body nickName: RequestNickName) : Call<ResponseNickName>
}