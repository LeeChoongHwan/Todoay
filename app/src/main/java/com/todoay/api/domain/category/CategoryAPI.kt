package com.todoay.api.domain.category

import com.todoay.api.config.ServiceRepository.CategoryServiceRepository.callCategoryService
import com.todoay.api.domain.category.dto.request.CreateCategoryRequest
import com.todoay.api.domain.category.dto.request.ModifyCategoryOrderIndexRequest
import com.todoay.api.domain.category.dto.response.CreateCategoryResponse
import com.todoay.api.domain.category.dto.response.ReadCategoryResponse
import com.todoay.api.util.response.error.ErrorResponse
import com.todoay.api.util.response.error.FailureResponse
import com.todoay.api.util.response.success.SuccessResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * 카테고리 관련 API 호출 및 응답을 처리하는 클래스.
 *
 * @see CategoryService
 */
class CategoryAPI {

    /**
     * 카테고리를 생성한다.
     *
     * @param request 생성할 카테고리 DTO 객체
     * @param onResponse
     * @param onErrorResponse
     * @param onFailure
     */
    fun createCategory(request : CreateCategoryRequest, onResponse : (CreateCategoryResponse) -> Unit, onErrorResponse : (ErrorResponse) -> Unit, onFailure : (FailureResponse) -> Unit) {
        callCategoryService().createCategory(request)
            .enqueue(object : Callback<CreateCategoryResponse> {
                override fun onResponse(
                    call: Call<CreateCategoryResponse>,
                    response: Response<CreateCategoryResponse>
                ) {
                    TODO("Not yet implemented")
                }

                override fun onFailure(call: Call<CreateCategoryResponse>, t: Throwable) {
                    TODO("Not yet implemented")
                }

            })
    }

    /**
     * 카테고리의 정보를 가져온다.
     *
     * @param onResponse
     * @param onErrorResponse
     * @param onFailure
     */
    fun readCategory(onResponse : (ReadCategoryResponse) -> Unit, onErrorResponse: (ErrorResponse) -> Unit, onFailure: (FailureResponse) -> Unit) {
        callCategoryService().readCategory()
            .enqueue(object : Callback<ReadCategoryResponse> {
                override fun onResponse(
                    call: Call<ReadCategoryResponse>,
                    response: Response<ReadCategoryResponse>
                ) {
                    TODO("Not yet implemented")
                }

                override fun onFailure(call: Call<ReadCategoryResponse>, t: Throwable) {
                    TODO("Not yet implemented")
                }

            })
    }

    /**
     * 특정 카테고리의 정보를 수정한다.
     *
     * @param id 정보를 수정할 카테고리 id
     * @param onResponse
     * @param onErrorResponse
     * @param onFailure
     */
    fun modifyCategory(id : Int, onResponse : (SuccessResponse) -> Unit, onErrorResponse: (ErrorResponse) -> Unit, onFailure: (FailureResponse) -> Unit) {
        callCategoryService().modifyCategory(id)
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
     * 특정 카테고리를 종료한다.
     *
     * @param id 종료할 카테고리 id
     * @param onResponse
     * @param onErrorResponse
     * @param onFailure
     */
    fun terminateCategory(id : Int, onResponse : (SuccessResponse) -> Unit, onErrorResponse: (ErrorResponse) -> Unit, onFailure: (FailureResponse) -> Unit) {
        callCategoryService().terminateCategory(id)
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
     * 카테고리의 정렬 순서를 변경한다.
     *
     * @param request 카테고리 정렬 순서를 변경할 DTO 객체
     * @param onResponse
     * @param onErrorResponse
     * @param onFailure
     */
    fun modifyCategoryOrderIndex(request : ModifyCategoryOrderIndexRequest, onResponse : (SuccessResponse) -> Unit, onErrorResponse: (ErrorResponse) -> Unit,onFailure: (FailureResponse) -> Unit) {
        callCategoryService().modifyCategoryOrderIndex(request)
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
     * 특정 카테고리를 삭제한다.
     *
     * @param id 삭제할 카테고리 id
     * @param onResponse
     * @param onErrorResponse
     * @param onFailure
     */
    fun deleteCategory(id : Int, onResponse : (SuccessResponse) -> Unit, onErrorResponse: (ErrorResponse) -> Unit, onFailure: (FailureResponse) -> Unit) {
        callCategoryService().deleteCategory(id)
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