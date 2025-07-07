package com.example.user_manager.ui.login

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.user_manager.ui.base.BaseViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LoginViewModel(application: Application) : BaseViewModel(application) {
    private val _loginState = MutableLiveData<LoginState>()
    val loginState: LiveData<LoginState> = _loginState

    fun login(username: String, password: String) {
        if (!validateInput(username, password)) return

        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            showLoading()

            try {
                // Simulate network delay
                delay(1000)

                if (username == "admin" && password == "admin") {
                    _loginState.value = LoginState.Success
                } else {
                    _loginState.value = LoginState.Error(
                        getApplication<Application>().getString(R.string.error_invalid_credentials)
                    )
                }
            } catch (e: Exception) {
                showError(e)
                _loginState.value = LoginState.Error(e.message ?: "Login failed")
            } finally {
                hideLoading()
            }
        }
    }

    private fun validateInput(username: String, password: String): Boolean {
        when {
            username.isBlank() -> {
                handleValidationError("username")
                return false
            }
            password.isBlank() -> {
                handleValidationError("password")
                return false
            }
        }
        return true
    }

    sealed class LoginState {
        object Loading : LoginState()
        object Success : LoginState()
        data class Error(val message: String) : LoginState()
    }
}
