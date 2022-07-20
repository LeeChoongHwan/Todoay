package com.todoay.api.profile

import com.todoay.api.profile.modifyProfile.RequestModifyProfile
import com.todoay.api.profile.modifyProfile.ResponseModifyProfile
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT

/**
 * 유저 정보(Profile) 관련 API 호출 인터페이스
 */
interface ProfileService {
    @GET("/profile/my")
    fun getProfile() : Call<ResponseProfile>

    @PUT("/profile/my")
    fun putModifyProfile(@Body body: RequestModifyProfile) : Call<ResponseModifyProfile>
}