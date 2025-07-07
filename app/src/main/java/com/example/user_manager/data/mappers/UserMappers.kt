package com.example.user_manager.data.mappers

import com.example.user_manager.data.api.UserRequest
import com.example.user_manager.data.model.User

object UserMappers {
    fun User.toUserRequest(): UserRequest {
        return UserRequest(
            firstName = firstName,
            lastName = lastName,
            email = email,
            gender = gender
        )
    }

    fun User.toDisplayName(): String {
        return "$firstName $lastName"
    }

    fun User.toAgeGroup(): String {
        return when (age) {
            in 0..20 -> "0-20"
            in 21..30 -> "21-30"
            in 31..40 -> "31-40"
            in 41..50 -> "41-50"
            else -> "50+"
        }
    }

    fun User.toFullAddress(): String {
        return """
            ${address.address}
            ${address.city}, ${address.state} ${address.postalCode}
        """.trimIndent()
    }

    fun User.toCompanyInfo(): String {
        return """
            ${company.name}
            ${company.title}
            ${company.department}
        """.trimIndent()
    }
}
