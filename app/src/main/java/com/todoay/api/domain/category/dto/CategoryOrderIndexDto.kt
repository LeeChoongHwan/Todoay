package com.todoay.api.domain.category.dto

import com.google.gson.annotations.SerializedName

data class CategoryOrderIndexDto(
    @SerializedName("id")
    val id : Long,
    @SerializedName("orderIndex")
    val orderIndex : Int
)
