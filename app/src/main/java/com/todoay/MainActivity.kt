package com.todoay

import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.todoay.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var mainActivityBinding : ActivityMainBinding

    var inputMethodManager : InputMethodManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainActivityBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainActivityBinding.root)

        inputMethodManager = getSystemService(android.content.Context.INPUT_METHOD_SERVICE) as
                InputMethodManager?

    }

    fun hideKeyboard(v: View) {
        if(v!=null) {
            inputMethodManager?.hideSoftInputFromWindow(v.windowToken, 0)
        }
    }

}