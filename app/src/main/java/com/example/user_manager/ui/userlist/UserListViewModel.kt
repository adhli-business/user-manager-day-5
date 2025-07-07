package com.example.user_manager.ui.userlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.user_manager.data.models.User
import com.example.user_manager.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserListViewModel : ViewModel() {
    private val repository = UserRepository()

    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users: StateFlow<List<User>> = _users

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        loadUsers()
    }

    fun loadUsers() {
        viewModelScope.launch {
            _isLoading.value = true

            val result = repository.getAllUsers() // asumsi: return Result<UserListResponse>
            result.onSuccess { response ->
                _users.value = response.users
            }.onFailure {
                // log atau tampilkan error
            }

            _isLoading.value = false
        }
    }

    fun refreshUsers() {
        loadUsers()
    }
}
