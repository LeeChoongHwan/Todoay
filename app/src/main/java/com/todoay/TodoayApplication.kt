package com.todoay

import android.app.Application
import com.todoay.global.config.user.Pref

class TodoayApplication: Application() {
    companion object {
        lateinit var pref: Pref
        fun showToast() {

        }
    }

    override fun onCreate() {
        pref = Pref(applicationContext)
        super.onCreate()
    }
}