package com.example.user_manager.ui.main

import com.example.user_manager.ui.utils.PreferencesHelper

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.user_manager.data.repository.UserRepository

import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val preferencesHelper = PreferencesHelper(application)
    private val userRepository = UserRepository()

    // LiveData for UI state
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private val _logoutState = MutableLiveData<Boolean>()
    val logoutState: LiveData<Boolean> = _logoutState

    private val _currentUserId = MutableLiveData<Int>()
    val currentUserId: LiveData<Int> = _currentUserId

    private val _currentUsername = MutableLiveData<String>()
    val currentUsername: LiveData<String> = _currentUsername

    init {
        loadUserSession()
    }

    private fun loadUserSession() {
        _currentUserId.value = preferencesHelper.getUserId()
        _currentUsername.value = preferencesHelper.getUsername()
    }

    fun logout() {
        viewModelScope.launch {
            try {
                _isLoading.value = true

                // Clear preferences
                preferencesHelper.clearPreferences()

                // Update logout state
                _logoutState.value = true

            } catch (e: Exception) {
                _errorMessage.value = "Error during logout: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getCurrentUserId(): Int {
        return preferencesHelper.getUserId()
    }

    fun getCurrentUsername(): String? {
        return preferencesHelper.getUsername()
    }

    fun isUserLoggedIn(): Boolean {
        return preferencesHelper.isLoggedIn()
    }

    fun clearErrorMessage() {
        _errorMessage.value = ""
    }

    fun refreshUserSession() {
        loadUserSession()
    }

    // Method to handle navigation events
    fun onNavigationItemSelected(itemId: Int) {
        // This can be used to track navigation analytics or perform actions
        // when specific navigation items are selected
    }

    // Method to check if user has permission to access certain features
    fun hasPermission(permission: String): Boolean {
        // This can be extended to handle role-based permissions
        return preferencesHelper.isLoggedIn()
    }

    // Method to update user session info
    fun updateUserSession(userId: Int, username: String) {
        preferencesHelper.setUserId(userId)
        preferencesHelper.setUsername(username)
        preferencesHelper.setLoggedIn(true)
        loadUserSession()
    }
}