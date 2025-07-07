package com.example.user_manager.ui.userstatistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.user_manager.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class UserStatistics(
    val totalUsers: Int,
    val maleUsers: Int,
    val femaleUsers: Int,
    val averageAge: Double
)

class UserStatisticsViewModel : ViewModel() {
    private val repository = UserRepository()

    private val _statistics = MutableStateFlow(UserStatistics(0, 0, 0, 0.0))
    val statistics: StateFlow<UserStatistics> = _statistics

    fun loadStatistics() {
        viewModelScope.launch {
            val result = repository.getAllUsers(limit = 100)
            result.onSuccess { response ->
                val users = response.users
                val totalUsers = users.size
                val maleUsers = users.count { it.gender.equals("male", ignoreCase = true) }
                val femaleUsers = users.count { it.gender.equals("female", ignoreCase = true) }
                val averageAge = users.mapNotNull { it.age }.average()

                _statistics.value = UserStatistics(totalUsers, maleUsers, femaleUsers, averageAge)
            }
        }
    }
}
