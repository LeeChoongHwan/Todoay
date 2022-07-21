package com.todoay.api.profile.modifyProfile

import com.google.gson.annotations.SerializedName

data class ResponseModifyProfile(
    @SerializedName("result")
    val result: String,
    @SerializedName("code")
    val code: Int,
    @SerializedName("message")
    val message: String
)
