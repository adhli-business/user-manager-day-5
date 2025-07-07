package com.example.user_manager.ui.statistics

data class UserStatistics(
    val genderDistribution: Map<String, Int>,
    val ageDistribution: Map<String, Int>
)
