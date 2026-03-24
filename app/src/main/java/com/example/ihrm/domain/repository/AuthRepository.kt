package com.example.ihrm.domain.repository

import com.example.ihrm.data.remote.login.LoginResponse
import com.example.ihrm.data.remote.base.NetworkResult

/**
 * Auth operations (login, refresh, logout).
 */
interface AuthRepository {

    /**
     * Performs login with employeeId and password.
     * @return NetworkResult with [LoginResponse] on success, or failure with error message.
     */
    suspend fun login(employeeId: String, password: String): NetworkResult<LoginResponse>
}
