package com.todoay.api.domain.category.dto

import com.google.gson.annotations.SerializedName

data class CategoryDto(
    @SerializedName("id")
    val id : Long,
    @SerializedName("name")
    val name : String,
    @SerializedName("color")
    val color : String,
    @SerializedName("orderIndex")
    val orderIndex : Int,
    @SerializedName("ended")
    val isEnded : Boolean
)
