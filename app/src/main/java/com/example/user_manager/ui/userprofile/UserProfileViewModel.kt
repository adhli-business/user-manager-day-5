package com.example.user_manager.ui.userprofile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.user_manager.data.models.User
import com.example.user_manager.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserProfileViewModel : ViewModel() {
    private val repository = UserRepository()

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    fun loadProfile(userId: Int) {
        viewModelScope.launch {
            val result = repository.getUserById(userId)
            result.onSuccess { user ->
                _user.value = user
            }.onFailure {
                // kamu bisa log error atau kasih default value
            }
        }
    }
}
