package com.example.ihrm.domain.usecase.login

import com.example.ihrm.core.errorHandler.CommonErrorException
import com.example.ihrm.core.usecase.BaseUseCase
import com.example.ihrm.data.remote.base.NetworkResult
import com.example.ihrm.data.remote.login.LoginRequest
import com.example.ihrm.data.remote.login.LoginResponse
import com.example.ihrm.data.remote.login.PermissionResponse
import com.example.ihrm.domain.model.Employee
import com.example.ihrm.domain.repository.AuthRepository
import com.example.ihrm.domain.repository.LanguageRepository
import com.example.ihrm.ui.login.LoginFieldError
import com.example.ihrm.ui.login.LoginUiState
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) : BaseUseCase() {
    @Inject
    override lateinit var languageRepository: LanguageRepository

    suspend fun login(employeeId: String, password: String): NetworkResult<LoginResponse> {
        val loginResult = authRepository.login(employeeId, password)
        return translateResponse(loginResult)
    }

    fun getErrorField(uiState: LoginUiState, response: CommonErrorException): LoginUiState {
        if (response !is CommonErrorException.InvalidInputException || response.errorField == null) return uiState
        return when {
            LoginRequest.isEmployeeIdField(response.errorField) -> uiState.copy(
                employeeIdError = LoginFieldError.ServerMsg(
                    response.errorMsg ?: ""
                )
            )

            LoginRequest.isPasswordField(response.errorField) -> uiState.copy(
                passwordError = LoginFieldError.ServerMsg(
                    response.errorMsg ?: ""
                )
            )

            else -> uiState
        }
    }

    suspend fun getPermission(employeeId: Int): NetworkResult<List<PermissionResponse>> {
        val permissionResult = authRepository.getPermission(employeeId)
        return translateResponse(permissionResult)
    }
}
