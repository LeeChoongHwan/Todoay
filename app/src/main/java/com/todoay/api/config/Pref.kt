package com.todoay.api.config

import android.content.Context
import android.content.SharedPreferences

class Pref(context: Context) {
    private val pref: SharedPreferences = context.getSharedPreferences("todoay_pref", Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = pref.edit()

    fun setUser(user: UserPrefEntity) {
        editor.putString("email", user.email)
        editor.putString("accessToken", user.accessToken)
        editor.putString("refreshToken", user.refreshToken)
        editor.putString("nickName", user.nickName)
        editor.apply()
    }

    fun getEmail() : String {
        return pref.getString("email", "").toString()
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

    fun getNickName() : String {
        return pref.getString("nickName", "").toString()
    }

    fun setNickName(_nickName: String) {
        editor.putString("nickName", _nickName)
        editor.apply()
    }

}