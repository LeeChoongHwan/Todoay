package com.todoay.api.domain.category.dto.request

import com.google.gson.annotations.SerializedName

data class ModifyCategoryOrderIndexRequest(
    @SerializedName("orderIndexes")
    val orderIndexList : List<CategoryOrderIndexDto>
) {
    data class CategoryOrderIndexDto(
        @SerializedName("id")
        val id : Int,
        @SerializedName("orderIndex")
        val orderIndex : Int
    )
}
