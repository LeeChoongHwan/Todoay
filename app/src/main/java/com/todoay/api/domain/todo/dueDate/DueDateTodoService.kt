package com.todoay.api.domain.todo.dueDate

import com.todoay.api.domain.todo.dueDate.dto.request.CreateDueDateTodoRequest
import com.todoay.api.domain.todo.dueDate.dto.request.ModifyDueDateTodoRequest
import com.todoay.api.domain.todo.dueDate.dto.response.*
import com.todoay.api.util.response.success.SuccessResponse
import retrofit2.Call
import retrofit2.http.*

/**
 * DueDateTodo 관련 API 호출 인터페이스.
 */
interface DueDateTodoService {

    /**
     * DueDateTodo를 생성하는 인터페이스 메소드.
     * Http Method : POST
     */
    @POST("/todo/due-date")
    fun createDueDateTodo(@Body request : CreateDueDateTodoRequest) : Call<CreateDueDateTodoResponse>

    /**
     * 모든 DueDateTodo의 정보 리스트를 가져오는 인터페이스 메소드.
     * Http Method : GET
     */
    @GET("/todo/due-date/my")
    fun readDueDateTodoList(@Query("order") order : String) : Call<List<ReadAllDueDateTodoResponse>>

    /**
     * 특정 DueDateTodo의 정보를 가져오는 인터페이스 메소드.
     * Http Method : GET
     */
    @GET("/todo/due-date/my/{id}")
    fun readDueDateTodoInfo(@Path("id") id: Long) : Call<ReadDueDateTodoResponse>

    /**
     * 완료한 DueDateTodo의 정보 리스트를 가져오는 인터페이스 메소드.
     * Http Method : GET
     */
    @GET("/todo/due-date/my/finished")
    fun readFinishDueDateTodoList() : Call<List<ReadAllDueDateTodoResponse>>

    /**
     * DueDateTodo의 정보를 수정하는 인터페이스 메소드.
     * Http Method : PUT
     */
    @PUT("/todo/due-date/{id}")
    fun modifyDueDateTodo(@Path("id") id: Long, @Body request : ModifyDueDateTodoRequest) : Call<SuccessResponse>

}