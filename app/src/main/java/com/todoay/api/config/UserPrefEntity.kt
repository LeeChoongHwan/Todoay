package com.todoay.api.config

data class UserPrefEntity(
    var email:String,
    var accessToken: String,
    var refreshToken: String,
    var nickName: String
)