package com.example.tanu

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {
    private var prefs: SharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)

    companion object {
        const val USER_LOGIN = "user_login"
        const val USER_PASSWORD = "user_password"
    }

    /**
     * Function to save login and password
     */
    fun saveCredentials(login: String, password: String) {
        val editor = prefs.edit()
        editor.putString(USER_LOGIN, login)
        editor.putString(USER_PASSWORD, password)
        editor.apply()
    }

    /**
     * Function to fetch login from SharedPreferences
     */
    fun fetchLogin(): String? {
        return prefs.getString(USER_LOGIN, null)
    }

    /**
     * Function to fetch password from SharedPreferences
     */
    fun fetchPassword(): String? {
        return prefs.getString(USER_PASSWORD, null)
    }
}