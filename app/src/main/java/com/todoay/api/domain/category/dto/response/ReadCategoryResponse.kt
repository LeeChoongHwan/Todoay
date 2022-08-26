package com.todoay.api.domain.category.dto.response

import com.google.gson.annotations.SerializedName
import com.todoay.api.domain.category.dto.CategoryDetailsDto

data class ReadCategoryResponse(
    @SerializedName("categories")
    val categoryList : List<CategoryDetailsDto>
)
