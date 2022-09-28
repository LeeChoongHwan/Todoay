package com.todoay.api.domain.hashtag

import com.todoay.api.domain.hashtag.dto.response.HashtagAutoResponse
import com.todoay.api.domain.hashtag.dto.response.HashtagResponse
import io.reactivex.rxjava3.core.Observable
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * 해시태그 관련 API 호출 인터페이스.
 */
interface HashtagService {

    /**
     * 해시태그를 검색하는 인터페이스 메소드.
     * 다음 페이지가 존재하면 다음 페이지를 검색한다.
     * Http Method : GET
     */
    @GET("/hashtag")
    fun getHashtag(@Query("name") name : String, @Query("pageNum") pageNum : Int, @Query("quantity") quantity : Int) : Call<HashtagResponse>

    /**
     * 해시태그를 자동검색하는 인터페이스 메소드.
     * Http Method : GET
     */
    @GET("/hashtag/auto")
    fun getHashtagAuto(@Query("name") name : String) : Call<HashtagAutoResponse>

}