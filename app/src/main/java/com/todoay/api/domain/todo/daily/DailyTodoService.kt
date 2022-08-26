package com.todoay.api.domain.todo.daily

import com.todoay.api.domain.todo.daily.dto.request.CreateDailyTodoRequest
import com.todoay.api.domain.todo.daily.dto.response.*
import com.todoay.api.util.response.success.SuccessResponse
import retrofit2.Call
import retrofit2.http.*

/**
 * DailyTodo 관련 API 호출 인터페이스.
 */
interface DailyTodoService {

    /**
     * DailyTodo를 생성하는 인터페이스 메소드.
     * Http Method: POST
     */
    @POST("/todo/daily")
    fun createDailyTodo(@Body request: CreateDailyTodoRequest) : Call<CreateDailyTodoResponse>

    /**
     * 특정 날짜의 모든 DailyTodo의 정보를 가져오는 인터페이스 메소드.
     * Http Method: GET
     */
    @GET("/todo/daily/my")
    fun readAllDailyTodo(@Query("localDate") date : String) : Call<ReadAllDailyTodoResponse>

    /**
     * 특정 DailyTodo의 정보를 가져오는 인터페이스 메소드.
     * Http Method: GET
     */
    @GET("/todo/daily/my/{id}")
    fun readDailyTodo(@Path("id") id: Int) : Call<ReadDailyTodoResponse>

    /**
     * DailyTodo의 반복 상태를 설정하는 인터페이스 메소드.
     * Http Method: POST
     */
    @POST("/todo/daily/{id}/repeat")
    fun setDailyTodoRepeat(@Path("id") id : Int) : Call<SuccessResponse>

    /**
     * DailyTodo의 정보를 수정하는 인터페이스 메소드.
     * Http Method: PUT
     */
    @PUT("/todo/daily/{id}")
    fun modifyDailyTodo(@Path("id") id : Int) : Call<ModifyDailyTodoResponse>

    /**
     * DailyTodo의 날짜 정보를 수정하는 인터페이스 메소드.
     * Http Method: PATCH
     */
    @PATCH("/todo/daily/{id}/daily-date")
    fun modifyDailyTodoDate(@Path("id") id : Int) : Call<SuccessResponse>

    /**
     * DailyTodo를 삭제하는 인터페이스 메소드.
     * Http Method: DELETE
     */
    @DELETE("/todo/daily/{id}")
    fun deleteDailyTodo(@Path("id") id : Int) : Call<DeleteDailyTodoResponse>


}