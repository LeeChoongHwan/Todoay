package com.todoay

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.PersistableBundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.todoay.api.domain.profile.ProfileAPI
import com.todoay.databinding.ActivitySplashBinding
import com.todoay.databinding.ToastBoardBinding
import kotlin.concurrent.thread

class SplashActivity : AppCompatActivity(){

    companion object{
        var splashAct : SplashActivity? = null
    }
    lateinit var binding : ActivitySplashBinding

    private lateinit var toastBinding: ToastBoardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        splashAct = this

        binding = ActivitySplashBinding.inflate(layoutInflater)
        toastBinding = ToastBoardBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

    fun showToast(message : String) {
        runOnUiThread {
            Toast(applicationContext).apply {
                toastBinding.toastMessage.text = message
                this.duration = Toast.LENGTH_LONG
                this.view = toastBinding.root
                show()
            }
        }
    }

}