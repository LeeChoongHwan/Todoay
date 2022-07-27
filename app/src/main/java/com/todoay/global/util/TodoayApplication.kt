package com.todoay.global.util

import android.app.Application
import android.util.Log
import com.todoay.global.config.user.Pref

class TodoayApplication: Application() {
    companion object {
        lateinit var pref: Pref
    }

    override fun onCreate() {
        pref = Pref(applicationContext)

        // 테스트를 위해 자동로그인을 해제한 기능
        pref.clear()

        super.onCreate()
    }
}