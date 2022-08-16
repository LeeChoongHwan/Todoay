package com.todoay

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.postDelayed
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.todoay.databinding.ActivityMainBinding
import com.todoay.global.RefreshTokenExpiredException
import com.todoay.view.login.LoginFragment
import java.net.Inet4Address
import java.nio.channels.InterruptedByTimeoutException

class MainActivity : AppCompatActivity() {

    lateinit var mainActivityBinding : ActivityMainBinding

    var inputMethodManager : InputMethodManager? = null
    var currentFragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainActivityBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainActivityBinding.root)

        inputMethodManager = getSystemService(android.content.Context.INPUT_METHOD_SERVICE) as
                InputMethodManager?


//        /**
//         * 네트워크 연결 확인
//         */
//        val connection = NetworkConnection(applicationContext)
//        connection.observe(this, Observer { isConnected ->
//            if(isConnected) {
//
//            }
//            else {
//
//            }
//        })

    }

    fun hideKeyboard(v: View) {
        if(v!=null) {
            inputMethodManager?.hideSoftInputFromWindow(v.windowToken, 0)
        }
    }



    fun restartWhenLogout() {

    }

}