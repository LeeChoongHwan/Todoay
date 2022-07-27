package com.todoay.api.domain.profile.dto.response

import com.google.gson.annotations.SerializedName
import com.todoay.api.util.response.basic.BasicResponse

data class ProfileResponse(
    @SerializedName("status")
    override val status: Int,
    @SerializedName("email")
    val email: String,
    @SerializedName("nickname")
    val nickname: String,
    @SerializedName("imageUrl")
    val imageUrl: String,
    @SerializedName("introMsg")
    val introMsg: String
): BasicResponse
