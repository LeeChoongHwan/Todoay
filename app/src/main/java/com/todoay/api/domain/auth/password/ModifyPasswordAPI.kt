package com.todoay.api.domain.auth.password

import android.os.Build
import androidx.annotation.RequiresApi
import com.todoay.api.config.RetrofitService
import com.todoay.api.config.ServiceRepository.AuthServiceRepository.callModifyPasswordService
import com.todoay.api.domain.auth.password.dto.request.ModifyPasswordRequest
import com.todoay.api.util.response.error.ErrorResponse
import com.todoay.api.util.response.error.FailureResponse
import com.todoay.api.util.response.success.SuccessResponse
import com.todoay.global.util.Utils.Companion.printLog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * 유저 비밀번호 관련 API 호출 및 응답을 처리하는 클래스.
 *
 * @see ModifyPasswordService
 */
class ModifyPasswordAPI {
    
    /**
     * 유저 비밀번호 변경 수행
     * [PATCH]("/auth/password")
     */
    fun modifyPassword(request: ModifyPasswordRequest, onResponse: (SuccessResponse) -> Unit, onErrorResponse: (ErrorResponse) -> Unit, onFailure: (FailureResponse) -> Unit) {
        callModifyPasswordService().patchModifyPassword(request)
            .enqueue(object: Callback<SuccessResponse> {
                override fun onResponse(
                    call: Call<SuccessResponse>,
                    response: Response<SuccessResponse>
                ) {
                    if(response.isSuccessful) {
                        val successResponse = SuccessResponse(
                            status = response.code()
                        )
                        onResponse(successResponse)
                        printLog("[비밀번호 변경] - 성공 {$successResponse}")
                    }
                    else {
                        val errorResponse = RetrofitService.getErrorResponse(response)
                        onErrorResponse(errorResponse)
                        printLog("[비밀번호 변경] - 실패 {$errorResponse}")
                    }
                }

                @RequiresApi(Build.VERSION_CODES.O)
                override fun onFailure(call: Call<SuccessResponse>, t: Throwable) {
                    val failure = RetrofitService.getFailure(
                        t,  "/auth/password"
                    )
                    onFailure(failure)
                    printLog("[SYSTEM ERROR] - 실패 {${failure}}")
                }

            })
    }

}