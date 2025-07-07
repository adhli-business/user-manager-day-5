package com.example.user_manager.ui.statistics

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.user_manager.data.repository.UserRepository
import com.example.user_manager.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class UserStatisticsViewModel(
    application: Application,
    private val repository: UserRepository
) : BaseViewModel(application) {

    private val _statistics = MutableLiveData<UserStatistics>()
    val statistics: LiveData<UserStatistics> = _statistics

    init {
        loadStatistics()
    }

    private fun loadStatistics() {
        viewModelScope.launch {
            showLoading()
            try {
                repository.getUsers(limit = 100).fold(
                    onSuccess = { users ->
                        val genderDistribution = users.groupingBy { it.gender }
                            .eachCount()
                            .mapValues { it.value.toFloat() / users.size }

                        val ageDistribution = users.groupBy { user ->
                            when (user.age) {
                                in 0..20 -> "0-20"
                                in 21..30 -> "21-30"
                                in 31..40 -> "31-40"
                                in 41..50 -> "41-50"
                                else -> "50+"
                            }
                        }.mapValues { it.value.size.toFloat() / users.size }
                        .toSortedMap()

                        _statistics.value = UserStatistics(
                            genderDistribution = genderDistribution,
                            ageDistribution = ageDistribution
                        )
                    },
                    onFailure = { showError(it) }
                )
            } finally {
                hideLoading()
            }
        }
    }

    fun refreshStatistics() {
        loadStatistics()
    }
}
