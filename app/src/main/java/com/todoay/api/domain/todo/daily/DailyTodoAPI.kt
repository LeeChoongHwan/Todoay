package com.todoay.api.domain.todo.daily

import com.todoay.api.config.RetrofitService
import com.todoay.api.config.ServiceRepository.TodoServiceRepository.callDailyTodoService
import com.todoay.api.domain.todo.daily.dto.request.CreateDailyTodoRequest
import com.todoay.api.domain.todo.daily.dto.request.DailyRepeatRequest
import com.todoay.api.domain.todo.daily.dto.request.ModifyDailyTodoDateRequest
import com.todoay.api.domain.todo.daily.dto.request.ModifyDailyTodoRequest
import com.todoay.api.domain.todo.daily.dto.response.CreateDailyTodoResponse
import com.todoay.api.domain.todo.daily.dto.response.ReadAllDailyTodoResponse
import com.todoay.api.domain.todo.daily.dto.response.ReadDailyTodoResponse
import com.todoay.api.util.response.error.ErrorResponse
import com.todoay.api.util.response.error.FailureResponse
import com.todoay.api.util.response.success.SuccessResponse
import com.todoay.global.util.PrintUtil.printLog
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

    companion object {
        private var instance : DailyTodoAPI? = null
        fun getInstance() : DailyTodoAPI {
            return instance ?: synchronized(this) {
                instance ?: DailyTodoAPI().also {
                    instance  = it
                }
            }
        }
    }

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
                    if(response.isSuccessful) {
                        val createResponse : CreateDailyTodoResponse = response.body()!!
                        onResponse(createResponse)
                        printLog("[DailyTodo 추가] - 성공 {$createResponse}")
                    } else {
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
                            printLog("[DailyTodo 추가] - 실패 {$errorResponse}")
                        }
                        catch (t : Throwable) {
                            onFailure(call, t)
                        }
                    }
                }

                override fun onFailure(call: Call<CreateDailyTodoResponse>, t: Throwable) {
                    val failure = RetrofitService.getFailure(
                        t, "/todo/daily"
                    )
                    onFailure(failure)
                    printLog("SYSTEM ERROR - FAILED {$failure}")
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
    fun readDailyTodoList(date : LocalDate, onResponse : (List<ReadAllDailyTodoResponse>) -> Unit, onErrorResponse: (ErrorResponse) -> Unit, onFailure: (FailureResponse) -> Unit) {
        callDailyTodoService().readDailyTodoList(date.toString())
            .enqueue(object : Callback<List<ReadAllDailyTodoResponse>> {
                override fun onResponse(
                    call: Call<List<ReadAllDailyTodoResponse>>,
                    response: Response<List<ReadAllDailyTodoResponse>>
                ) {
                    if(response.isSuccessful) {
                        val responseList : List<ReadAllDailyTodoResponse> = response.body()!!
                        onResponse(responseList)
                        printLog("[DueDateTodo 전체 조회] - 성공 {$responseList}")
                    }
                    else {
                        try {
                            val errorResponse = RetrofitService.getErrorResponse(response)
                            onErrorResponse(errorResponse)
                            printLog("[DailyTodo 전체 조회] - 실패 {$errorResponse}")
                        }
                        catch (t : Throwable) {
                            onFailure(call, t)
                        }
                    }
                }

                override fun onFailure(call: Call<List<ReadAllDailyTodoResponse>>, t: Throwable) {
                    val failure = RetrofitService.getFailure(
                        t, "/todo/daily/my"
                    )
                    onFailure(failure)
                    printLog("[SYSTEM ERROR] - 실패 {${failure}}")
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
    fun readDailyTodo(id : Long, onResponse : (ReadDailyTodoResponse) -> Unit, onErrorResponse: (ErrorResponse) -> Unit, onFailure: (FailureResponse) -> Unit) {
        callDailyTodoService().readDailyTodo(id)
            .enqueue(object : Callback<ReadDailyTodoResponse> {
                override fun onResponse(
                    call: Call<ReadDailyTodoResponse>,
                    response: Response<ReadDailyTodoResponse>
                ) {
                    if(response.isSuccessful) {
                        val readDailyTodoResponse : ReadDailyTodoResponse = response.body()!!
                        onResponse(readDailyTodoResponse)
                        printLog("[DailyTodo 상세 조회] - 성공 {$readDailyTodoResponse}")
                    } else {
                        try {
                            val errorResponse = RetrofitService.getErrorResponse(response)
                            onErrorResponse(errorResponse)
                            printLog("[DailyTodo 상세 조회] - 실패 {$errorResponse}")
                        }
                        catch (t : Throwable) {
                            onFailure(call, t)
                        }
                    }
                }

                override fun onFailure(call: Call<ReadDailyTodoResponse>, t: Throwable) {
                    val failure = RetrofitService.getFailure(
                        t, "/todo/daily/my"
                    )
                    onFailure(failure)
                    printLog("[SYSTEM ERROR] - 실패 {${failure}}")
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
    fun setDailyTodoRepeat(id : Long, request : DailyRepeatRequest, onResponse : (SuccessResponse) -> Unit, onErrorResponse: (ErrorResponse) -> Unit, onFailure: (FailureResponse) -> Unit) {
        callDailyTodoService().setDailyTodoRepeat(id, request)
            .enqueue(object : Callback<SuccessResponse> {
                override fun onResponse(
                    call: Call<SuccessResponse>,
                    response: Response<SuccessResponse>
                ) {
                    if(response.isSuccessful) {
                        val successResponse = SuccessResponse(status = response.code())
                        onResponse(successResponse)
                        printLog("[Daily 반복 설정] - 성공 {$successResponse}")
                    }
                    else {
                        try {
                            val errorResponse = RetrofitService.getErrorResponse(response)
                            onErrorResponse(errorResponse)
                            printLog("[Daily 반복 설정] - 실패 {$errorResponse}")
                        }
                        catch (t : Throwable) {
                            onFailure(call, t)
                        }
                    }
                }

                override fun onFailure(call: Call<SuccessResponse>, t: Throwable) {
                    val failure = RetrofitService.getFailure(
                        t, "/todo/daily/{id}/repeat"
                    )
                    onFailure(failure)
                    printLog("[SYSTEM ERROR] - 실패 {${failure}}")
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
    fun modifyDailyTodo(id : Long, request: ModifyDailyTodoRequest, onResponse : (SuccessResponse) -> Unit, onErrorResponse: (ErrorResponse) -> Unit, onFailure: (FailureResponse) -> Unit) {
        callDailyTodoService().modifyDailyTodo(id, request)
            .enqueue(object : Callback<SuccessResponse> {
                override fun onResponse(
                    call: Call<SuccessResponse>,
                    response: Response<SuccessResponse>
                ) {
                    if(response.isSuccessful) {
                        val successResponse = SuccessResponse(status = response.code())
                        onResponse(successResponse)
                        printLog("[Daily Info 수정] - 성공 {$successResponse}")
                    }
                    else {
                        try {
                            val errorResponse = RetrofitService.getErrorResponse(response)
                            onErrorResponse(errorResponse)
                            printLog("[Daily Info 수정] - 실패 {$errorResponse}")
                        }
                        catch (t : Throwable) {
                            onFailure(call, t)
                        }
                    }
                }

                override fun onFailure(call: Call<SuccessResponse>, t: Throwable) {
                    val failure = RetrofitService.getFailure(
                        t, "/todo/daily/{id}"
                    )
                    onFailure(failure)
                    printLog("[SYSTEM ERROR] - 실패 {${failure}}")
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
    fun modifyDailyTodoDate(id : Long, request : ModifyDailyTodoDateRequest, onResponse : (SuccessResponse) -> Unit, onErrorResponse: (ErrorResponse) -> Unit, onFailure: (FailureResponse) -> Unit) {
        callDailyTodoService().modifyDailyTodoDate(id, request)
            .enqueue(object : Callback<SuccessResponse> {
                override fun onResponse(
                    call: Call<SuccessResponse>,
                    response: Response<SuccessResponse>
                ) {
                    if(response.isSuccessful) {
                        val successResponse = SuccessResponse(status = response.code())
                        onResponse(successResponse)
                        printLog("[Daily 날짜 수정] - 성공 {$successResponse}")
                    }
                    else {
                        try {
                            val errorResponse = RetrofitService.getErrorResponse(response)
                            onErrorResponse(errorResponse)
                            printLog("[Daily 날짜 수정] - 실패 {$errorResponse}")
                        }
                        catch (t : Throwable) {
                            onFailure(call, t)
                        }
                    }
                }

                override fun onFailure(call: Call<SuccessResponse>, t: Throwable) {
                    val failure = RetrofitService.getFailure(
                        t, "/todo/daily/{id}/daily-date"
                    )
                    onFailure(failure)
                    printLog("[SYSTEM ERROR] - 실패 {${failure}}")
                }

            })
    }

    /**
     * 반복 설정된 DailyTodo의 모든 데이터를 삭제.
     *
     * @param id 삭제할 DailyTodo의 id
     * @param onResponse
     * @param onErrorResponse
     * @param onFailure
     */
    fun deleteRepeatDailyTodo(id : Long, onResponse: (SuccessResponse) -> Unit, onErrorResponse: (ErrorResponse) -> Unit, onFailure: (FailureResponse) -> Unit) {
        callDailyTodoService().deleteRepeatDailyTodo(id)
            .enqueue(object : Callback<SuccessResponse> {
                override fun onResponse(
                    call: Call<SuccessResponse>,
                    response: Response<SuccessResponse>
                ) {
                    if(response.isSuccessful) {
                        val successResponse = SuccessResponse(status = response.code())
                        onResponse(successResponse)
                        printLog("[Daily 반복 그룹 삭제] -성공 {$successResponse}")
                    }
                    else {
                        try {
                            val errorResponse = RetrofitService.getErrorResponse(response)
                            onErrorResponse(errorResponse)
                            printLog("[Daily 반복 그룹 삭제] - 실패 {$errorResponse}")
                        }
                        catch (t : Throwable) {
                            onFailure(call, t)
                        }
                    }
                }

                override fun onFailure(call: Call<SuccessResponse>, t: Throwable) {
                    val failure = RetrofitService.getFailure(
                        t, "/todo/daily/{id}/repeat"
                    )
                    onFailure(failure)
                    printLog("[SYSTEM ERROR] - 실패 {${failure}}")
                }

            })
    }

}

