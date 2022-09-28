package com.todoay.api.domain.profile

import com.todoay.api.domain.profile.dto.request.ModifyProfileRequest
import com.todoay.api.domain.profile.dto.response.ProfileResponse
import com.todoay.api.util.response.success.SuccessResponse
import com.todoay.data.profile.Profile
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

/**
 * 유저 정보(Profile) 관련 API 호출 인터페이스
 */
interface ProfileService {
    @GET("/profile/my")
    fun getProfile() : Call<ProfileResponse>

    @Multipart
    @PUT("/profile/my")
    fun putProfile(@Part image: MultipartBody.Part, @Part profile : MultipartBody.Part) : Call<SuccessResponse>
}