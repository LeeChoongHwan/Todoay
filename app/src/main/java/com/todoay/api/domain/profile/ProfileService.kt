package com.todoay.api.domain.profile

import com.todoay.api.domain.profile.dto.ModifyProfileRequest
import com.todoay.api.domain.profile.dto.ModifyProfileResponse
import com.todoay.api.domain.profile.dto.ProfileResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT

/**
 * 유저 정보(Profile) 관련 API 호출 인터페이스
 */
interface ProfileService {
    @GET("/profile/my")
    fun getProfile() : Call<ProfileResponse>

    @PUT("/profile/my")
    fun putModifyProfile(@Body body: ModifyProfileRequest) : Call<ModifyProfileResponse>
}