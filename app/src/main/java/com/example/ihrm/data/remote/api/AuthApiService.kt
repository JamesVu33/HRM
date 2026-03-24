package com.example.ihrm.data.remote.api

import com.example.ihrm.data.remote.dto.ApiResponseDto
import com.example.ihrm.data.remote.dto.LoginDto
import com.example.ihrm.data.remote.dto.LoginResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Auth API từ be-nest-hrm.
 * Swagger: https://be-nest-hrm.onrender.com/swagger#/Auth/AuthController_login
 *
 * Login: POST /auth/login, body { employeeId, password }, response { statusCode, message, data: LoginResponseDto }.
 */
interface AuthApiService {

    /** POST /auth/login — [LoginDto]: employeeId, password. */
    @POST("auth/login")
    suspend fun login(@Body body: LoginDto): Response<ApiResponseDto<LoginResponseDto>>
}
