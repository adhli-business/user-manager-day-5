package com.example.user_manager

import android.app.Application
import com.example.user_manager.data.network.RetrofitClient
import com.example.user_manager.utils.NetworkStateHandler
import timber.log.Timber

class UserManagerApplication : Application() {
    lateinit var networkStateHandler: NetworkStateHandler

    override fun onCreate() {
        super.onCreate()

        // Initialize Timber for logging
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        // Initialize NetworkStateHandler
        networkStateHandler = NetworkStateHandler(this)

        // Initialize RetrofitClient
        RetrofitClient.initialize(this)
    }

    override fun onTerminate() {
        super.onTerminate()
        networkStateHandler.cleanup()
    }
}
