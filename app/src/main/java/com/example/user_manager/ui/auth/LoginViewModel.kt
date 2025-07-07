package com.example.user_manager.ui.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.user_manager.data.models.User
import com.example.user_manager.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    data class Success(val user: User) : LoginState()
    data class Error(val message: String) : LoginState()
}

class LoginViewModel : ViewModel() {
    private val repository = UserRepository()

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            Log.d("LOGIN", "Sending login request...")

            val result = repository.getAllUsers(1, 0)

            result.onSuccess { response ->
                Log.d("LOGIN", "API success: ${response.users.size} users found")

                val user = response.users.find {
                    it.email.equals(email, ignoreCase = true) && it.password == password
                }

                if (user != null) {
                    Log.d("LOGIN", "Login success: ${user.email}")
                    _loginState.value = LoginState.Success(user)
                } else {
                    Log.d("LOGIN", "Login failed: Email or password incorrect")
                    _loginState.value = LoginState.Error("Email or password incorrect")
                }
            }.onFailure { exception ->
                Log.e("LOGIN", "Login error: ${exception.message}", exception)
                _loginState.value = LoginState.Error(exception.message ?: "Unknown error")
            }
        }
    }

}
