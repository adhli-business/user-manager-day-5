package com.example.user_manager.data.api

// ApiService.kt
import com.example.user_manager.data.models.UserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface ApiService {
    @GET("users")
    suspend fun getAllUsers(
        @Query("limit") limit: Int = 30,
        @Query("skip") skip: Int = 0
    ): Response<UserResponse>

    @GET("users/{id}")
    suspend fun getUserById(
        @retrofit2.http.Path("id") id: Int
    ): Response<com.example.user_manager.data.models.User>

    @POST("users/add")
    suspend fun addUser(
        @Body user: com.example.user_manager.data.models.UserRequest
    ): Response<com.example.user_manager.data.models.User>

    @PUT("users/{id}")
    suspend fun updateUser(
        @retrofit2.http.Path("id") id: Int,
        @Body user: com.example.user_manager.data.models.UserRequest
    ): Response<com.example.user_manager.data.models.User>

    @DELETE("users/{id}")
    suspend fun deleteUser(
        @retrofit2.http.Path("id") id: Int
    ): Response<com.example.user_manager.data.models.User>

    // ✅ Tambahkan ini untuk pencarian user
    @GET("users/search")
    suspend fun searchUsers(
        @Query("q") query: String,
        @Query("limit") limit: Int = 30
    ): Response<UserResponse>
}
