package com.example.user_manager.data.session

import android.content.Context
import android.content.SharedPreferences
import com.example.user_manager.utils.Constants

class SessionManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("UserManagerPrefs", Context.MODE_PRIVATE)

    var isLoggedIn: Boolean
        get() = prefs.getBoolean("is_logged_in", false)
        set(value) = prefs.edit().putBoolean("is_logged_in", value).apply()

    var userId: Int
        get() = prefs.getInt(Constants.PREF_USER_ID, -1)
        set(value) = prefs.edit().putInt(Constants.PREF_USER_ID, value).apply()

    var userName: String?
        get() = prefs.getString(Constants.PREF_USER_NAME, null)
        set(value) = prefs.edit().putString(Constants.PREF_USER_NAME, value).apply()

    var userEmail: String?
        get() = prefs.getString(Constants.PREF_USER_EMAIL, null)
        set(value) = prefs.edit().putString(Constants.PREF_USER_EMAIL, value).apply()

    fun saveUserSession(id: Int, name: String, email: String) {
        isLoggedIn = true
        userId = id
        userName = name
        userEmail = email
    }

    fun clearSession() {
        prefs.edit().clear().apply()
    }
}
