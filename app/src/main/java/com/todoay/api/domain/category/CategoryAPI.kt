package com.todoay.api.domain.category

import com.todoay.api.config.RetrofitService
import com.todoay.api.config.ServiceRepository.CategoryServiceRepository.callCategoryService
import com.todoay.api.domain.category.dto.request.CreateCategoryRequest
import com.todoay.api.domain.category.dto.request.ModifyCategoryOrderIndexRequest
import com.todoay.api.domain.category.dto.request.ModifyCategoryRequest
import com.todoay.api.domain.category.dto.response.CreateCategoryResponse
import com.todoay.api.domain.category.dto.response.ReadCategoryResponse
import com.todoay.api.util.response.error.ErrorResponse
import com.todoay.api.util.response.error.FailureResponse
import com.todoay.api.util.response.success.SuccessResponse
import com.todoay.global.util.PrintUtil.printLog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * 카테고리 관련 API 호출 및 응답을 처리하는 클래스.
 *
 * @see CategoryService
 */
class CategoryAPI {

    companion object {
        private var instance : CategoryAPI? = null
        fun getInstance() : CategoryAPI {
            return instance ?: synchronized(this) {
                instance ?: CategoryAPI().also {
                    instance  = it
                }
            }
        }
    }

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
                    if(response.isSuccessful) {
                        val successResponse : CreateCategoryResponse = response.body()!!
                        onResponse(successResponse)
                        printLog("[카테고리 생성] - 성공 {$successResponse}")
                    }
                    else {
                        try {
                            val errorResponse = RetrofitService.getErrorResponse(response)
                            onErrorResponse(errorResponse)
                            printLog("[카테고리 생성] - 실패 {$errorResponse}")
                        }
                        catch (t : Throwable) {
                            onFailure(call, t)
                        }
                    }
                }

                override fun onFailure(call: Call<CreateCategoryResponse>, t: Throwable) {
                    val failure = RetrofitService.getFailure(
                        t, "/category"
                    )
                    onFailure(failure)
                    printLog("SYSTEM ERROR - FAILED {$failure}")
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
                    if(response.isSuccessful) {
                        val successResponse : ReadCategoryResponse = response.body()!!
                        onResponse(successResponse)
                        printLog("[카테고리 전체 조회] - 성공 {$successResponse}")
                    }
                    else {
                        try {
                            val errorResponse = RetrofitService.getErrorResponse(response)
                            onErrorResponse(errorResponse)
                            printLog("[카테고리 전체 조회] - 실패 {$errorResponse}")
                        }
                        catch (t: Throwable) {
                            onFailure(call, t)
                        }
                    }
                }

                override fun onFailure(call: Call<ReadCategoryResponse>, t: Throwable) {
                    val failure = RetrofitService.getFailure(
                        t, "/category/my"
                    )
                    onFailure(failure)
                    printLog("SYSTEM ERROR - FAILED {$failure}")
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
    fun modifyCategory(id : Long, request : ModifyCategoryRequest, onResponse : (SuccessResponse) -> Unit, onErrorResponse: (ErrorResponse) -> Unit, onFailure: (FailureResponse) -> Unit) {
        callCategoryService().modifyCategory(id, request)
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
                        printLog("[카테고리 수정] - 성공 {$successResponse}")
                    }
                    else {
                        try {
                            val errorResponse = RetrofitService.getErrorResponse(response)
                            onErrorResponse(errorResponse)
                            printLog("[카테고리 수정] - 실패 {$errorResponse}")
                        }
                        catch (t : Throwable) {
                            onFailure(call, t)
                        }
                    }
                }

                override fun onFailure(call: Call<SuccessResponse>, t: Throwable) {
                    val failure = RetrofitService.getFailure(
                        t, "/category/{id}"
                    )
                    onFailure(failure)
                    printLog("SYSTEM ERROR - FAILED {$failure}")
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
    fun terminateCategory(id : Long, onResponse : (SuccessResponse) -> Unit, onErrorResponse: (ErrorResponse) -> Unit, onFailure: (FailureResponse) -> Unit) {
        callCategoryService().terminateCategory(id)
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
                        printLog("[카테고리 종료] - 성공 {$successResponse}")
                    }
                    else {
                        try {
                            val errorResponse = RetrofitService.getErrorResponse(response)
                            onErrorResponse(errorResponse)
                            printLog("[카테고리 종료] - 실패 {$errorResponse}")
                        }
                        catch (t : Throwable) {
                            onFailure(call, t)
                        }
                    }
                }

                override fun onFailure(call: Call<SuccessResponse>, t: Throwable) {
                    val failure = RetrofitService.getFailure(
                        t, "/category/{id}/end"
                    )
                    onFailure(failure)
                    printLog("SYSTEM ERROR - FAILED {$failure}")
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
                    if(response.isSuccessful) {
                        val successResponse = SuccessResponse(
                            status = response.code()
                        )
                        onResponse(successResponse)
                        printLog("[카테고리 순서 변경] - 성공 {$successResponse}")
                    }
                    else {
                        try {
                            val errorResponse = RetrofitService.getErrorResponse(response)
                            onErrorResponse(errorResponse)
                            printLog("[카테고리 순서 변경] - 실패 {$errorResponse}")
                        }
                        catch (t : Throwable) {
                            onFailure(call, t)
                        }
                    }
                }

                override fun onFailure(call: Call<SuccessResponse>, t: Throwable) {
                    val failure = RetrofitService.getFailure(
                        t, "/category/order-indexes"
                    )
                    onFailure(failure)
                    printLog("SYSTEM ERROR - FAILED {$failure}")
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
    fun deleteCategory(id : Long, onResponse : (SuccessResponse) -> Unit, onErrorResponse: (ErrorResponse) -> Unit, onFailure: (FailureResponse) -> Unit) {
        callCategoryService().deleteCategory(id)
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
                        printLog("[카테고리 삭제] - 성공 {$successResponse}")
                    }
                    else {
                        try {
                            val errorResponse = RetrofitService.getErrorResponse(response)
                            onErrorResponse(errorResponse)
                            printLog("[카테고리 삭제] - 실패 {$errorResponse}")
                        }
                        catch (t : Throwable) {
                            onFailure(call, t)
                        }
                    }
                }

                override fun onFailure(call: Call<SuccessResponse>, t: Throwable) {
                    val failure = RetrofitService.getFailure(
                        t, "/category/{id}"
                    )
                    onFailure(failure)
                    printLog("SYSTEM ERROR - FAILED {$failure}")
                }

            })
    }

}