package com.example.user_manager.data.models

data class UserRequest(
    val firstName: String,
    val lastName: String,
    val email: String,
    val gender: String? = null,
    val phone: String? = null,
    val username: String? = null,
    val password: String? = null,
    val birthDate: String? = null,
    val age: Int? = null
)