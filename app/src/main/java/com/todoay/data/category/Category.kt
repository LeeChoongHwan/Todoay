package com.todoay.data.category

data class Category(
    val id : Long,
    var name : String,
    var color : String,
    var orderIndex : Int = -1,
    var isEnded : Boolean = false
)
