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

    fun login(username: String, password: String) {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading

            val result = repository.loginUser(username, password)

            result.onSuccess { response ->
                val user = User( // kalau kamu pakai model `User` lokal
                    id = response.id,
                    firstName = response.firstName,
                    lastName = response.lastName,
                    email = response.email,
                    username = response.username,
                    password = password, // opsional
                    image = null // sesuaikan dengan kebutuhan
                )
                _loginState.value = LoginState.Success(user)
            }.onFailure { e ->
                _loginState.value = LoginState.Error(e.message ?: "Unknown error")
            }
        }
    }


}
