package com.todoay.api.domain.category.dto.response

import com.google.gson.annotations.SerializedName
import com.todoay.api.domain.category.dto.CategoryDto

data class ReadCategoryResponse(
    @SerializedName("categories")
    val categoryList : List<CategoryDto>
)
