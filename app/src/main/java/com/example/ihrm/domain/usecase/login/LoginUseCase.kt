package com.example.ihrm.domain.usecase.login

import com.example.ihrm.core.usecase.BaseUseCase
import com.example.ihrm.data.remote.base.NetworkResult
import com.example.ihrm.data.remote.login.LoginResponse
import com.example.ihrm.domain.model.Employee
import com.example.ihrm.domain.repository.AuthRepository
import com.example.ihrm.domain.repository.EmployeeRepository
import com.example.ihrm.domain.repository.LanguageRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository,
): BaseUseCase() {
    @Inject
    override lateinit var languageRepository: LanguageRepository

    suspend fun login(employeeId: String, password: String): NetworkResult<LoginResponse> {
        val loginResult = authRepository.login(employeeId, password)
        return translateResponse(loginResult)
    }
}
