package com.todoay.api.domain.hashtag

import com.todoay.api.config.RetrofitService
import com.todoay.api.config.ServiceRepository.HashtagServiceRepository.callHashtagService
import com.todoay.api.domain.hashtag.dto.response.HashtagAutoResponse
import com.todoay.api.domain.hashtag.dto.response.HashtagResponse
import com.todoay.api.util.response.error.ErrorResponse
import com.todoay.api.util.response.error.FailureResponse
import com.todoay.global.util.Utils.Companion.printLog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * 해시태그 관련 API 호출 및 응답을 처리하는 클래스.
 *
 * @see HashtagService
 */
class HashtagAPI {

    /**
     * 해시태그를 검색한다.
     * 다음 페이지가 존재하면 다음 페이지를 검색한다.
     *
     * @param name 검색할 해시태그 String 변수
     * @param pageNum 다음 페이지 int 변수
     * @param onResponse
     * @param onErrorResponse
     * @param onFailure
     */
    fun getHashtag(name : String, pageNum : Int, onResponse : (HashtagResponse) -> Unit, onErrorResponse : (ErrorResponse) -> Unit, onFailure : (FailureResponse) -> Unit) {
        callHashtagService().getHashtag(name, pageNum)
            .enqueue(object : Callback<HashtagResponse> {
                override fun onResponse(
                    call: Call<HashtagResponse>,
                    response: Response<HashtagResponse>
                ) {
                    TODO("Not yet implemented")
                }

                override fun onFailure(call: Call<HashtagResponse>, t: Throwable) {
                    TODO("Not yet implemented")
                }

            })
    }

    /**
     * 해시태그를 자동검색한다.
     *
     * @param name 검색할 해시태그 String 변수
     * @param onResponse
     * @param onErrorResponse
     * @param onFailure
     */
    fun getHashtagAuto(name : String, onResponse : (HashtagAutoResponse) -> Unit, onErrorResponse: (ErrorResponse) -> Unit, onFailure: (FailureResponse) -> Unit) {
        callHashtagService().getHashtagAuto(name)
            .enqueue(object : Callback<HashtagAutoResponse> {
                override fun onResponse(
                    call: Call<HashtagAutoResponse>,
                    response: Response<HashtagAutoResponse>
                ) {
                    if(response.isSuccessful) {
                        val hashtagAutoResponse : HashtagAutoResponse = response.body()!!
                        onResponse(hashtagAutoResponse)
                        printLog("[Hashtag Auto Search] - 성공 {$hashtagAutoResponse}")
                    }
                    else {
                        try {
                            val errorResponse = RetrofitService.getErrorResponse(response)
                            onErrorResponse(errorResponse)
                            printLog("[Hashtag Auto Search] - 실패 {$errorResponse}")
                        }
                        catch (t : Throwable) {
                            onFailure(call, t)
                        }
                    }
                }

                override fun onFailure(call: Call<HashtagAutoResponse>, t: Throwable) {
                    val failure = RetrofitService.getFailure(
                        t, "/hashtag/auto"
                    )
                    onFailure(failure)
                    printLog("SYSTEM ERROR - FAILED {$failure}")
                }
            })
    }


}