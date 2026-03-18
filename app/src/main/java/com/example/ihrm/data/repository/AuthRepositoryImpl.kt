package com.example.ihrm.data.repository

import com.example.ihrm.data.remote.api.AuthApiService
import com.example.ihrm.data.remote.dto.AppErrorResponseDto
import com.example.ihrm.data.remote.dto.LoginDto
import com.example.ihrm.data.remote.dto.LoginResponseDto
import com.example.ihrm.domain.repository.AuthRepository
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

/** Gọi API auth be-nest-hrm: POST /auth/login (base URL từ Constants.BASE_URL). */
class AuthRepositoryImpl @Inject constructor(
    private val authApiService: AuthApiService,
    private val gson: Gson
) : AuthRepository {

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
}
