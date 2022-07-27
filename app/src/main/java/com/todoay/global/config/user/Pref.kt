package com.todoay.global.config.user

import android.content.Context
import android.content.SharedPreferences

class Pref(context: Context) {
    private val pref: SharedPreferences = context.getSharedPreferences("todoay_pref", Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = pref.edit()

    fun setUser(accessToken : String, refreshToken : String) {
        editor.putString("accessToken", accessToken)
        editor.putString("refreshToken", refreshToken)
        editor.apply()
    }

    fun getAccessToken() : String {
        return pref.getString("accessToken", "").toString()
    }

    fun setAccessToken(_accessToken: String) {
        editor.putString("accessToken", _accessToken)
        editor.apply()
    }

    fun getRefreshToken(): String {
        return pref.getString("refreshToken", "").toString()
    }

    fun setRefreshToken(_refreshToken: String) {
        editor.putString("refreshToken", _refreshToken)
        editor.apply()
    }

    fun clear() {
        editor.clear()
        editor.apply()
    }

}