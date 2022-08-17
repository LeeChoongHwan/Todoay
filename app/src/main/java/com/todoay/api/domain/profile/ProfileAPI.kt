package com.todoay.api.domain.profile

import android.os.Build
import androidx.annotation.RequiresApi
import com.todoay.api.config.RetrofitService
import com.todoay.api.config.ServiceRepository.ProfileServiceRepository.callProfileService
import com.todoay.api.domain.profile.dto.request.ModifyProfileRequest
import com.todoay.api.domain.profile.dto.response.ModifyProfileResponse
import com.todoay.api.domain.profile.dto.response.ProfileResponse
import com.todoay.api.util.response.error.ErrorResponse
import com.todoay.api.util.response.error.FailureResponse
import com.todoay.api.util.response.error.ValidErrorResponse
import com.todoay.global.util.Utils.Companion.printLog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * 유저 정보(Profile) 관련 API 호출 및 응답을 처리하는 클래스.
 * API Interface: callProfileService().kt
 */
class ProfileAPI {

    /**
     * 유저 정보(Profile) 조회 수행
     * [GET]("/profile/my")
     */
    fun getProfile(onResponse: (ProfileResponse) -> Unit, onErrorResponse: (ErrorResponse) -> Unit, onFailure: (FailureResponse) -> Unit) {
        callProfileService().getProfile()
            .enqueue(object : Callback<ProfileResponse> {
                override fun onResponse(
                    call: Call<ProfileResponse>,
                    response: Response<ProfileResponse>
                ) {
                    if(response.isSuccessful) {
                        val profileResponse : ProfileResponse = response.body()!!
                        onResponse(profileResponse)
                        printLog("[내 정보 조회] - 성공 {$profileResponse}")
                    }
                    else {
                        try {
                            val errorResponse = RetrofitService.getErrorResponse(response)
                            onErrorResponse(errorResponse)
                            printLog("[내 정보 조회] - 실패 {$errorResponse}")
                        }
                        catch (t: Throwable) {
                            onFailure(call, t)
                        }
                    }
                }

                @RequiresApi(Build.VERSION_CODES.O)
                override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                    val failure = RetrofitService.getFailure(
                        t, "/profile/my"
                    )
                    onFailure(failure)
                    printLog("SYSTEM ERROR - FAILED {$failure}")
                }

            })
    }

    /**
     * 유저 정보(Profile) 변경 수행
     * [PUT]("/profile/my")
     */
    fun putProfile(_nickname: String, _introMsg: String, _imageUrl: String, onResponse: (ModifyProfileResponse) -> Unit, onErrorResponse: (ErrorResponse) -> Unit, onFailure: (FailureResponse) -> Unit) {
        val request = ModifyProfileRequest(
            nickname = _nickname,
            introMsg = _introMsg,
            imageUrl = _imageUrl
        )
        callProfileService().putProfile(request)
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
                        printLog("[내 정보 변경] - 성공 {$modifyProfileResponse}")
                    }
                    else {
                        var errorResponse: ErrorResponse? = null
                        if(response.code() == 400) {
                            errorResponse = RetrofitService.getValidErrorResponse(response)
                            onErrorResponse(errorResponse)
                        }
                        else {
                            errorResponse = RetrofitService.getErrorResponse(response)
                            onErrorResponse(errorResponse)
                        }
                        printLog("[내 정보 변경] - 실패 {$errorResponse?}")
                    }
                }

                @RequiresApi(Build.VERSION_CODES.O)
                override fun onFailure(call: Call<ModifyProfileResponse>, t: Throwable) {
                    val failure = RetrofitService.getFailure(
                        t,  "/profile/my"
                    )
                    onFailure(failure)
                    printLog("SYSTEM ERROR - FAILED {$failure}")
                }

            })
    }

}