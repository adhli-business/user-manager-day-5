package com.example.user_manager.data.repository

import com.example.user_manager.data.model.User
import com.example.user_manager.data.network.RetrofitClient
import com.example.user_manager.utils.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class UserRepository {
    private val api = RetrofitClient.getApiService()

    fun getUsers(limit: Int? = null, skip: Int? = null): Flow<NetworkResult<List<User>>> = flow {
        try {
            emit(NetworkResult.Loading())
            val response = api.getUsers(limit, skip)
            if (response.isSuccessful) {
                response.body()?.let { userResponse ->
                    emit(NetworkResult.Success(userResponse.users))
                } ?: emit(NetworkResult.Error("Empty response"))
            } else {
                emit(NetworkResult.Error("Failed to fetch users"))
            }
        } catch (e: Exception) {
            emit(NetworkResult.Error(e.message ?: "An error occurred"))
        }
    }.flowOn(Dispatchers.IO)

    fun getUserById(id: Int): Flow<NetworkResult<User>> = flow {
        try {
            emit(NetworkResult.Loading())
            val response = api.getUserById(id)
            if (response.isSuccessful) {
                response.body()?.let { user ->
                    emit(NetworkResult.Success(user))
                } ?: emit(NetworkResult.Error("User not found"))
            } else {
                emit(NetworkResult.Error("Failed to fetch user"))
            }
        } catch (e: Exception) {
            emit(NetworkResult.Error(e.message ?: "An error occurred"))
        }
    }.flowOn(Dispatchers.IO)

    fun addUser(user: User): Flow<NetworkResult<User>> = flow {
        try {
            emit(NetworkResult.Loading())
            val response = api.addUser(user)
            if (response.isSuccessful) {
                response.body()?.let { newUser ->
                    emit(NetworkResult.Success(newUser))
                } ?: emit(NetworkResult.Error("Failed to add user"))
            } else {
                emit(NetworkResult.Error("Failed to add user"))
            }
        } catch (e: Exception) {
            emit(NetworkResult.Error(e.message ?: "An error occurred"))
        }
    }.flowOn(Dispatchers.IO)

    fun updateUser(id: Int, user: User): Flow<NetworkResult<User>> = flow {
        try {
            emit(NetworkResult.Loading())
            val response = api.updateUser(id, user)
            if (response.isSuccessful) {
                response.body()?.let { updatedUser ->
                    emit(NetworkResult.Success(updatedUser))
                } ?: emit(NetworkResult.Error("Failed to update user"))
            } else {
                emit(NetworkResult.Error("Failed to update user"))
            }
        } catch (e: Exception) {
            emit(NetworkResult.Error(e.message ?: "An error occurred"))
        }
    }.flowOn(Dispatchers.IO)

    fun searchUsers(query: String): Flow<NetworkResult<List<User>>> = flow {
        try {
            emit(NetworkResult.Loading())
            val response = api.searchUsers(query)
            if (response.isSuccessful) {
                response.body()?.let { userResponse ->
                    emit(NetworkResult.Success(userResponse.users))
                } ?: emit(NetworkResult.Error("No results found"))
            } else {
                emit(NetworkResult.Error("Search failed"))
            }
        } catch (e: Exception) {
            emit(NetworkResult.Error(e.message ?: "An error occurred"))
        }
    }.flowOn(Dispatchers.IO)
}
