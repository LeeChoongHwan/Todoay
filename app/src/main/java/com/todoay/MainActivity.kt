package com.todoay

import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.todoay.databinding.ActivityMainBinding
import com.todoay.databinding.ToastBoardBinding
import com.todoay.global.util.PrintUtil.printLog
import com.todoay.view.global.NetworkNotFoundFragment

class MainActivity : AppCompatActivity() {
    companion object {
        var mainAct: MainActivity? = null
    }

    lateinit var mainActivityBinding : ActivityMainBinding
    lateinit var toastBinding: ToastBoardBinding

    private var inputMethodManager : InputMethodManager? = null
    private var currentDestination : NavDestination? = null
    private var currentFragment : Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainAct = this
        mainActivityBinding = ActivityMainBinding.inflate(layoutInflater)
        toastBinding = ToastBoardBinding.inflate(layoutInflater)
        setContentView(mainActivityBinding.root)
        inputMethodManager = getSystemService(android.content.Context.INPUT_METHOD_SERVICE) as InputMethodManager

    }

    fun hideKeyboard(v: View) {
        inputMethodManager?.hideSoftInputFromWindow(v.windowToken, 0)
    }

    /**
     * 유저 로그아웃 및 메시지 Toast.
     * 로그아웃과 동시에 유저에게 메시지(Toast)를 보여주기 위한 메소드.
     */
    fun logout(toastMsg: String?) {
        logout()
        if(!toastMsg.isNullOrBlank()) {
            showLongToast(toastMsg)
        }
    }

    /**
     * 유저 로그아웃.
     * 유저의 토큰을 초기화하고, Login Fragment로 이동.
     */
    private fun logout() {
        printLog("[USER] 로그아웃")
        TodoayApplication.pref.clearToken()
        printLog("[USER] IsTokenEmpty: ${TodoayApplication.pref.isTokenEmpty()}")
        navigateToDestinationFromCurrent(R.id.action_global_loginFragment)
    }

    fun withdrawal() {
        printLog("[USER] 계정 삭제")
        TodoayApplication.pref.clear()
        showShortToast("계정이 삭제되었습니다")
        navigateToDestinationFromCurrent(R.id.action_global_loginFragment)
    }

    /**
     * 현재 보여지고 있는 Current Fragment에서 Destination Fragment로
     * 이동하는 메소드.
     * @param destinationId 이동하고자 하는 Fragment Destination Id
     */
    private fun navigateToDestinationFromCurrent(destinationId: Int) {
        runOnUiThread {
            val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView)
            currentDestination = navHostFragment?.findNavController()?.currentDestination
            currentFragment = navHostFragment?.childFragmentManager?.fragments?.get(0)
            if (currentFragment != null) {
                printLog("[NavigateFromCurrent] Current : ${currentFragment?.javaClass}")
                NavHostFragment.findNavController(currentFragment!!).navigate(destinationId)
            }
        }
    }

    /**
     * Toast를 보여주는 메소드.
     *
     * @param message Toast에 보여줄 메시지
     * @param duration Toast를 보여줄 지속 기간
     */
    private fun showToast(message: String, duration: Int) {
        runOnUiThread {
            Toast(applicationContext).apply {
                toastBinding.toastMessage.text = message
                this.duration = duration
                this.view = toastBinding.root
                show()
            }
        }
    }

    /**
     * Toast를 짧게 보여주는 메소드.
     * @param message Toast에 보여줄 메시지
     */
    fun showShortToast(message: String) {
        showToast(message, Toast.LENGTH_SHORT)
    }

    /**
     * Toast를 길게 보여주는 메소드.
     * @param message Toast에 보여줄 메시지
     */
    fun showLongToast(message: String) {
        showToast(message, Toast.LENGTH_LONG)
    }

}