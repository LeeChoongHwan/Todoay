package com.todoay.global.util

import android.util.Log
import androidx.fragment.app.Fragment
import com.todoay.view.global.NetworkNotFoundFragment

class Utils {
    companion object {
        const val TAG = "TODOAY_LOG"
        fun printLog(log: String) {
            Log.d(TAG, log)
        }
        fun printLogView(view: Fragment) {
            Log.d(TAG, "[VIEW CREATE] ${view.javaClass.simpleName}")
        }
    }
}