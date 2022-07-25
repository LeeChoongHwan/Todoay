package com.todoay.global.config.user

data class UserPrefEntity(
    var email:String,
    var accessToken: String,
    var refreshToken: String,
    var nickName: String
)