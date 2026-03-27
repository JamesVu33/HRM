package com.example.ihrm.data.repository

import com.example.ihrm.data.remote.api.AuthApiService
import com.example.ihrm.data.remote.base.safeApiCall
import com.example.ihrm.data.remote.login.LoginRequest
import com.example.ihrm.data.remote.login.LoginResponse
import com.example.ihrm.data.remote.base.NetworkResult
import com.example.ihrm.domain.repository.AuthRepository
import retrofit2.Retrofit
import javax.inject.Inject

/** Gọi API auth be-nest-hrm: POST /auth/login (base URL từ Constants.BASE_URL). */
class AuthRepositoryImpl @Inject constructor(
    private val authApiService: AuthApiService,
    private val retrofit: Retrofit
) : AuthRepository {

    override suspend fun login(employeeId: String, password: String): NetworkResult<LoginResponse> =
        safeApiCall(retrofit) {
            authApiService.login(
                LoginRequest(employeeId = employeeId.trim(), password = password)
            )
        }




    /*
     override suspend fun login(employeeId: String, password: String): Result<LoginResponseDto> =
        withContext(Dispatchers.IO) {
            try {
                val response = authApiService.login(
                    LoginDto(employeeId = employeeId.trim(), password = password)
                )
                when {
                    response.statusCode == 401 -> Result.failure(Exception("Unauthorized"))
                    response.statusCode in 200..299 && response.data != null -> Result.success(response.data)
                    else -> Result.failure(Exception(response.message))
                }
            } catch (e: HttpException) {
                val message = parseApiErrorMessage(e.response()?.errorBody()?.string())
                    ?: e.message()
                Result.failure(Exception(message))
            } catch (e: IOException) {
                Result.failure(e)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    /**
     * Parses be-nest-hrm error body (errorType, errors) into a user-friendly message.
     */
    private fun parseApiErrorMessage(errorBody: String?): String? {
        if (errorBody.isNullOrBlank()) return null
        return try {
            val dto = gson.fromJson(errorBody, AppErrorResponseDto::class.java)
            buildErrorMessage(dto)
        } catch (_: JsonSyntaxException) {
            null
        }
    }

    private fun buildErrorMessage(dto: AppErrorResponseDto): String {
        val errors = dto.errors
        if (!errors.isNullOrEmpty()) {
            val firstEntry = errors.entries.firstOrNull()
            val firstList = firstEntry?.value
            val firstKey = firstList?.firstOrNull()?.key
            if (!firstKey.isNullOrBlank()) return firstKey
        }
        return when (dto.errorType) {
            "DOMAIN_ERROR" -> "Invalid employee ID or password."
            "VALIDATION_ERROR" -> "Invalid input. Please check your entries."
            "GUARD_ERROR" -> "Access denied."
            "HTTP_ERROR" -> "Request failed. Please try again."
            "DB_KNOWN_ERROR", "DB_UNKNOWN_ERROR" -> "Server error. Please try again later."
            "INTERNAL_ERROR" -> "Server error. Please try again later."
            else -> "Something went wrong. Please try again."
        }
    }
     */
}
