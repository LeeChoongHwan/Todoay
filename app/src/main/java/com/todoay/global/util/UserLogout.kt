package com.todoay.global.util

class UserLogout {
    companion object {
        fun logout() {
            TodoayApplication.pref.clearToken()
        }
    }
}