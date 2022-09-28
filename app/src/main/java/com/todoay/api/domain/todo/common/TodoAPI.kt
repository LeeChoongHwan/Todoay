package com.todoay.api.domain.todo.common

import com.todoay.api.config.RetrofitService
import com.todoay.api.config.ServiceRepository.TodoServiceRepository.callTodoService
import com.todoay.api.util.response.error.ErrorResponse
import com.todoay.api.util.response.error.FailureResponse
import com.todoay.api.util.response.success.SuccessResponse
import com.todoay.global.util.PrintUtil.printLog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * DailyTodo 및 DueDateTodo 공통 관련 API 호출 및 응답을 처리하는 클래스.
 *
 * @see TodoService
 */
class TodoAPI {

    companion object {
        private var instance : TodoAPI? = null
        fun getInstance() : TodoAPI {
            return instance ?: synchronized(this) {
                instance ?: TodoAPI().also {
                    instance  = it
                }
            }
        }
    }

    /**
     * 투두의 완료 상태를 바꾼다.
     *
     * @param id 완료 상태를 바꿀 투두 id
     * @param onResponse
     * @param onErrorResponse
     * @param onFailure
     */
    fun switchTodoFinishState(id : Long, onResponse : (SuccessResponse) -> Unit, onErrorResponse : (ErrorResponse) -> Unit, onFailure : (FailureResponse) -> Unit) {
        callTodoService().switchTodoFinishState(id)
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
                        printLog("[Todo 완료 상태 변경] - 성공 {$successResponse}")
                    }
                    else {
                        try {
                            val errorResponse = RetrofitService.getErrorResponse(response)
                            onErrorResponse(errorResponse)
                            printLog("[Todo 완료 상태 변경] - 실패 {$errorResponse}")
                        }
                        catch (t : Throwable) {
                            onFailure(call, t)
                        }
                    }
                }

                override fun onFailure(call: Call<SuccessResponse>, t: Throwable) {
                    val failure = RetrofitService.getFailure(
                        t, "/todo/{id}/switch"
                    )
                    onFailure(failure)
                    printLog("SYSTEM ERROR - FAILED {$failure}")
                }
            })
    }

    /**
     * 투두를 삭제한다.
     *
     * @param id 삭제할 투두 id
     * @param onResponse
     * @param onErrorResponse
     * @param onFailure
     */
    fun deleteTodo(id : Long, onResponse : (SuccessResponse) -> Unit, onErrorResponse: (ErrorResponse) -> Unit, onFailure: (FailureResponse) -> Unit) {
        callTodoService().deleteTodo(id)
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
                        printLog("[Todo 삭제] - 성공 {$successResponse}")
                    }
                    else {
                        try {
                            val errorResponse = RetrofitService.getErrorResponse(response)
                            onErrorResponse(errorResponse)
                            printLog("[Todo 삭제] - 실패 {$errorResponse}")
                        }
                        catch (t: Throwable) {
                            onFailure(call, t)
                        }
                    }
                }

                override fun onFailure(call: Call<SuccessResponse>, t: Throwable) {
                    val failure = RetrofitService.getFailure(
                        t, "/todo/{id}"
                    )
                    onFailure(failure)
                    printLog("SYSTEM ERROR - FAILED {$failure}")
                }

            })
    }

}