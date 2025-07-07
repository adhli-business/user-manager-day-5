package com.example.user_manager.utils

object Constants {
    const val BASE_URL = "https://dummyjson.com/"
    const val DEFAULT_PAGE_SIZE = 10
    const val CONNECT_TIMEOUT = 30L
    const val READ_TIMEOUT = 30L
    const val WRITE_TIMEOUT = 30L

    const val PREF_USER_ID = "user_id"
    const val PREF_USER_NAME = "user_name"
    const val PREF_USER_EMAIL = "user_email"

    // Chart colors
    val CHART_COLORS = intArrayOf(
        android.graphics.Color.rgb(64, 89, 128),
        android.graphics.Color.rgb(149, 165, 124),
        android.graphics.Color.rgb(217, 184, 162),
        android.graphics.Color.rgb(191, 134, 134),
        android.graphics.Color.rgb(179, 48, 80)
    )
}
