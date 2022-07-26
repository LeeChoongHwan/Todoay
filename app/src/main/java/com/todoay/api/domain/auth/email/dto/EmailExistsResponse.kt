package com.todoay.api.domain.auth.email.dto

import com.google.gson.annotations.SerializedName

data class EmailExistsResponse(
    @SerializedName("status")
    val status: Int,
    @SerializedName("emailExists")
    val emailExists : Boolean
)
