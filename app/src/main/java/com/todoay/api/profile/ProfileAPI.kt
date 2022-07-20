package com.todoay.api.profile

import android.util.Log
import com.todoay.api.profile.modifyProfile.RequestModifyProfile
import com.todoay.api.profile.modifyProfile.ResponseModifyProfile
import com.todoay.api.retrofit.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * 유저 정보(Profile) 관련 API 호출 및 응답을 처리하는 클래스.
 * API Interface: ProfileService.kt
 */
class ProfileAPI {

    private val service = RetrofitService.getService().create(ProfileService::class.java)

    /**
     * 유저 정보(Profile) 조회 수행
     */
    fun getMyProfile() {
        service.getProfile()
            .enqueue(object : Callback<ResponseProfile> {
                override fun onResponse(
                    call: Call<ResponseProfile>,
                    response: Response<ResponseProfile>
                ) {
                    if(response.isSuccessful) {
                        val result = response.body()

                        Log.d("profile", "get profile - success{$result}")
                    }
                }

                override fun onFailure(call: Call<ResponseProfile>, t: Throwable) {

                    Log.d("profile", "get profile - failed{${t.message.toString()}")
                }

            })
    }

    /**
     * 유저 정보(Profile) 변경 수행
     */
    fun modifyMyProfile(_nickName: String, _message: String, _imageUrl: String) {
        val requestBody = RequestModifyProfile(
            nickName = _nickName,
            message = _message,
            imageUrl = _imageUrl
        )

        service.putModifyProfile(requestBody)
            .enqueue(object : Callback<ResponseModifyProfile> {
                override fun onResponse(
                    call: Call<ResponseModifyProfile>,
                    response: Response<ResponseModifyProfile>
                ) {
                    if(response.isSuccessful) {
                        val result = response.body()

                        Log.d("profile", "modify profile - success{$result}")
                    }
                }

                override fun onFailure(call: Call<ResponseModifyProfile>, t: Throwable) {

                    Log.d("profile", "modify profile - failed{${t.message.toString()}")
                }

            })
    }
}