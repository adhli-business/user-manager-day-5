package com.example.user_manager.ui.useredit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.user_manager.data.models.User
import com.example.user_manager.data.models.UserRequest
import com.example.user_manager.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserEditViewModel : ViewModel() {
    private val repository = UserRepository()

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    private val _updateSuccess = MutableStateFlow(false)
    val updateSuccess: StateFlow<Boolean> = _updateSuccess

    fun loadUser(userId: Int) {
        viewModelScope.launch {
            val result = repository.getUserById(userId)
            result.onSuccess { user ->
                _user.value = user
            }
        }
    }

    fun updateUser(userId: Int, userRequest: UserRequest) {
        viewModelScope.launch {
            val result = repository.updateUser(userId, userRequest)
            result.onSuccess {
                _updateSuccess.value = true
            }
        }
    }
}
