package com.todoay.api.domain.todo.common

import com.todoay.api.util.response.success.SuccessResponse
import retrofit2.Call
import retrofit2.http.DELETE
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
    fun switchTodoFinishState(@Path("id") id : Long) : Call<SuccessResponse>

    /**
     * 투두를 삭제하는 인터페이스 메소드.
     * Http Method : DELETE
     *
     * @param id 삭제할 투두 id
     * @return Call<SuccessResponse>
     */
    @DELETE("/todo/{id}")
    fun deleteTodo(@Path("id") id : Long) : Call<SuccessResponse>

}