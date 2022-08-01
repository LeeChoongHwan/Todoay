package com.todoay.global.util

import android.app.Application
import com.todoay.global.config.user.Pref

class TodoayApplication: Application() {
    companion object {
        lateinit var pref: Pref
    }

    override fun onCreate() {
        pref = Pref(applicationContext)
//        pref.clearToken()
        super.onCreate()
    }
}