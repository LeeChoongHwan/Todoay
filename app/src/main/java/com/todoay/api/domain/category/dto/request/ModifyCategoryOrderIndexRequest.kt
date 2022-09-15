package com.todoay.api.domain.category.dto.request

import com.google.gson.annotations.SerializedName
import com.todoay.api.domain.category.dto.CategoryOrderIndexDto

data class ModifyCategoryOrderIndexRequest(
    @SerializedName("orderIndexes")
    val orderIndexes : List<CategoryOrderIndexDto>
)
