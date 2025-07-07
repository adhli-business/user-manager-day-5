package com.example.user_manager.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException

object NetworkUtils {
    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false

        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    fun getErrorMessage(throwable: Throwable): String {
        return when (throwable) {
            is IOException -> "Network error occurred. Please check your internet connection."
            is SocketTimeoutException -> "Request timed out. Please try again."
            is HttpException -> {
                when (throwable.code()) {
                    400 -> "Bad request"
                    401 -> "Unauthorized access"
                    403 -> "Forbidden access"
                    404 -> "Resource not found"
                    500 -> "Internal server error"
                    else -> "An error occurred: ${throwable.message}"
                }
            }
            else -> throwable.message ?: "An unexpected error occurred"
        }
    }
}
