package com.example.user_manager.data.network

import com.example.user_manager.data.model.User
import com.example.user_manager.data.model.UserResponse
import retrofit2.Response
import retrofit2.http.*

interface UserApiService {
    @GET("users")
    suspend fun getUsers(
        @Query("limit") limit: Int? = null,
        @Query("skip") skip: Int? = null
    ): Response<UserResponse>

    @GET("users/{id}")
    suspend fun getUserById(@Path("id") id: Int): Response<User>

    @POST("users/add")
    suspend fun addUser(@Body user: User): Response<User>

    @PUT("users/{id}")
    suspend fun updateUser(
        @Path("id") id: Int,
        @Body user: User
    ): Response<User>

    @DELETE("users/{id}")
    suspend fun deleteUser(@Path("id") id: Int): Response<Map<String, Boolean>>

    @GET("users/search")
    suspend fun searchUsers(@Query("q") query: String): Response<UserResponse>
}
