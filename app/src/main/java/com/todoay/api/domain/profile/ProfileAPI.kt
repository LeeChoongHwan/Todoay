package com.todoay.api.domain.profile

import android.os.Build
import androidx.annotation.RequiresApi
import com.todoay.api.config.RetrofitService
import com.todoay.api.config.ServiceRepository.ProfileServiceRepository.callProfileService
import com.todoay.api.domain.profile.dto.request.ModifyProfileRequest
import com.todoay.api.domain.profile.dto.response.ProfileResponse
import com.todoay.api.util.response.error.ErrorResponse
import com.todoay.api.util.response.error.FailureResponse
import com.todoay.api.util.response.success.SuccessResponse
import com.todoay.global.util.PrintUtil.printLog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * 유저 정보(Profile) 관련 API 호출 및 응답을 처리하는 클래스.
 *
 * @see ProfileService
 */
class ProfileAPI {

    companion object {
        private var instance : ProfileAPI? = null
        fun getInstance() : ProfileAPI {
            return instance ?: synchronized(this) {
                instance ?: ProfileAPI().also {
                    instance  = it
                }
            }
        }
    }

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
    fun putProfile(request: ModifyProfileRequest, onResponse: (SuccessResponse) -> Unit, onErrorResponse: (ErrorResponse) -> Unit, onFailure: (FailureResponse) -> Unit) {
        callProfileService().putProfile(request)
            .enqueue(object : Callback<SuccessResponse> {
                override fun onResponse(
                    call: Call<SuccessResponse>,
                    response: Response<SuccessResponse>
                ) {
                    if(response.isSuccessful) {
                        val successResponse = SuccessResponse(
                            status = response.code()
                        )
                        onResponse(successResponse)
                        printLog("[내 정보 변경] - 성공 {$successResponse}")
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
                override fun onFailure(call: Call<SuccessResponse>, t: Throwable) {
                    val failure = RetrofitService.getFailure(
                        t,  "/profile/my"
                    )
                    onFailure(failure)
                    printLog("SYSTEM ERROR - FAILED {$failure}")
                }

            })
    }

}