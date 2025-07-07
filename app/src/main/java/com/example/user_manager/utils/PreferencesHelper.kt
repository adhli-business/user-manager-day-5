package com.example.user_manager.utils

import android.content.Context
import android.content.SharedPreferences

class PreferencesHelper(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE)

    fun setLoggedIn(isLoggedIn: Boolean) {
        sharedPreferences.edit().putBoolean(Constants.KEY_IS_LOGGED_IN, isLoggedIn).apply()
    }

    fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean(Constants.KEY_IS_LOGGED_IN, false)
    }

    fun setUserId(userId: Int) {
        sharedPreferences.edit().putInt(Constants.KEY_USER_ID, userId).apply()
    }

    fun getUserId(): Int {
        return sharedPreferences.getInt(Constants.KEY_USER_ID, -1)
    }

    fun setUsername(username: String) {
        sharedPreferences.edit().putString(Constants.KEY_USERNAME, username).apply()
    }

    fun getUsername(): String? {
        return sharedPreferences.getString(Constants.KEY_USERNAME, null)
    }

    fun clearPreferences() {
        sharedPreferences.edit().clear().apply()
    }
}