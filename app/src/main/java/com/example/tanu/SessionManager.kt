package com.example.tanu

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {
    private var prefs: SharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)

    companion object {
        const val ACCESS_TOKEN = "access_token"
        const val REFRESH_TOKEN = "refresh_token"
    }

    /**
     * Function to save access token and refresh token
     */
    fun saveTokens(accessToken: String, refreshToken: String) {
        val editor = prefs.edit()
        editor.putString(ACCESS_TOKEN, accessToken)
        editor.putString(REFRESH_TOKEN, refreshToken)
        editor.apply()
    }

    /**
     * Function to fetch access token from SharedPreferences
     */
    fun fetchAccessToken(): String? {
        return prefs.getString(ACCESS_TOKEN, null)
    }

    /**
     * Function to fetch refresh token from SharedPreferences
     */
    fun fetchRefreshToken(): String? {
        return prefs.getString(REFRESH_TOKEN, null)
    }
}
