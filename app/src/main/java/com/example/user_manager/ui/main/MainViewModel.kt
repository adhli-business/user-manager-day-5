package com.example.user_manager.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.user_manager.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val repository = UserRepository()

    private val _userCount = MutableStateFlow(0)
    val userCount: StateFlow<Int> = _userCount

    init {
        loadUserCount()
    }

    private fun loadUserCount() {
        viewModelScope.launch {
            val result = repository.getAllUsers(1, 0)
            result.onSuccess { response ->
                _userCount.value = response.total
            }.onFailure {
                // Optional: Handle error, e.g., log or set fallback value
                _userCount.value = 0
            }
        }
    }

}