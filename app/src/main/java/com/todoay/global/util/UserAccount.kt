package com.todoay.global.util

import android.util.Log

class UserAccount {
    companion object {
        fun logout() {
            Log.d("USER ACCOUNT", "[로그아웃 실행]")
            TodoayApplication.pref.clearToken()
        }
    }
}