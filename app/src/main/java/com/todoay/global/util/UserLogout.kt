package com.todoay.global.util

import com.todoay.api.config.RetrofitService

class UserLogout {
    companion object {
        fun logout() {
            TodoayApplication.pref.clearToken()
            RetrofitService.refresh()
        }
    }
}