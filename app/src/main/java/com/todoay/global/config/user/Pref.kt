package com.todoay.global.config.user

import android.content.Context
import android.content.SharedPreferences

class Pref(context: Context) {
    private val emailPref: SharedPreferences = context.getSharedPreferences("todoay_pref_email", Context.MODE_PRIVATE)
    private val emailEditor: SharedPreferences.Editor = emailPref.edit()
    private val tokenPref: SharedPreferences = context.getSharedPreferences("todoay_pref_token", Context.MODE_PRIVATE)
    private val tokenEditor: SharedPreferences.Editor = tokenPref.edit()

    fun setUser(email: String, accessToken : String, refreshToken : String) {
        emailEditor.putString("email", email)
        tokenEditor.putString("accessToken", accessToken)
        tokenEditor.putString("refreshToken", refreshToken)

        emailEditor.apply()
        tokenEditor.apply()
    }

    fun getEmail() : String {
        return emailPref.getString("email", "").toString()
    }

    fun getAccessToken() : String {
        return tokenPref.getString("accessToken", "").toString()
    }

    fun isAccessTokenEmpty() : Boolean {
        return getAccessToken() == ""
    }

    fun getRefreshToken(): String {
        return tokenPref.getString("refreshToken", "").toString()
    }

    fun isRefreshTokenEmpty() : Boolean {
        return getRefreshToken() == ""
    }

    fun isTokenEmpty() : Boolean {
        return isAccessTokenEmpty() && isRefreshTokenEmpty()
    }

    fun clearEmail() {
        emailEditor.clear()
        emailEditor.apply()
    }

    fun clearToken() {
        tokenEditor.clear()
        tokenEditor.apply()
    }

    fun clear() {
        clearEmail()
        clearToken()
    }

}