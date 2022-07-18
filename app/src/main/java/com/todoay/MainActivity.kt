package com.todoay

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.todoay.FragmentControllerName.Companion.CONTROLLER_FIND_PASSWORD
import com.todoay.FragmentControllerName.Companion.CONTROLLER_SIGN_UP
import com.todoay.FragmentControllerName.Companion.CONTROLLER_LOGIN
import com.todoay.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var mainActivityBinding : ActivityMainBinding
    lateinit var currentFragment : Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainActivityBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainActivityBinding.root)

//        fragmentController(CONTROLLER_LOGIN, false)
    }

//    fun fragmentController(name: String, addToBackStack: Boolean) {
//        when(name) {
//            CONTROLLER_LOGIN -> {
//                currentFragment = LoginFragment()
//            }
//            CONTROLLER_SIGN_UP -> {
//                currentFragment = SignUpFragment()
//            }
//            CONTROLLER_FIND_PASSWORD -> {
//                currentFragment = FindPasswordFragment()
//            }
//        }
//
//        val trans = supportFragmentManager.beginTransaction()
//        trans.replace(R.id.main_container, currentFragment)
//
//        if(addToBackStack) {
//            trans.addToBackStack(name)
//        }
//
//        trans.commit()
//    }


}