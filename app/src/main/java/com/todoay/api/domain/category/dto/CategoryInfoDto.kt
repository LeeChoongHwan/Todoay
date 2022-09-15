package com.todoay.api.domain.category.dto

import com.google.gson.annotations.SerializedName

data class CategoryInfoDto(
    @SerializedName("id")
    val id : Long,
    @SerializedName("name")
    val name : String,
    @SerializedName("color")
    val color : String
)
