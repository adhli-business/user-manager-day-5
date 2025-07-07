package com.example.user_manager.data.network

import android.content.Context
import com.example.user_manager.utils.NetworkUtils
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class NetworkInterceptor(private val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        if (!NetworkUtils.isNetworkAvailable(context)) {
            throw IOException("No internet connection")
        }

        val request = chain.request()
        return chain.proceed(request)
    }
}
