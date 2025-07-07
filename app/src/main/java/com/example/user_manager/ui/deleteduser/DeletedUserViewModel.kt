package com.example.user_manager.ui.deleteduser

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.user_manager.data.models.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DeletedUserViewModel : ViewModel() {
    private val _deletedUsers = MutableStateFlow<List<User>>(emptyList())
    val deletedUsers: StateFlow<List<User>> = _deletedUsers

    fun restoreUser(user: User) {
        viewModelScope.launch {
            val currentList = _deletedUsers.value.toMutableList()
            currentList.remove(user)
            _deletedUsers.value = currentList
        }
    }
}