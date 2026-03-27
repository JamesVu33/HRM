package com.example.ihrm.domain.repository

import com.example.ihrm.data.remote.base.NetworkResult
import com.example.ihrm.data.remote.login.LoginResponse
import com.example.ihrm.data.remote.login.PermissionResponse

/**
 * Auth operations (login, refresh, logout).
 */
interface AuthRepository {

    /**
     * Performs login with employeeId and password.
     * On success, [LoginResponseDto] may include `accountType` (`BASIC` / `EXTRA`) and
     * `modifiableFeatures` (feature codes). When omitted, [com.example.ihrm.util.AuthManager.saveTokens]
     * uses [com.example.ihrm.domain.session.LoginSessionResolver] and [com.example.ihrm.data.mock.LoginMockSession].
     *
     * @return NetworkResult with [LoginResponseDto] on success, or failure with error message.
     */
    suspend fun login(employeeId: String, password: String): NetworkResult<LoginResponse>
    suspend fun getPermission(employeeId: Int): NetworkResult<List<PermissionResponse>>
}
