package com.example.user_manager.ui.userdetail

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.user_manager.data.model.User
import com.example.user_manager.data.repository.UserRepository
import com.example.user_manager.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class UserDetailViewModel(
    application: Application,
    private val repository: UserRepository
) : BaseViewModel(application) {

    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user

    private val _userDeleted = MutableLiveData<Boolean>()
    val userDeleted: LiveData<Boolean> = _userDeleted

    fun loadUser(userId: Int) {
        viewModelScope.launch {
            showLoading()
            try {
                repository.getUserById(userId).fold(
                    onSuccess = { _user.value = it },
                    onFailure = { showError(it) }
                )
            } finally {
                hideLoading()
            }
        }
    }

    fun deleteUser(userId: Int) {
        viewModelScope.launch {
            showLoading()
            try {
                repository.deleteUser(userId).fold(
                    onSuccess = { success ->
                        if (success) {
                            _userDeleted.value = true
                        } else {
                            showError("Failed to delete user")
                        }
                    },
                    onFailure = { showError(it) }
                )
            } finally {
                hideLoading()
            }
        }
    }
}
