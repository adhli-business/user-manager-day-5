package com.example.user_manager.data.model

data class User(
    val id: Int = 0,
    val firstName: String,
    val lastName: String,
    val email: String,
    val gender: String,
    val image: String? = null,
    val age: Int = 0,
    val phone: String? = null,
    val birthDate: String? = null,
    val isDeleted: Boolean = false
)

data class UserResponse(
    val users: List<User>,
    val total: Int,
    val skip: Int,
    val limit: Int
)
