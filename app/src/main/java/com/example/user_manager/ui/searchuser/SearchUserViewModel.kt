package com.example.user_manager.ui.searchuser

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.user_manager.data.models.User
import com.example.user_manager.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collect


class SearchUserViewModel : ViewModel() {
    private val repository = UserRepository()

    private val _searchResults = MutableStateFlow<List<User>>(emptyList())
    val searchResults: StateFlow<List<User>> = _searchResults

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun searchUsers(query: String) {
        viewModelScope.launch {
            _isLoading.value = true

            Log.d("SearchViewModel", "Search query: $query")


            val result = repository.searchUsers(query)
            result.onSuccess { response ->
                Log.d("SearchViewModel", "Found users: ${response.users}")
                _searchResults.value = response.users
            }.onFailure {
                Log.e("SearchViewModel", "Search failed: ${it.message}")
                _searchResults.value = emptyList()
            }

            _isLoading.value = false
        }

    }

    fun clearSearch() {
        _searchResults.value = emptyList()
    }
}
