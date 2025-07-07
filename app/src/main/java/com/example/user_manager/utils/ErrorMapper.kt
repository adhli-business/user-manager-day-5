package com.example.user_manager.utils

import android.content.Context
import com.example.user_manager.R
import com.example.user_manager.data.network.NetworkException

class ErrorMapper(private val context: Context) {
    fun mapToUserFriendlyMessage(error: Throwable): String {
        return when (error) {
            is NetworkException.NoInternetException ->
                context.getString(R.string.error_no_internet)
            is NetworkException.TimeoutException ->
                context.getString(R.string.error_timeout)
            is NetworkException.ServerException ->
                context.getString(R.string.error_server)
            is NetworkException.ApiException -> {
                when (error.code) {
                    401 -> context.getString(R.string.error_unauthorized)
                    403 -> context.getString(R.string.error_forbidden)
                    404 -> context.getString(R.string.error_not_found)
                    else -> error.message
                }
            }
            else -> context.getString(R.string.error_unknown)
        }
    }

    fun mapValidationError(field: String): String {
        return when (field.lowercase()) {
            "email" -> context.getString(R.string.error_invalid_email)
            "firstname" -> context.getString(R.string.error_invalid_firstname)
            "lastname" -> context.getString(R.string.error_invalid_lastname)
            "phone" -> context.getString(R.string.error_invalid_phone)
            else -> context.getString(R.string.error_invalid_field, field)
        }
    }
}
