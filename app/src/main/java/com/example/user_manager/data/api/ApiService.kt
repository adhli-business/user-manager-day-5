package com.example.user_manager.data.api

import com.example.user_manager.data.models.User
import com.example.user_manager.data.models.UserRequest
import com.example.user_manager.data.models.UserResponse
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @GET("users")
    suspend fun getAllUsers(
        @Query("limit") limit: Int = 30,
        @Query("skip") skip: Int = 0
    ): Response<UserResponse>

    @GET("users/{id}")
    suspend fun getUserById(@Path("id") id: Int): Response<User>

    @POST("users/add")
    suspend fun addUser(@Body user: UserRequest): Response<User>

    @PUT("users/{id}")
    suspend fun updateUser(
        @Path("id") id: Int,
        @Body user: UserRequest
    ): Response<User>

    @DELETE("users/{id}")
    suspend fun deleteUser(@Path("id") id: Int): Response<User>

    @GET("users/search")
    suspend fun searchUsers(
        @Query("q") query: String,
        @Query("limit") limit: Int = 30
    ): Response<UserResponse>
}