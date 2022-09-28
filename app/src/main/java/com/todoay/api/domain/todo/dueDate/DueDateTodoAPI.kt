package com.todoay.api.domain.todo.dueDate

import com.todoay.api.config.RetrofitService
import com.todoay.api.config.ServiceRepository.TodoServiceRepository.callDueDateTodoService
import com.todoay.api.domain.todo.dueDate.dto.request.CreateDueDateTodoRequest
import com.todoay.api.domain.todo.dueDate.dto.request.ModifyDueDateTodoRequest
import com.todoay.api.domain.todo.dueDate.dto.response.CreateDueDateTodoResponse
import com.todoay.api.domain.todo.dueDate.dto.response.ReadAllDueDateTodoResponse
import com.todoay.api.domain.todo.dueDate.dto.response.ReadDueDateTodoResponse
import com.todoay.api.util.response.error.ErrorResponse
import com.todoay.api.util.response.error.FailureResponse
import com.todoay.api.util.response.success.SuccessResponse
import com.todoay.global.util.PrintUtil.printLog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * DueDateTodo API 호출 및 응답을 처리하는 클래스.
 *
 * @see DueDateTodoService
 */
class DueDateTodoAPI {

    companion object {
        private var instance : DueDateTodoAPI? = null
        fun getInstance() : DueDateTodoAPI {
            return instance ?: synchronized(this) {
                instance ?: DueDateTodoAPI().also {
                    instance  = it
                }
            }
        }
    }

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
                    if(response.isSuccessful) {
                        val createResponse : CreateDueDateTodoResponse = response.body()!!
                        onResponse(createResponse)
                        printLog("[DueDate Todo 추가] - 성공 {$createResponse}")
                    }
                    else {
                        try {
                            val errorResponse : ErrorResponse
                            if(response.code() == 400) {
                                errorResponse = RetrofitService.getValidErrorResponse(response)
                                onErrorResponse(errorResponse)
                            }
                            else {
                                errorResponse = RetrofitService.getErrorResponse(response)
                                onErrorResponse(errorResponse)
                            }
                            printLog("[DueDate Todo 추가] - 실패 {$errorResponse}")
                        }
                        catch (t : Throwable) {
                            onFailure(call, t)
                        }
                    }
                }

                override fun onFailure(call: Call<CreateDueDateTodoResponse>, t: Throwable) {
                    val failure = RetrofitService.getFailure(
                        t, "/todo/due-date"
                    )
                    onFailure(failure)
                    printLog("SYSTEM ERROR - FAILED {$failure}")
                }

            })
    }

    /**
     * 모든 DueDateTodo의 정보를 가져온다.
     *
     * @param orderType DueDateTodo 리스트를 정렬할 타입의 String 변수
     * @param onResponse
     * @param onErrorResponse
     * @param onFailure
     */
    fun readDueDateTodoList(orderType : String, onResponse : (List<ReadAllDueDateTodoResponse>) -> Unit, onErrorResponse: (ErrorResponse) -> Unit, onFailure: (FailureResponse) -> Unit) {
        callDueDateTodoService().readDueDateTodoList(orderType)
            .enqueue(object : Callback<List<ReadAllDueDateTodoResponse>> {
                override fun onResponse(
                    call: Call<List<ReadAllDueDateTodoResponse>>,
                    response: Response<List<ReadAllDueDateTodoResponse>>
                ) {
                    if(response.isSuccessful) {
                        val responseList : List<ReadAllDueDateTodoResponse> = response.body()!!
                        onResponse(responseList)
                        printLog("[DueDateTodo 전체 조회] - 성공 {$responseList}")
                    }
                    else {
                        try {
                            val errorResponse = RetrofitService.getErrorResponse(response)
                            onErrorResponse(errorResponse)
                            printLog("[DueDateTodo 전체 조회] - 실패 {$errorResponse}")
                        }
                        catch (t : Throwable) {
                            onFailure(call, t)
                        }
                    }
                }

                override fun onFailure(call: Call<List<ReadAllDueDateTodoResponse>>, t: Throwable) {
                    val failure = RetrofitService.getFailure(
                        t, "/todo/due-date/my"
                    )
                    onFailure(failure)
                    printLog("[SYSTEM ERROR] - 실패 {${failure}}")
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
    fun readDueDateTodoInfo(id : Long, onResponse : (ReadDueDateTodoResponse) -> Unit, onErrorResponse: (ErrorResponse) -> Unit, onFailure: (FailureResponse) -> Unit) {
        callDueDateTodoService().readDueDateTodoInfo(id)
            .enqueue(object : Callback<ReadDueDateTodoResponse> {
                override fun onResponse(
                    call: Call<ReadDueDateTodoResponse>,
                    response: Response<ReadDueDateTodoResponse>
                ) {
                    if(response.isSuccessful) {
                        val successResponse = response.body()!!
                        onResponse(successResponse)
                        printLog("[DueDate Info 조회] - 성공 {$successResponse}")
                    }
                    else {
                        try {
                            val errorResponse = RetrofitService.getErrorResponse(response)
                            onErrorResponse(errorResponse)
                            printLog("[DueDate Info 조회] - 실패 {$errorResponse}")
                        }
                        catch (t : Throwable) {
                            onFailure(call, t)
                        }
                    }
                }

                override fun onFailure(call: Call<ReadDueDateTodoResponse>, t: Throwable) {
                    val failure = RetrofitService.getFailure(
                        t, "/todo/due-date/my/{id}"
                    )
                    onFailure(failure)
                    printLog("[SYSTEM ERROR] - 실패 {${failure}}")
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
    fun readFinishDueDateTodoList(onResponse : (List<ReadAllDueDateTodoResponse>) -> Unit, onErrorResponse: (ErrorResponse) -> Unit, onFailure: (FailureResponse) -> Unit) {
        callDueDateTodoService().readFinishDueDateTodoList()
            .enqueue(object : Callback<List<ReadAllDueDateTodoResponse>> {
                override fun onResponse(
                    call: Call<List<ReadAllDueDateTodoResponse>>,
                    response: Response<List<ReadAllDueDateTodoResponse>>
                ) {
                    if(response.isSuccessful) {
                        val successResponse = response.body()!!
                        onResponse(successResponse)
                        printLog("[Finish DueDateTodo 전체 조회] - 성공 {$successResponse}")
                    }
                    else {
                        try {
                            val errorResponse = RetrofitService.getErrorResponse(response)
                            onErrorResponse(errorResponse)
                            printLog("[Finish DueDateTodo 전체 조회] - 실패 {$errorResponse}")
                        }
                        catch (t : Throwable) {
                            onFailure(call, t)
                        }
                    }
                }

                override fun onFailure(call: Call<List<ReadAllDueDateTodoResponse>>, t: Throwable) {
                    val failure = RetrofitService.getFailure(
                        t, "/todo/due-date/my/finished"
                    )
                    onFailure(failure)
                    printLog("[SYSTEM ERROR] - 실패 {${failure}}")
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
    fun modifyDueDateTodo(id : Long, request : ModifyDueDateTodoRequest, onResponse : (SuccessResponse) -> Unit, onErrorResponse: (ErrorResponse) -> Unit, onFailure: (FailureResponse) -> Unit) {
        callDueDateTodoService().modifyDueDateTodo(id, request)
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
                        printLog("[DueDate Info 수정] - 성공 {$successResponse}")
                    }
                    else {
                        try {
                            val errorResponse = RetrofitService.getErrorResponse(response)
                            onErrorResponse(errorResponse)
                            printLog("[DueDate Info 수정] - 실패 {$errorResponse}")
                        }
                        catch (t : Throwable) {
                            onFailure(call, t)
                        }
                    }
                }

                override fun onFailure(call: Call<SuccessResponse>, t: Throwable) {
                    val failure = RetrofitService.getFailure(
                        t, "/todo/due-date/{id}"
                    )
                    onFailure(failure)
                    printLog("[SYSTEM ERROR] - 실패 {${failure}}")
                }

            })
    }

}