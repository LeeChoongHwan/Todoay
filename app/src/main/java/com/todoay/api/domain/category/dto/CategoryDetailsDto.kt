package com.todoay.api.domain.category.dto

import com.google.gson.annotations.SerializedName

data class CategoryDetailsDto(
    @SerializedName("id")
    val id : Int,
    @SerializedName("name")
    val name : String,
    @SerializedName("color")
    val color : String,
    @SerializedName("orderIndex")
    val orderIndex : Int,
    @SerializedName("ended")
    val isFinish : Boolean
)
