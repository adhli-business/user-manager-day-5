package com.example.user_manager.ui.userdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.user_manager.data.models.User
import com.example.user_manager.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserDetailViewModel : ViewModel() {
    private val repository = UserRepository()

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    private val _deleteSuccess = MutableStateFlow(false)
    val deleteSuccess: StateFlow<Boolean> = _deleteSuccess

    fun loadUser(userId: Int) {
        viewModelScope.launch {
            val result = repository.getUserById(userId)
            result.onSuccess { user ->
                _user.value = user
            }
        }
    }

    fun deleteUser(userId: Int) {
        viewModelScope.launch {
            val result = repository.deleteUser(userId)
            result.onSuccess {
                _deleteSuccess.value = true
            }
        }
    }
}
