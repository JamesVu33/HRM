package com.example.ihrm.core.refreshToken

import android.util.Log
import com.example.ihrm.data.remote.api.AuthApiService
import com.example.ihrm.util.AuthManager
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject
import javax.inject.Provider

class TokenAuthenticator @Inject constructor(
    private val authApiProvider: Provider<AuthApiService>
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        Log.d("TokenAuthenticator", "Phát hiện lỗi 401 tại: ${response.request.url}")

        // 1. Lấy Refresh Token
        val refreshToken = AuthManager.getRefreshToken()
        if (refreshToken.isNullOrBlank()) {
            Log.e("TokenAuthenticator", "Không tìm thấy Refresh Token, yêu cầu đăng nhập lại.")
            return null
        }

        synchronized(this) {
            val currentToken = AuthManager.getAccessToken()
            val requestToken = response.request.header("Authorization")

            if (requestToken != "Bearer $currentToken") {
                Log.d("TokenAuthenticator", "Token đã được refresh bởi request khác, thử lại với token mới.")
                return response.request.newBuilder()
                    .header("Authorization", "Bearer $currentToken")
                    .build()
            }

            Log.d("TokenAuthenticator", "Đang tiến hành Refresh Token...")
            
            return try {
                // 3. Gọi API Refresh đồng bộ
                val refreshResponse = authApiProvider.get()
                    .refreshToken("Bearer $refreshToken")
                    .execute()

                if (refreshResponse.isSuccessful && refreshResponse.body()?.data != null) {
                    val loginResponse = refreshResponse.body()!!.data!!
                    Log.d("TokenAuthenticator", "Refresh thành công! Token mới: ${loginResponse.accessToken.take(10)}...")

                    AuthManager.updateTokens(loginResponse.accessToken, loginResponse.refreshToken)

                    response.request.newBuilder()
                        .header("Authorization", "Bearer ${loginResponse.accessToken}")
                        .build()
                } else {
                    Log.e("TokenAuthenticator", "Refresh thất bại: ${refreshResponse.code()} - ${refreshResponse.message()}")
                    handleLogout()
                    null
                }
            } catch (e: Exception) {
                Log.e("TokenAuthenticator", "Lỗi kết nối khi refresh: ${e.message}")
                null
            }
        }
    }

    private fun handleLogout() {
        AuthManager.clearTokens()
        runBlocking {
            AuthManager.emitLogoutEvent()
        }
    }
}
