package com.todoay.api.domain.auth.dto.request

import com.google.gson.annotations.SerializedName

data class DeleteAuthRequest(
    @SerializedName("password")
    val password : String
)
