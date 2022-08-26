package com.todoay.api.domain.category.dto.request

import com.google.gson.annotations.SerializedName

data class ModifyCategoryRequest(
    @SerializedName("name")
    val name : String,
    @SerializedName("color")
    val color : String
)
