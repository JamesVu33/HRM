package com.example.ihrm.ui.login

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.example.ihrm.core.viewmodel.BaseViewmodel
import com.example.ihrm.core.viewmodel.CallbackWrapper
import com.example.ihrm.data.remote.dto.AppErrorResponseDto
import com.example.ihrm.data.remote.dto.LoginResponseDto
import com.example.ihrm.domain.repository.AuthRepository
import com.example.ihrm.util.AuthManager
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LoginUiState(
    val employeeId: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val employeeIdError: LoginFieldError? = null,
    val passwordError: LoginFieldError? = null,
    val isLoginSuccess: Boolean = false,
    val isPasswordVisible: Boolean = false,
    val loginError: String? = null
)

/** API requires employeeId exactly 8 characters. */
const val EMPLOYEE_ID_EXACT_LENGTH = 8

sealed interface LoginFieldError {
    data object Required : LoginFieldError
    data object TooShort : LoginFieldError
    data object InvalidLength : LoginFieldError  // not exactly 8 chars
    data object InvalidRules : LoginFieldError
}

@HiltViewModel
class LoginViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val authRepository: AuthRepository
) : BaseViewmodel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun updateEmployeeId(employeeId: String) {
        val error = validateEmployeeId(employeeId)
        _uiState.value = _uiState.value.copy(
            employeeId = employeeId,
            employeeIdError = error,
            loginError = null
        )
    }

    fun updatePassword(password: String) {
        val error = validatePassword(password)
        _uiState.value = _uiState.value.copy(
            password = password,
            passwordError = error,
            loginError = null
        )
    }

    fun togglePasswordVisibility() {
        _uiState.value = _uiState.value.copy(
            isPasswordVisible = !_uiState.value.isPasswordVisible
        )
    }

    fun login(onSuccess: () -> Unit) {
        viewModelScope.launch {
            val employeeId = _uiState.value.employeeId.trim()
            val password = _uiState.value.password

            if (employeeId.isEmpty()) {
                _uiState.value = _uiState.value.copy(
                    employeeIdError = LoginFieldError.Required
                )
                return@launch
            }

            if (password.isEmpty()) {
                _uiState.value = _uiState.value.copy(
                    passwordError = LoginFieldError.Required
                )
                return@launch
            }

            val employeeIdError = validateEmployeeId(employeeId)
            val passwordError = validatePassword(password)
            if (employeeIdError != null || passwordError != null) {
                _uiState.value = _uiState.value.copy(
                    employeeIdError = employeeIdError,
                    passwordError = passwordError
                )
                return@launch
            }

            _uiState.value = _uiState.value.copy(
                isLoading = true,
                employeeIdError = null,
                passwordError = null,
                loginError = null
            )

            fetchData(
                fetching = { authRepository.login(employeeId, password) },
                callbackWrapper = object : CallbackWrapper<LoginResponseDto> {
                    override fun onSuccess(data: LoginResponseDto) {
                        AuthManager.saveTokens(data)
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            isLoginSuccess = true
                        )
                        onSuccess()
                    }

                    override fun onFail(e: AppErrorResponseDto) {
                        _uiState.value = _uiState.value.copy(isLoading = false)
                    }
                }
            )
        }
    }

    private fun validateEmployeeId(employeeId: String): LoginFieldError? {
        if (employeeId.isBlank()) return null
        val len = employeeId.trim().length
        return when {
            len != EMPLOYEE_ID_EXACT_LENGTH -> LoginFieldError.InvalidLength
            else -> null
        }
    }

    private fun validatePassword(password: String): LoginFieldError? {
        if (password.isEmpty()) return null
        val hasUpperCase = password.any { it.isUpperCase() }
        val hasLowerCase = password.any { it.isLowerCase() }
        val hasDigit = password.any { it.isDigit() }
        val hasSpecialChar = password.any { !it.isLetterOrDigit() }
        val isValidLength = password.length in 8..16

        return when {
            !isValidLength || !hasUpperCase || !hasLowerCase || !hasDigit || !hasSpecialChar -> {
                null
            }

            else -> null
        }
    }

    fun reset() {
        _uiState.value = LoginUiState()
    }
}
