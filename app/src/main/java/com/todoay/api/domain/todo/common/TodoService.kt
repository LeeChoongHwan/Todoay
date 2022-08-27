package com.todoay.api.domain.todo.common

import com.todoay.api.util.response.success.SuccessResponse
import retrofit2.Call
import retrofit2.http.PATCH
import retrofit2.http.Path

/**
 * DailyTodo 및 DueDateTodo 공통 관련 API 호출 인터페이스.
 */
interface TodoService {

    /**
     * 투두의 완료 상태를 바꾸는 인터페이스 메소드.
     * Http Method : PATCH
     *
     * @return Call<SuccessResponse>
     */
    @PATCH("/todo/{id}/switch")
    fun switchTodoComplete(@Path("id") id : Int) : Call<SuccessResponse>

}