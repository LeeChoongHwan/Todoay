package com.todoay

import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.todoay.databinding.ActivityMainBinding
import com.todoay.global.config.network.NetworkConnection

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

        /**
         * 네트워크 연결 확인
         */
        val connection = NetworkConnection(applicationContext)
        connection.observe(this, Observer { isConnected ->
            if(isConnected) {

            }
            else {

            }
        })

    }

    fun hideKeyboard(v: View) {
        if(v!=null) {
            inputMethodManager?.hideSoftInputFromWindow(v.windowToken, 0)
        }
    }

}