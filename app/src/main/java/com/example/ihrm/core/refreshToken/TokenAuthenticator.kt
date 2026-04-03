package com.example.ihrm.core.refreshToken

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
        // 1. Lấy Refresh Token hiện tại
        val refreshToken = AuthManager.getRefreshToken() ?: return null

        // 2. Đồng bộ hóa luồng (tránh gọi refresh nhiều lần cùng lúc)
        synchronized(this) {
            val currentToken = AuthManager.getAccessToken()
            val requestToken = response.request.header("Authorization")

            // Nếu token trong máy đã được update bởi một request khác trước đó
            if (requestToken != "Bearer $currentToken") {
                return response.request.newBuilder()
                    .header("Authorization", "Bearer $currentToken")
                    .build()
            }

            // 3. Gọi API Refresh (Sử dụng Call.execute() để chạy đồng bộ trong Authenticator)
            val refreshResponse = authApiProvider.get()
                .refreshToken("Bearer $refreshToken")
                .execute()

            return if (refreshResponse.isSuccessful && refreshResponse.body()?.data != null) {
                val loginResponse = refreshResponse.body()!!.data!!

                // 4. Cập nhật token vào SharedPreferences
                AuthManager.updateTokens(loginResponse.accessToken, loginResponse.refreshToken)

                // 5. Thử lại request bị lỗi ban đầu với token mới
                response.request.newBuilder()
                    .header("Authorization", "Bearer ${loginResponse.accessToken}")
                    .build()
            } else {
                // 6. Nếu refresh thất bại (Refresh token hết hạn) -> Logout
                AuthManager.clearTokens()
                
                // Gửi event Logout để MainActivity xử lý chuyển hướng màn hình
                runBlocking {
                    AuthManager.emitLogoutEvent()
                }
                null
            }
        }
    }
}
