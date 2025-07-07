package com.example.user_manager.ui.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket

object NetworkUtils {

    /**
     * Check if device has internet connection
     */
    fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false

            return when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            @Suppress("DEPRECATION")
            val networkInfo = connectivityManager.activeNetworkInfo ?: return false
            @Suppress("DEPRECATION")
            return networkInfo.isConnected
        }
    }

    /**
     * Check if device can reach a specific host
     */
    fun canReachHost(host: String, port: Int, timeout: Int = 3000): Boolean {
        return try {
            val socket = Socket()
            socket.connect(InetSocketAddress(host, port), timeout)
            socket.close()
            true
        } catch (e: IOException) {
            false
        }
    }

    /**
     * Get network type as string
     */
    fun getNetworkType(context: Context): String {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return "No Connection"
            val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return "No Connection"

            return when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> "WiFi"
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> "Cellular"
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> "Ethernet"
                else -> "Unknown"
            }
        } else {
            @Suppress("DEPRECATION")
            val networkInfo = connectivityManager.activeNetworkInfo ?: return "No Connection"
            @Suppress("DEPRECATION")
            return networkInfo.typeName
        }
    }

    /**
     * Check if connection is metered (limited data)
     */
    fun isConnectionMetered(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return connectivityManager.isActiveNetworkMetered
    }

    /**
     * Format error message based on network status
     */
    fun getNetworkErrorMessage(context: Context, error: Throwable): String {
        return when {
            !isInternetAvailable(context) -> "No internet connection. Please check your network settings."
            error is java.net.SocketTimeoutException -> "Connection timeout. Please try again."
            error is java.net.UnknownHostException -> "Unable to reach server. Please check your internet connection."
            error is java.net.ConnectException -> "Connection failed. Please try again later."
            error is javax.net.ssl.SSLException -> "Secure connection failed. Please check your network settings."
            else -> "Network error: ${error.message ?: "Unknown error"}"
        }
    }

    /**
     * Retry mechanism for network operations
     */
    suspend fun <T> retryNetworkCall(
        maxRetries: Int = 3,
        delayMillis: Long = 1000,
        action: suspend () -> T
    ): T {
        var lastException: Exception? = null

        repeat(maxRetries) { attempt ->
            try {
                return action()
            } catch (e: Exception) {
                lastException = e
                if (attempt < maxRetries - 1) {
                    kotlinx.coroutines.delay(delayMillis * (attempt + 1)) // Exponential backoff
                }
            }
        }

        throw lastException ?: Exception("Max retry attempts reached")
    }
}