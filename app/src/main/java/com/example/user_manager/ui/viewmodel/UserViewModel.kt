package com.example.user_manager.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.user_manager.data.model.User
import com.example.user_manager.data.repository.UserRepository
import com.example.user_manager.utils.NetworkResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserViewModel : ViewModel() {
    private val repository = UserRepository()

    private val _users = MutableStateFlow<NetworkResult<List<User>>>(NetworkResult.Loading())
    val users: StateFlow<NetworkResult<List<User>>> = _users

    private val _selectedUser = MutableStateFlow<NetworkResult<User>>(NetworkResult.Loading())
    val selectedUser: StateFlow<NetworkResult<User>> = _selectedUser

    private val _searchResults = MutableStateFlow<NetworkResult<List<User>>>(NetworkResult.Loading())
    val searchResults: StateFlow<NetworkResult<List<User>>> = _searchResults

    fun fetchUsers(limit: Int? = null, skip: Int? = null) {
        viewModelScope.launch {
            repository.getUsers(limit, skip).collect { result ->
                _users.value = result
            }
        }
    }

    fun getUserById(id: Int) {
        viewModelScope.launch {
            repository.getUserById(id).collect { result ->
                _selectedUser.value = result
            }
        }
    }

    fun addUser(user: User) {
        viewModelScope.launch {
            repository.addUser(user).collect { result ->
                when (result) {
                    is NetworkResult.Success -> fetchUsers()
                    else -> {} // Handle error cases if needed
                }
            }
        }
    }

    fun updateUser(id: Int, user: User) {
        viewModelScope.launch {
            repository.updateUser(id, user).collect { result ->
                when (result) {
                    is NetworkResult.Success -> {
                        fetchUsers()
                        _selectedUser.value = result
                    }
                    else -> {} // Handle error cases if needed
                }
            }
        }
    }

    fun searchUsers(query: String) {
        viewModelScope.launch {
            repository.searchUsers(query).collect { result ->
                _searchResults.value = result
            }
        }
    }
}
