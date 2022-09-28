package com.todoay.api.util.response.success

import com.google.gson.annotations.SerializedName

data class SuccessResponse(
    @SerializedName("code")
    val status : Int
)
