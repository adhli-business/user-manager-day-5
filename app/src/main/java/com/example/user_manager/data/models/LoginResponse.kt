package com.example.user_manager.data.models

data class LoginResponse(
    val id: Int,
    val username: String,
    val email: String,
    val token: String,
    val firstName: String,
    val lastName: String
)
