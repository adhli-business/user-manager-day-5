package com.example.user_manager.data.repository

import com.example.user_manager.data.api.RetrofitClient
import com.example.user_manager.data.models.LoginRequest
import com.example.user_manager.data.models.LoginResponse
import com.example.user_manager.data.models.UserRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class UserRepository {
    private val apiService = RetrofitClient.apiService

    suspend fun loginUser(username: String, password: String): Result<LoginResponse> {
        return try {
            val response = apiService.loginUser(LoginRequest(username, password))
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) Result.success(body)
                else Result.failure(Exception("Empty response"))
            } else {
                Result.failure(Exception("Login failed: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    suspend fun getAllUsers(limit: Int = 30, skip: Int = 0) = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getAllUsers(limit, skip)
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUserById(id: Int) = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getUserById(id)
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun addUser(userRequest: UserRequest) = withContext(Dispatchers.IO) {
        try {
            val response = apiService.addUser(userRequest)
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateUser(id: Int, userRequest: UserRequest) = withContext(Dispatchers.IO) {
        try {
            val response = apiService.updateUser(id, userRequest)
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteUser(id: Int) = withContext(Dispatchers.IO) {
        try {
            val response = apiService.deleteUser(id)
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun searchUsers(query: String, limit: Int = 30) = withContext(Dispatchers.IO) {
        try {
            val response = apiService.searchUsers(query, limit)
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}