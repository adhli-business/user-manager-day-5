package com.example.user_manager.ui.userlist

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.user_manager.data.model.User
import com.example.user_manager.data.repository.UserRepository
import com.example.user_manager.ui.base.BaseViewModel
import com.example.user_manager.utils.EventBus
import com.example.user_manager.utils.UserEvent
import kotlinx.coroutines.launch

class UserListViewModel(
    application: Application,
    private val repository: UserRepository
) : BaseViewModel(application) {

    private val _users = MutableLiveData<List<User>>()
    val users: LiveData<List<User>> = _users

    private val _isLastPage = MutableLiveData<Boolean>()
    val isLastPage: LiveData<Boolean> = _isLastPage

    private val _deletedUser = MutableLiveData<User?>()
    val deletedUser: LiveData<User?> = _deletedUser

    private var currentPage = 0
    private var isLoadingPage = false
    private val usersList = mutableListOf<User>()

    init {
        loadUsers()
    }

    fun loadUsers(isRefresh: Boolean = false) {
        if (isRefresh) {
            currentPage = 0
            usersList.clear()
        }

        if (isLoadingPage) return
        isLoadingPage = true

        viewModelScope.launch {
            showLoading()
            try {
                repository.getUsers(skip = currentPage * 10).fold(
                    onSuccess = { users ->
                        usersList.addAll(users)
                        _users.value = usersList.toList()
                        _isLastPage.value = users.isEmpty()
                        currentPage++
                    },
                    onFailure = { showError(it) }
                )
            } finally {
                hideLoading()
                isLoadingPage = false
            }
        }
    }

    fun deleteUser(userId: Int) {
        viewModelScope.launch {
            showLoading()
            try {
                repository.getUserById(userId).fold(
                    onSuccess = { user ->
                        repository.deleteUser(userId).fold(
                            onSuccess = { success ->
                                if (success) {
                                    usersList.removeAll { it.id == userId }
                                    _users.value = usersList.toList()
                                    _deletedUser.value = user
                                    EventBus.publish(UserEvent.UserDeleted(user))
                                }
                            },
                            onFailure = { showError(it) }
                        )
                    },
                    onFailure = { showError(it) }
                )
            } finally {
                hideLoading()
            }
        }
    }

    fun undoDelete(user: User) {
        viewModelScope.launch {
            showLoading()
            repository.addUser(user.toUserRequest()).fold(
                onSuccess = { restoredUser ->
                    usersList.add(restoredUser)
                    _users.value = usersList.toList()
                    _deletedUser.value = null
                },
                onFailure = { showError(it) }
            )
            hideLoading()
        }
    }

    fun isLoading() = isLoadingPage

    fun refreshUsers() {
        loadUsers(isRefresh = true)
    }
}
