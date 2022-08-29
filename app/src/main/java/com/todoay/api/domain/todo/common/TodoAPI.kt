package com.todoay.api.domain.todo.common

import com.todoay.api.config.ServiceRepository.TodoServiceRepository.callTodoService
import com.todoay.api.util.response.error.ErrorResponse
import com.todoay.api.util.response.error.FailureResponse
import com.todoay.api.util.response.success.SuccessResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * DailyTodo 및 DueDateTodo 공통 관련 API 호출 및 응답을 처리하는 클래스.
 *
 * @see TodoService
 */
class TodoAPI {

    /**
     * 투두의 완료 상태를 바꾼다.
     *
     * @param id 완료 상태를 바꿀 투두 id
     * @param onResponse
     * @param onErrorResponse
     * @param onFailure
     */
    fun switchTodoComplete(id : Int, onResponse : (SuccessResponse) -> Unit, onErrorResponse : (ErrorResponse) -> Unit, onFailure : (FailureResponse) -> Unit) {
        callTodoService().switchTodoComplete(id)
            .enqueue(object : Callback<SuccessResponse> {
                override fun onResponse(
                    call: Call<SuccessResponse>,
                    response: Response<SuccessResponse>
                ) {
                    TODO("Not yet implemented")
                }

                override fun onFailure(call: Call<SuccessResponse>, t: Throwable) {
                    TODO("Not yet implemented")
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
    fun deleteTodo(id : Int, onResponse : (SuccessResponse) -> Unit, onErrorResponse: (ErrorResponse) -> Unit, onFailure: (FailureResponse) -> Unit) {
        callTodoService().deleteTodo(id)
            .enqueue(object : Callback<SuccessResponse> {
                override fun onResponse(
                    call: Call<SuccessResponse>,
                    response: Response<SuccessResponse>
                ) {
                    TODO("Not yet implemented")
                }

                override fun onFailure(call: Call<SuccessResponse>, t: Throwable) {
                    TODO("Not yet implemented")
                }

            })
    }

}