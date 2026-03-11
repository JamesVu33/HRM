package com.example.ihrm.ui.loginTest

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LoginTestUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val emailError: LoginTestFieldError? = null,
    val passwordError: LoginTestFieldError? = null,
    val isLoginSuccess: Boolean = false,
    val isPasswordVisible: Boolean = false
)

sealed interface LoginTestFieldError {
    data object Required : LoginTestFieldError
    data object InvalidFormat : LoginTestFieldError
    data object InvalidRules : LoginTestFieldError
}

@HiltViewModel
class LoginTestViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(LoginTestUiState())
    val uiState: StateFlow<LoginTestUiState> = _uiState.asStateFlow()

    fun updateEmail(email: String) {
        val emailError = validateEmail(email)
        _uiState.value = _uiState.value.copy(
            email = email,
            emailError = emailError
        )
    }

    fun updatePassword(password: String) {
        val passwordError = validatePassword(password)
        _uiState.value = _uiState.value.copy(
            password = password,
            passwordError = passwordError
        )
    }

    fun togglePasswordVisibility() {
        _uiState.value = _uiState.value.copy(
            isPasswordVisible = !_uiState.value.isPasswordVisible
        )
    }

    fun login(onSuccess: () -> Unit) {
        viewModelScope.launch {
            val email = _uiState.value.email.trim()
            val password = _uiState.value.password.trim()

            val emailError = validateEmail(email)
            val passwordError = validatePassword(password)

            if (email.isEmpty()) {
                _uiState.value = _uiState.value.copy(
                    emailError = LoginTestFieldError.Required
                )
                return@launch
            }

            if (password.isEmpty()) {
                _uiState.value = _uiState.value.copy(
                    passwordError = LoginTestFieldError.Required
                )
                return@launch
            }

            if (emailError != null || passwordError != null) {
                _uiState.value = _uiState.value.copy(
                    emailError = emailError,
                    passwordError = passwordError
                )
                return@launch
            }

            _uiState.value = _uiState.value.copy(
                isLoading = true,
                emailError = null,
                passwordError = null
            )

            delay(1000)

            // TODO: Replace with real auth implementation.
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                isLoginSuccess = true
            )
            onSuccess()
        }
    }

    private fun validateEmail(email: String): LoginTestFieldError? {
        if (email.isEmpty()) return null
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()
        return if (!emailRegex.matches(email)) LoginTestFieldError.InvalidFormat else null
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
                LoginTestFieldError.InvalidRules
            }

            else -> null
        }
    }

    fun reset() {
        _uiState.value = LoginTestUiState()
    }
}

