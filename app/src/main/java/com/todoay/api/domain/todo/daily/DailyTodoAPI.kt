package com.todoay.api.domain.todo.daily

import com.todoay.api.config.ServiceRepository.TodoServiceRepository.callDailyTodoService
import com.todoay.api.domain.todo.daily.dto.request.CreateDailyTodoRequest
import com.todoay.api.domain.todo.daily.dto.response.CreateDailyTodoResponse
import com.todoay.api.util.response.error.ErrorResponse
import com.todoay.api.util.response.error.FailureResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate

/**
 * DailyTodo API 호출 및 응답을 처리하는 클래스.
 *
 * @see DailyTodoService
 */
class DailyTodoAPI {

    /**
     * DailyTodo 생성
     *
     * @param request 생성할 DailyTodo Dto 객체
     * @param onResponse
     * @param onErrorResponse
     * @param onFailure
     */
    fun createDailyTodo(request : CreateDailyTodoRequest, onResponse : (CreateDailyTodoResponse) -> Unit, onErrorResponse : (ErrorResponse) -> Unit, onFailure : (FailureResponse) -> Unit) {
        callDailyTodoService().createDailyTodo(request)
            .enqueue(object : Callback<CreateDailyTodoResponse> {
                override fun onResponse(
                    call: Call<CreateDailyTodoResponse>,
                    response: Response<CreateDailyTodoResponse>
                ) {
                    TODO("Not yet implemented")
                }

                override fun onFailure(call: Call<CreateDailyTodoResponse>, t: Throwable) {
                    TODO("Not yet implemented")
                }

            })
    }

    fun readAllDailyTodo(date : LocalDate) {

    }


}

