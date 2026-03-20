package com.example.ihrm.domain.repository

import com.example.ihrm.data.remote.dto.LoginResponseDto

/**
 * Auth operations (login, refresh, logout).
 */
interface AuthRepository {

    /**
     * Performs login with employeeId and password.
     * @return Result with [LoginResponseDto] on success, or failure with error message.
     */
    suspend fun login(employeeId: String, password: String): Result<LoginResponseDto>
}
