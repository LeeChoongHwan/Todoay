package com.todoay.api.domain.category.dto.response

import com.google.gson.annotations.SerializedName

data class CreateCategoryResponse(
    @SerializedName("id")
    val id : Int
)
