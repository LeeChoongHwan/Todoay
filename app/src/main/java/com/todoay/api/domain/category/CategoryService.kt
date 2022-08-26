package com.todoay.api.domain.category

import com.todoay.api.domain.category.dto.request.CreateCategoryRequest
import com.todoay.api.domain.category.dto.request.ModifyCategoryOrderIndexRequest
import com.todoay.api.domain.category.dto.response.CreateCategoryResponse
import com.todoay.api.domain.category.dto.response.ReadCategoryResponse
import com.todoay.api.util.response.success.SuccessResponse
import retrofit2.Call
import retrofit2.http.*

/**
 * 유저의 카테고리 관련 API 호출 인터페이스.
 */
interface CategoryService {

    /**
     * 카테고리를 생성하는 인터페이스 메소드.
     * Http Method : POST
     */
    @POST("/category")
    fun createCategory(@Body request : CreateCategoryRequest) : Call<CreateCategoryResponse>

    /**
     * 카테고리의 정보를 가져오는 인터페이스 메소드.
     * Http Method : GET
     */
    @GET("/category/my")
    fun readCategory() : Call<ReadCategoryResponse>

    /**
     * 카테고리의 정보를 수정하는 인터페이스 메소드.
     * Http Method : PUT
     */
    @PUT("/category/{id}")
    fun modifyCategory(@Path("id") id : Int) : Call<SuccessResponse>

    /**
     * 카테고리를 종료하는 인터페이스 메소드.
     * Http Method : PATCH
     */
    @PATCH("/category/{id}/end")
    fun terminateCategory(@Path("id") id: Int) : Call<SuccessResponse>

    /**
     * 카테고리의 순서를 변경하는 인터페이스 메소드.
     * Http Method : PATCH
     */
    @PATCH("/category/order-indexes")
    fun modifyCategoryOrderIndex(@Body request : ModifyCategoryOrderIndexRequest) : Call<SuccessResponse>

    /**
     * 카테고리를 삭제하는 인터페이스 메소드.
     * Http Method : DELETE
     */
    @DELETE("/category/{id}")
    fun deleteCategory(@Path("id") id: Int) : Call<SuccessResponse>

}