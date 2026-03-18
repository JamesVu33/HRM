package com.example.ihrm.ui.loginTest

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ihrm.R
import com.example.ihrm.domain.repository.AuthRepository
import com.example.ihrm.util.AuthManager
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

data class LoginTestUiState(
    val employeeId: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val employeeIdError: LoginTestFieldError? = null,
    val passwordError: LoginTestFieldError? = null,
    val isLoginSuccess: Boolean = false,
    val isPasswordVisible: Boolean = false,
    val loginError: String? = null
)

/** API requires employeeId exactly 8 characters. */
const val EMPLOYEE_ID_EXACT_LENGTH = 8

sealed interface LoginTestFieldError {
    data object Required : LoginTestFieldError
    data object TooShort : LoginTestFieldError
    data object InvalidLength : LoginTestFieldError  // not exactly 8 chars
    data object InvalidRules : LoginTestFieldError
}

@HiltViewModel
class LoginTestViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginTestUiState())
    val uiState: StateFlow<LoginTestUiState> = _uiState.asStateFlow()

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
                    employeeIdError = LoginTestFieldError.Required
                )
                return@launch
            }

            if (password.isEmpty()) {
                _uiState.value = _uiState.value.copy(
                    passwordError = LoginTestFieldError.Required
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

            val result = authRepository.login(employeeId, password)

            _uiState.value = _uiState.value.copy(isLoading = false)

            result.fold(
                onSuccess = { data ->
                    AuthManager.saveTokens(data)
                    _uiState.value = _uiState.value.copy(isLoginSuccess = true)
                    onSuccess()
                },
                onFailure = { e ->
                    val msg = e.message?.lowercase() ?: ""
                    val isNetworkOrUnreachable = e is UnknownHostException
                            || e is SocketTimeoutException
                            || e is ConnectException
                            || msg.contains("unable to resolve host", ignoreCase = true)
                            || msg.contains("no address associated with the hostname", ignoreCase = true)
                            || msg.contains("connection refused", ignoreCase = true)
                            || msg.contains("failed to connect", ignoreCase = true)
                            || msg.contains("timeout", ignoreCase = true)
                            || msg.contains("timed out", ignoreCase = true)
                    val message = if (isNetworkOrUnreachable) {
                        context.getString(R.string.login_test_error_network)
                    } else {
                        e.message ?: context.getString(R.string.login_test_error_api)
                    }
                    _uiState.value = _uiState.value.copy(loginError = message)
                }
            )
        }
    }

    private fun validateEmployeeId(employeeId: String): LoginTestFieldError? {
        if (employeeId.isBlank()) return null
        val len = employeeId.trim().length
        return when {
            len != EMPLOYEE_ID_EXACT_LENGTH -> LoginTestFieldError.InvalidLength
            else -> null
        }
    }

    private fun validatePassword(password: String): LoginTestFieldError? {
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
        _uiState.value = LoginTestUiState()
    }
}
