package com.example.user_manager.utils

object ValidationUtils {
    fun isValidEmail(email: String): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        return email.matches(emailPattern.toRegex())
    }

    fun isValidName(name: String): Boolean {
        return name.trim().length >= 2
    }

    fun isValidPhone(phone: String): Boolean {
        return phone.trim().length >= 10
    }

    data class ValidationResult(
        val isValid: Boolean,
        val errorMessage: String? = null
    )

    fun validateUserInput(
        firstName: String,
        lastName: String,
        email: String,
        phone: String? = null
    ): ValidationResult {
        return when {
            !isValidName(firstName) ->
                ValidationResult(false, "First name must be at least 2 characters")
            !isValidName(lastName) ->
                ValidationResult(false, "Last name must be at least 2 characters")
            !isValidEmail(email) ->
                ValidationResult(false, "Please enter a valid email address")
            phone != null && !isValidPhone(phone) ->
                ValidationResult(false, "Please enter a valid phone number")
            else -> ValidationResult(true)
        }
    }
}
