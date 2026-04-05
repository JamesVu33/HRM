package com.example.ihrm.data.remote.interceptor

import com.example.ihrm.util.AuthManager
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        // Bỏ qua việc thêm token cho login và refresh-token
        if (originalRequest.url.encodedPath.contains("auth/login") ||
            originalRequest.url.encodedPath.contains("auth/refresh-token")
        ) {
            return chain.proceed(originalRequest)
        }

        val token = AuthManager.getAccessToken()
        return if (!token.isNullOrBlank()) {
            val newRequest = originalRequest.newBuilder()
                .header("Authorization", "Bearer $token") // Dùng .header() để ghi đè, tránh bị trùng
                .build()
            chain.proceed(newRequest)
        } else {
            chain.proceed(originalRequest)
        }
    }
}
