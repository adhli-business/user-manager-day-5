package com.example.user_manager.ui.useradd

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.user_manager.data.models.UserRequest
import com.example.user_manager.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserAddViewModel : ViewModel() {
    private val repository = UserRepository()

    private val _addSuccess = MutableStateFlow(false)
    val addSuccess: StateFlow<Boolean> = _addSuccess

    fun addUser(userRequest: UserRequest) {
        viewModelScope.launch {
            val result = repository.addUser(userRequest)
            result.onSuccess {
                _addSuccess.value = true
            }
        }
    }
}
