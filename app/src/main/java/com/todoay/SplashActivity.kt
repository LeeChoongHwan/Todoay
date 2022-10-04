package com.todoay

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.todoay.api.domain.profile.ProfileAPI
import com.todoay.databinding.ActivitySplashBinding
import kotlin.concurrent.thread

class SplashActivity : AppCompatActivity(){

    lateinit var binding : ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        TodoayApplication.pref.clear()

        Handler().postDelayed({
            autoLogin()
        }, 1500)
    }

    private fun autoLogin() {
        val mainIntent = Intent(applicationContext, MainActivity::class.java)
        if(TodoayApplication.pref.hasAccessToken()) {
            val profileService = ProfileAPI.getInstance()
            profileService.getProfile(
                onResponse = {
                    autoLoginSuccess(mainIntent)
                },
                onErrorResponse = {
                    autoLoginFailed(mainIntent)
                },
                onFailure = {
                    autoLoginFailed(mainIntent)
                }
            )
        } else {
            autoLoginFailed(mainIntent)
        }
    }

    private fun autoLoginSuccess(intent : Intent) {
        intent.putExtra("auto-login", true)
        startActivity(intent)
        finish()
    }

    private fun autoLoginFailed(intent : Intent) {
        intent.putExtra("auto-login", false)
        startActivity(intent)
        finish()
    }

}