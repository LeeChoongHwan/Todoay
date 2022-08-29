package com.todoay.api.domain.todo.daily

import com.todoay.api.config.ServiceRepository.TodoServiceRepository.callDailyTodoService
import com.todoay.api.domain.todo.daily.dto.request.CreateDailyTodoRequest
import com.todoay.api.domain.todo.daily.dto.response.*
import com.todoay.api.util.response.error.ErrorResponse
import com.todoay.api.util.response.error.FailureResponse
import com.todoay.api.util.response.success.SuccessResponse
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

    /**
     * 특정 날짜의 모든 DailyTodo의 정보 리스트를 가져온다.
     *
     * @param date 정보를 가져올 DailyTodo의 날짜
     * @param onResponse
     * @param onErrorResponse
     * @param onFailure
     */
    fun readAllDailyTodo(date : LocalDate, onResponse : (List<ReadAllDailyTodoResponse>) -> Unit, onErrorResponse: (ErrorResponse) -> Unit, onFailure: (FailureResponse) -> Unit) {
        callDailyTodoService().readAllDailyTodo(date.toString())
            .enqueue(object : Callback<List<ReadAllDailyTodoResponse>> {
                override fun onResponse(
                    call: Call<List<ReadAllDailyTodoResponse>>,
                    response: Response<List<ReadAllDailyTodoResponse>>
                ) {
                    TODO("Not yet implemented")
                }

                override fun onFailure(call: Call<List<ReadAllDailyTodoResponse>>, t: Throwable) {
                    TODO("Not yet implemented")
                }

            })
    }

    /**
     * 특정 DailyTodo의 모든 정보(세부정보 포함)을 가져온다.
     *
     * @param id 정보를 가져올 DailyTodo의 id
     * @param onResponse
     * @param onErrorResponse
     * @param onFailure
     */
    fun readDailyTodo(id : Int, onResponse : (ReadDailyTodoResponse) -> Unit, onErrorResponse: (ErrorResponse) -> Unit, onFailure: (FailureResponse) -> Unit) {
        callDailyTodoService().readDailyTodo(id)
            .enqueue(object : Callback<ReadDailyTodoResponse> {
                override fun onResponse(
                    call: Call<ReadDailyTodoResponse>,
                    response: Response<ReadDailyTodoResponse>
                ) {
                    TODO("Not yet implemented")
                }

                override fun onFailure(call: Call<ReadDailyTodoResponse>, t: Throwable) {
                    TODO("Not yet implemented")
                }

            })
    }

    /**
     * 특정 DailyTodo의 반복 상태를 설정한다.
     *
     * @param id 반복상태를 설정할 DailyTodo의 id
     * @param onResponse
     * @param onErrorResponse
     * @param onFailure
     */
    fun setDailyTodoRepeat(id : Int, onResponse : (SuccessResponse) -> Unit, onErrorResponse: (ErrorResponse) -> Unit, onFailure: (FailureResponse) -> Unit) {
        callDailyTodoService().setDailyTodoRepeat(id)
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
     * 특정 DailyTodo의 정보(세부정보 포함)를 수정한다.
     *
     * @param id 정보를 수정할 DailyTodo의 id
     * @param onResponse
     * @param onErrorResponse
     * @param onFailure
     */
    fun modifyDailyTodo(id : Int, onResponse : (ModifyDailyTodoResponse) -> Unit, onErrorResponse: (ErrorResponse) -> Unit, onFailure: (FailureResponse) -> Unit) {
        callDailyTodoService().modifyDailyTodo(id)
            .enqueue(object : Callback<ModifyDailyTodoResponse> {
                override fun onResponse(
                    call: Call<ModifyDailyTodoResponse>,
                    response: Response<ModifyDailyTodoResponse>
                ) {
                    TODO("Not yet implemented")
                }

                override fun onFailure(call: Call<ModifyDailyTodoResponse>, t: Throwable) {
                    TODO("Not yet implemented")
                }

            })
    }

    /**
     * 특정 DailyTodo의 날짜 정보를 수정한다.
     *
     * @param id 날짜 정보를 수정할 DailyTodo의 id
     * @param onResponse
     * @param onErrorResponse
     * @param onFailure
     */
    fun modifyDailyTodoDate(id : Int, onResponse : (SuccessResponse) -> Unit, onErrorResponse: (ErrorResponse) -> Unit, onFailure: (FailureResponse) -> Unit) {
        callDailyTodoService().modifyDailyTodoDate(id)
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

