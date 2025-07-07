package com.example.user_manager.data.network

sealed class NetworkException(message: String? = null) : Exception(message) {
    object NoInternetException : NetworkException("No internet connection available")
    object TimeoutException : NetworkException("Request timed out")
    data class ServerException(override val message: String) : NetworkException(message)
    data class ApiException(override val message: String, val code: Int) : NetworkException(message)
    data class UnknownException(override val message: String) : NetworkException(message)
}
