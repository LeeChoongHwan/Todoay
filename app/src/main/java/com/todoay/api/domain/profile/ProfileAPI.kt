package com.todoay.api.domain.profile

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.todoay.api.domain.profile.dto.request.ModifyProfileRequest
import com.todoay.api.domain.profile.dto.response.ModifyProfileResponse
import com.todoay.api.domain.profile.dto.response.ProfileResponse
import com.todoay.api.util.response.error.ErrorResponse
import com.todoay.api.config.RetrofitService
import com.todoay.api.config.ServiceRepository.ProfileServiceRepository.profileService
import com.todoay.api.util.response.error.FailureResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * 유저 정보(Profile) 관련 API 호출 및 응답을 처리하는 클래스.
 * API Interface: ProfileService.kt
 */
class ProfileAPI {

    /**
     * 유저 정보(Profile) 조회 수행
     */
    fun getProfile(onResponse: (ProfileResponse) -> Unit, onErrorResponse: (ErrorResponse) -> Unit, onFailure: (FailureResponse) -> Unit) {
        profileService.getProfile()
            .enqueue(object : Callback<ProfileResponse> {
                override fun onResponse(
                    call: Call<ProfileResponse>,
                    response: Response<ProfileResponse>
                ) {
                    if(response.isSuccessful) {
                        val profileResponse : ProfileResponse = response.body()!!
                        onResponse(profileResponse)
                        Log.d("profile", "get profile - success {$profileResponse}")
                    }
                    else {
                        val errorResponse = RetrofitService.getErrorResponse(response)
                        onErrorResponse(errorResponse)
                        Log.d("profile", "get profile - failed {$errorResponse}")
                    }
                }

                @RequiresApi(Build.VERSION_CODES.O)
                override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                    val failure = RetrofitService.getFailure(
                        t, "/profile/my"
                    )
                    onFailure(failure)
                    Log.d("profile", "system - failed {${failure}}")
                }

            })
    }

    /**
     * 유저 정보(Profile) 변경 수행
     */
    fun putProfile(_nickname: String, _introMsg: String, _imageUrl: String, onResponse: (ModifyProfileResponse) -> Unit, onErrorResponse: (ErrorResponse) -> Unit, onFailure: (FailureResponse) -> Unit) {
        val request = ModifyProfileRequest(
            nickname = _nickname,
            introMsg = _introMsg,
            imageUrl = _imageUrl
        )
        profileService.putProfile(request)
            .enqueue(object : Callback<ModifyProfileResponse> {
                override fun onResponse(
                    call: Call<ModifyProfileResponse>,
                    response: Response<ModifyProfileResponse>
                ) {
                    if(response.isSuccessful) {
                        val modifyProfileResponse = ModifyProfileResponse(
                            status = response.code()
                        )
                        onResponse(modifyProfileResponse)
                        Log.d("profile", "modify profile - success {$modifyProfileResponse}")
                    }
                    else {
                        val errorResponse = RetrofitService.getErrorResponse(response)
                        onErrorResponse(errorResponse)
                        Log.d("profile", "modify profile - failed {$errorResponse}")
                    }
                }

                @RequiresApi(Build.VERSION_CODES.O)
                override fun onFailure(call: Call<ModifyProfileResponse>, t: Throwable) {
                    val failure = RetrofitService.getFailure(
                        t,  "/profile/my"
                    )
                    onFailure(failure)
                    Log.d("profile", "system - failed {${failure}}")
                }

            })
    }

}