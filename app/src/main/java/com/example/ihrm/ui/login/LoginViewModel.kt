package com.example.ihrm.ui.login

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.ihrm.core.errorHandler.CommonErrorException
import com.example.ihrm.core.viewmodel.BaseViewmodel
import com.example.ihrm.core.viewmodel.CallbackWrapper
import com.example.ihrm.data.remote.base.AppErrorResponse
import com.example.ihrm.data.remote.login.LoginResponse
import com.example.ihrm.domain.repository.AuthRepository
import com.example.ihrm.domain.usecase.login.LoginUseCase
import com.example.ihrm.util.AuthManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
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
    private val loginUseCase: LoginUseCase
) : BaseViewmodel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    /**
     * UI update account ID (employee id) event
     */
    fun updateEmployeeId(employeeId: String) {
//        val error = validateEmployeeId(employeeId)
        _uiState.value = _uiState.value.copy(
            employeeId = employeeId,
            employeeIdError = null,
            loginError = null
        )
    }

    /**
     * UI update password event
     */
    fun updatePassword(password: String) {
//        val error = validatePassword(password)
        _uiState.value = _uiState.value.copy(
            password = password,
            passwordError = null,
            loginError = null
        )
    }

    /**
     * UI update password display event
     */
    fun togglePasswordVisibility() {
        _uiState.value = _uiState.value.copy(
            isPasswordVisible = !_uiState.value.isPasswordVisible
        )
    }

    /**
     * Login event
     */
    fun login(onSuccess: () -> Unit) {
        viewModelScope.launch(Dispatchers.Default) {
            val employeeId = _uiState.value.employeeId.trim()
            val password = _uiState.value.password

            val errorPwd = validatePassword(password)
            val errorId = validateEmployeeId(employeeId)
            if (errorPwd != null || errorId != null) {
                _uiState.value = _uiState.value.copy(
                    employeeIdError = errorId,
                    passwordError = errorPwd
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
                fetching = { loginUseCase.login(employeeId, password) },
                callbackWrapper = object : CallbackWrapper<LoginResponse> {
                    override fun onSuccess(data: LoginResponse) {
                        Log.d("loginTest", "onSuccess: $data")
                        AuthManager.saveTokens(data)
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            isLoginSuccess = true
                        )
                        onSuccess()
                    }

                    override fun onFail(e: CommonErrorException) {
                        _uiState.value = _uiState.value.copy(isLoading = false)
                        Log.d("loginTest", "onFail: ${e.errorMsg}")
                    }
                }
            )
        }
    }

    /**
     * Validate employeeId
     */
    private fun validateEmployeeId(employeeId: String): LoginFieldError? {
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
    private fun validatePassword(password: String): LoginFieldError? {
       return when {
            password.isEmpty() -> null
            password.length < 8 -> LoginFieldError.TooShort
            else -> null
        }
    }

    fun reset() {
        _uiState.value = LoginUiState()
    }
}
