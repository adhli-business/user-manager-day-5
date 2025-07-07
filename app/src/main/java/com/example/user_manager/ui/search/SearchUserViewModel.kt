package com.example.user_manager.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.user_manager.data.model.User
import com.example.user_manager.data.repository.UserRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchUserViewModel : ViewModel() {
    private val repository = UserRepository()

    private val _users = MutableLiveData<List<User>>()
    val users: LiveData<List<User>> = _users

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private var searchJob: Job? = null

    fun searchUsers(query: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(300) // Debounce search
            _isLoading.value = true
            try {
                // For now, we'll just get all users and filter them in memory
                // since the API doesn't provide a search endpoint
                repository.getUsers(limit = 100).fold(
                    onSuccess = { users ->
                        _users.value = users.filter { user ->
                            user.firstName.contains(query, ignoreCase = true) ||
                            user.lastName.contains(query, ignoreCase = true) ||
                            user.email.contains(query, ignoreCase = true)
                        }
                    },
                    onFailure = { _error.value = it.message }
                )
            } finally {
                _isLoading.value = false
            }
        }
    }
}
