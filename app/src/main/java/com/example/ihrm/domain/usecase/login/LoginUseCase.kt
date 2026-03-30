package com.example.ihrm.domain.usecase.login

import com.example.ihrm.core.AppContainer
import com.example.ihrm.core.errorHandler.CommonErrorException
import com.example.ihrm.core.usecase.BaseUseCase
import com.example.ihrm.data.remote.base.NetworkResult
import com.example.ihrm.data.remote.login.LoginRequest
import com.example.ihrm.data.remote.login.LoginResponse
import com.example.ihrm.data.remote.login.PermissionResponse
import com.example.ihrm.domain.model.AccountType
import com.example.ihrm.domain.model.LoginInfo
import com.example.ihrm.domain.repository.AuthRepository
import com.example.ihrm.domain.repository.LanguageRepository
import com.example.ihrm.ui.login.EMPLOYEE_ID_EXACT_LENGTH
import com.example.ihrm.ui.login.LoginFieldError
import com.example.ihrm.ui.login.LoginUiState
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) : BaseUseCase() {
    @Inject
    override lateinit var languageRepository: LanguageRepository

    private var _loginInfo : LoginInfo? = null

    suspend fun login(employeeId: String, password: String): NetworkResult<LoginResponse> {
        _loginInfo = null
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

    /**
     * store login info after login success
     */
    fun storeUserInfo(data: LoginResponse) {
        _loginInfo = data.toLoginInfo()
    }

    /**
     * update account type and store into AppContainer
     */
    fun updateUserInfoFrom(permissions: PermissionResponse) {
        _loginInfo?.let {
            AppContainer.updateLoginInfo(it.copy(accountType = getAccountType(permissions)))
        }
    }

    private fun getAccountType(permissions: PermissionResponse): AccountType {
        var accountType = AccountType.Basic
        // SOW:
        // If an account is not allow to manage Leave or Security, we consider this account is BASIC
        // but currently, the Leave feature is not define yet, so only check security first
        permissions.permissions["SECURITY_CHECK"]?.run {
            if (canRead) {
                accountType = AccountType.Extra
            }
        }
        return accountType
    }

    /**
     * Validate employeeId
     */
    fun validateEmployeeId(employeeId: String): LoginFieldError? {
        if (employeeId.isBlank()) return null
        val len = employeeId.trim().length
        return when {
            len != EMPLOYEE_ID_EXACT_LENGTH -> LoginFieldError.InvalidLength
            else -> null
        }
    }

    /**
     * Validate password
     */
    fun validatePassword(password: String): LoginFieldError? {
        return when {
            password.isEmpty() -> null
            password.length < 8 -> LoginFieldError.TooShort
            else -> null
        }
    }
}
