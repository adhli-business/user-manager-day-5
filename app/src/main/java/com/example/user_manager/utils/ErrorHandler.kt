package com.example.user_manager.utils

sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val message: String, val cause: Exception? = null) : Result<Nothing>()
}

object ErrorHandler {
    fun handleApiError(throwable: Throwable): String {
        return when (throwable) {
            is retrofit2.HttpException -> {
                when (throwable.code()) {
                    400 -> "Invalid request"
                    401 -> "Unauthorized access"
                    403 -> "Access forbidden"
                    404 -> "Resource not found"
                    500 -> "Server error"
                    else -> "An error occurred: ${throwable.message()}"
                }
            }
            is java.net.UnknownHostException -> "No internet connection"
            is java.net.SocketTimeoutException -> "Connection timed out"
            else -> throwable.message ?: "An unexpected error occurred"
        }
    }

    fun handleValidationError(field: String): String {
        return when (field.lowercase()) {
            "email" -> "Please enter a valid email address"
            "password" -> "Password must be at least 6 characters"
            "firstname", "lastname" -> "Name cannot be empty"
            else -> "Invalid $field"
        }
    }
}
