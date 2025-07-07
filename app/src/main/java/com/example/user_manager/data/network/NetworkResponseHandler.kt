package com.example.user_manager.data.network

import retrofit2.Response
import java.net.SocketTimeoutException
import java.net.UnknownHostException

object NetworkResponseHandler {
    suspend fun <T> handleApiResponse(apiCall: suspend () -> Response<T>): Result<T> {
        return try {
            val response = apiCall()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Result.success(body)
                } else {
                    Result.failure(NetworkException.ApiException("Response body is null", response.code()))
                }
            } else {
                Result.failure(NetworkException.ApiException(response.message(), response.code()))
            }
        } catch (e: Exception) {
            Result.failure(mapToNetworkException(e))
        }
    }

    private fun mapToNetworkException(throwable: Throwable): NetworkException {
        return when (throwable) {
            is UnknownHostException -> NetworkException.NoInternetException
            is SocketTimeoutException -> NetworkException.TimeoutException
            is NetworkException -> throwable
            else -> NetworkException.UnknownException(throwable.message ?: "Unknown error occurred")
        }
    }
}
