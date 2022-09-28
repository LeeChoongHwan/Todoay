package com.todoay.global.config.user

import android.content.Context
import android.content.SharedPreferences

/**
 * 유저의 계정 관련 데이터 디바이스 저장소.
 * context.getSharedPreferences()를 이용한 저장소 생성 및 접근.
 * @param context : Application Context
 */
class Pref(context: Context) {

    /* 유저 이메일 저장소 */
    private val emailPref: SharedPreferences = context.getSharedPreferences("todoay_pref_email", Context.MODE_PRIVATE)
    private val emailEditor: SharedPreferences.Editor = emailPref.edit()

    /* 유저 토큰 저장소 */
    private val tokenPref: SharedPreferences = context.getSharedPreferences("todoay_pref_token", Context.MODE_PRIVATE)
    private val tokenEditor: SharedPreferences.Editor = tokenPref.edit()

    /**
     * 로그인에 따른 저장소에 유저 계정 관련 데이터 저장.
     * @param email : 유저 이메일
     * @param accessToken : 유저 access token
     * @param refreshToken : 유저 refresh token
     */
    fun setUser(email: String, accessToken : String, refreshToken : String) {
        emailEditor.putString("email", email)
        tokenEditor.putString("accessToken", accessToken)
        tokenEditor.putString("refreshToken", refreshToken)

        emailEditor.apply()
        tokenEditor.apply()
    }

    /**
     * 유저 email 리턴.
     * @return 유저 email
     *          (null: "")
     */
    fun getEmail() : String {
        return emailPref.getString("email", "").toString()
    }

    /**
     * 유저 email이 비어있는지에 대한 여부 리턴.
     * @return
     */
    fun isEmailEmpty() : Boolean {
        return getEmail() == ""
    }

    /**
     * 유저 email을 가지고 있는지에 대한 여부 리턴.
     * @return
     */
    fun hasEmail() : Boolean {
        return !isEmailEmpty()
    }

    /**
     * 유저 access token 리턴.
     * @return 유저 access token
     *          (null: "")
     */
    fun getAccessToken() : String {
        return tokenPref.getString("accessToken", "").toString()
    }

    /**
     * 유저 access token이 비어있는지에 대한 여부 리턴.
     * @return
     */
    fun isAccessTokenEmpty() : Boolean {
        return getAccessToken() == ""
    }

    /**
     * 유저 access token을 가지고 있는지에 대한 여부 리턴.
     * @return
     */
    fun hasAccessToken() : Boolean {
        return !isAccessTokenEmpty()
    }

    /**
     * 유저 refresh token 리턴.
     * @return 유저 refresh token
     *          (null: "")
     */
    fun getRefreshToken(): String {
        return tokenPref.getString("refreshToken", "").toString()
    }

    /**
     * 유저 refresh token이 비어있는지에 대한 여부 리턴.
     * @return
     */
    fun isRefreshTokenEmpty() : Boolean {
        return getRefreshToken() == ""
    }

    /**
     * 유저 refresh token을 가지고 있는지에 대한 여부 리턴.
     * @return
     */
    fun hasRefreshToken() : Boolean {
        return !isRefreshTokenEmpty()
    }

    /**
     * 유저의 access token, refresh token을 가지고 있는지에 대한 여부.
     * @return
     */
    fun isTokenEmpty() : Boolean {
        return isAccessTokenEmpty() && isRefreshTokenEmpty()
    }

    /**
     * 유저의 email 저장소를 초기화하는 메소드.
     */
    fun clearEmail() {
        emailEditor.clear()
        emailEditor.apply()
    }

    /**
     * 유저의 token 저장소를 초기화하는 메소드.
     */
    fun clearToken() {
        tokenEditor.clear()
        tokenEditor.apply()
    }

    /**
     * 유저의 email, token 저장소를 초기화하는 메소드.
     */
    fun clear() {
        clearEmail()
        clearToken()
    }

}