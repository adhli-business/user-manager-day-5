package com.example.user_manager.di

import android.content.Context
import com.example.user_manager.data.network.RetrofitClient
import com.example.user_manager.data.repository.UserRepository
import com.example.user_manager.data.session.SessionManager

object DependencyProvider {
    private var sessionManager: SessionManager? = null
    private var userRepository: UserRepository? = null

    fun provideSessionManager(context: Context): SessionManager {
        return sessionManager ?: SessionManager(context.applicationContext).also {
            sessionManager = it
        }
    }

    fun provideUserRepository(): UserRepository {
        return userRepository ?: UserRepository(RetrofitClient.userApiService).also {
            userRepository = it
        }
    }

    fun clearDependencies() {
        sessionManager = null
        userRepository = null
    }
}
