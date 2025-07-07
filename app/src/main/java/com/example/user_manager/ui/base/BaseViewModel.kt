package com.example.user_manager.ui.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.user_manager.utils.ErrorMapper

abstract class BaseViewModel(application: Application) : AndroidViewModel(application) {
    private val errorMapper = ErrorMapper(application)

    protected val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    protected val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    protected fun showLoading() {
        _isLoading.value = true
    }

    protected fun hideLoading() {
        _isLoading.value = false
    }

    protected fun showError(throwable: Throwable) {
        _error.value = errorMapper.mapToUserFriendlyMessage(throwable)
    }

    protected fun showError(message: String) {
        _error.value = message
    }

    protected fun clearError() {
        _error.value = null
    }

    protected fun handleValidationError(field: String) {
        _error.value = errorMapper.mapValidationError(field)
    }
}
