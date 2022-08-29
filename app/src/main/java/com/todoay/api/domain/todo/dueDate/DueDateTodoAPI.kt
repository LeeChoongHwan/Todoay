package com.todoay.api.domain.todo.dueDate

import com.todoay.api.config.ServiceRepository.TodoServiceRepository.callDueDateTodoService
import com.todoay.api.domain.todo.dueDate.dto.request.CreateDueDateTodoRequest
import com.todoay.api.domain.todo.dueDate.dto.response.*
import com.todoay.api.util.response.error.ErrorResponse
import com.todoay.api.util.response.error.FailureResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * DueDateTodo API 호출 및 응답을 처리하는 클래스.
 *
 * @see DueDateTodoService
 */
class DueDateTodoAPI {

    /**
     * DueDateTodo를 생성한다.
     *
     * @param request 생성할 DueDateTodo DTO 객체
     * @param onResponse
     * @param onErrorResponse
     * @param onFailure
     */
    fun createDueDateTodo(request: CreateDueDateTodoRequest, onResponse : (CreateDueDateTodoResponse) -> Unit, onErrorResponse : (ErrorResponse) -> Unit, onFailure : (FailureResponse) -> Unit) {
        callDueDateTodoService().createDueDateTodo(request)
            .enqueue(object : Callback<CreateDueDateTodoResponse> {
                override fun onResponse(
                    call: Call<CreateDueDateTodoResponse>,
                    response: Response<CreateDueDateTodoResponse>
                ) {
                    TODO("Not yet implemented")
                }

                override fun onFailure(call: Call<CreateDueDateTodoResponse>, t: Throwable) {
                    TODO("Not yet implemented")
                }

            })
    }

    /**
     * 모든 DueDateTodo의 정보를 가져온다.
     *
     * @param order DueDateTodo 리스트를 정렬할 타입의 String 변수
     * @param onResponse
     * @param onErrorResponse
     * @param onFailure
     */
    fun readAllDueDateTodo(order : String, onResponse : (List<ReadAllDueDateTodoResponse>) -> Unit, onErrorResponse: (ErrorResponse) -> Unit, onFailure: (FailureResponse) -> Unit) {
        callDueDateTodoService().readAllDueDateTodo(order)
            .enqueue(object : Callback<List<ReadAllDueDateTodoResponse>> {
                override fun onResponse(
                    call: Call<List<ReadAllDueDateTodoResponse>>,
                    response: Response<List<ReadAllDueDateTodoResponse>>
                ) {
                    TODO("Not yet implemented")
                }

                override fun onFailure(call: Call<List<ReadAllDueDateTodoResponse>>, t: Throwable) {
                    TODO("Not yet implemented")
                }

            })
    }

    /**
     * 특정 DueDateTodo의 정보(세부정보 포함)를 가져온다.
     *
     * @param id 정보를 가져올 DueDateTodo의 id
     * @param onResponse
     * @param onErrorResponse
     * @param onFailure
     */
    fun readDueDateTodo(id : Int, onResponse : (ReadDueDateTodoResponse) -> Unit, onErrorResponse: (ErrorResponse) -> Unit, onFailure: (FailureResponse) -> Unit) {
        callDueDateTodoService().readDueDateTodo(id)
            .enqueue(object : Callback<ReadDueDateTodoResponse> {
                override fun onResponse(
                    call: Call<ReadDueDateTodoResponse>,
                    response: Response<ReadDueDateTodoResponse>
                ) {
                    TODO("Not yet implemented")
                }

                override fun onFailure(call: Call<ReadDueDateTodoResponse>, t: Throwable) {
                    TODO("Not yet implemented")
                }

            })
    }

    /**
     * 완료한 DueDateTodo의 리스트 정보를 가져온다.
     *
     * @param onResponse
     * @param onErrorResponse
     * @param onFailure
     */
    fun readFinishedDueDateTodo(onResponse : (List<ReadAllDueDateTodoResponse>) -> Unit, onErrorResponse: (ErrorResponse) -> Unit, onFailure: (FailureResponse) -> Unit) {
        callDueDateTodoService().readFinishedDueDateTodo()
            .enqueue(object : Callback<List<ReadAllDueDateTodoResponse>> {
                override fun onResponse(
                    call: Call<List<ReadAllDueDateTodoResponse>>,
                    response: Response<List<ReadAllDueDateTodoResponse>>
                ) {
                    TODO("Not yet implemented")
                }

                override fun onFailure(call: Call<List<ReadAllDueDateTodoResponse>>, t: Throwable) {
                    TODO("Not yet implemented")
                }

            })
    }

    /**
     * 특정 DueDateTodo의 정보(세부정보 포함)를 수정한다.
     *
     * @param id 정보를 수정할 DueDateTodo의 id
     * @param onResponse
     * @param onErrorResponse
     * @param onFailure
     */
    fun modifyDueDateTodo(id : Int, onResponse : (ModifyDueDateTodoResponse) -> Unit, onErrorResponse: (ErrorResponse) -> Unit, onFailure: (FailureResponse) -> Unit) {
        callDueDateTodoService().modifyDueDateTodo(id)
            .enqueue(object : Callback<ModifyDueDateTodoResponse> {
                override fun onResponse(
                    call: Call<ModifyDueDateTodoResponse>,
                    response: Response<ModifyDueDateTodoResponse>
                ) {
                    TODO("Not yet implemented")
                }

                override fun onFailure(call: Call<ModifyDueDateTodoResponse>, t: Throwable) {
                    TODO("Not yet implemented")
                }

            })
    }

    /**
     * 특정 DueDateTodo를 삭제한다.
     *
     * @param id 삭제할 DueDateTodo의 id
     * @param onResponse
     * @param onErrorResponse
     * @param onFailure
     */
    fun deleteDueDateTodo(id : Int, onResponse : (DeleteDueDateTodoResponse) -> Unit, onErrorResponse: (ErrorResponse) -> Unit, onFailure: (FailureResponse) -> Unit) {
        callDueDateTodoService().deleteDueDateTodo(id)
            .enqueue(object : Callback<DeleteDueDateTodoResponse> {
                override fun onResponse(
                    call: Call<DeleteDueDateTodoResponse>,
                    response: Response<DeleteDueDateTodoResponse>
                ) {
                    TODO("Not yet implemented")
                }

                override fun onFailure(call: Call<DeleteDueDateTodoResponse>, t: Throwable) {
                    TODO("Not yet implemented")
                }

            })
    }


}