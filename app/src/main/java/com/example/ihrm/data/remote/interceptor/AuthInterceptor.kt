package com.example.ihrm.data.remote.interceptor

import com.example.ihrm.util.AuthManager
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = AuthManager.getAccessToken()
        val request = if (!token.isNullOrBlank()) {
            chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
        } else {
            chain.request()
        }
        return chain.proceed(request)
    }
}