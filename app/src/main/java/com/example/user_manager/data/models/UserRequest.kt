package com.example.user_manager.data.models

data class UserRequest(
    val firstName: String,
    val lastName: String,
    val email: String,
    val gender: String,
    val age: String,
    val phone: String,
    val birthDate: String? = null,
    val address: String? = null
)

