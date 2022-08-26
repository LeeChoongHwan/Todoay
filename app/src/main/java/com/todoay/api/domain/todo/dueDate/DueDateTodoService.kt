package com.todoay.api.domain.todo.dueDate

import com.todoay.api.domain.todo.dueDate.dto.request.CreateDueDateTodoRequest
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
     * 모든 DueDateTodo의 정보를 가져오는 인터페이스 메소드.
     * Http Method : GET
     */
    @GET("/todo/due-date/my")
    fun readAllDueDateTodo(@Query("order") order : String) : Call<ReadAllDueDateTodoResponse>

    /**
     * 특정 DueDateTodo의 정보를 가져오는 인터페이스 메소드.
     * Http Method : GET
     */
    @GET("/todo/due-date/my/{id}")
    fun readDueDateTodo(@Path("id") id: Int) : Call<ReadDueDateTodoResponse>

    /**
     * 완료한 DueDateTodo의 정보를 가져오는 인터페이스 메소드.
     * Http Method : GET
     */
    @GET("/todo/due-date/my/finished")
    fun readFinishedDueDateTodo() : Call<ReadAllDueDateTodoResponse>

    /**
     * DueDateTodo의 정보를 수정하는 인터페이스 메소드.
     * Http Method : PUT
     */
    @PUT("/todo/due-date/{id}")
    fun modifyDueDateTodo(@Path("id") id: Int) : Call<ModifyDueDateTodoResponse>

    /**
     * DueDateTodo를 삭제하는 인터페이스 메소드.
     * Http Method : DELETE
     */
    @DELETE("/todo/due-date/{id}")
    fun deleteDueDateTodo(@Path("id") id: Int) : Call<DeleteDueDateTodoResponse>

}