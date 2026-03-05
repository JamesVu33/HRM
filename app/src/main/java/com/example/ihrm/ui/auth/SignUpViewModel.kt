package com.example.ihrm.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SignUpUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val emailError: String? = null,
    val passwordError: String? = null,
    val isPasswordVisible: Boolean = false,
    val isSignUpSuccess: Boolean = false
)

@HiltViewModel
class SignUpViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(SignUpUiState())
    val uiState: StateFlow<SignUpUiState> = _uiState.asStateFlow()

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

    private fun validateEmail(email: String): String? {
        if (email.isEmpty()) return null
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$".toRegex()
        return if (!emailRegex.matches(email)) {
            "Please enter a valid email address"
        } else {
            null
        }
    }

    private fun validatePassword(password: String): String? {
        if (password.isEmpty()) return null
        val hasUpperCase = password.any { it.isUpperCase() }
        val hasLowerCase = password.any { it.isLowerCase() }
        val hasDigit = password.any { it.isDigit() }
        val hasSpecialChar = password.any { !it.isLetterOrDigit() }
        val isValidLength = password.length in 8..16

        return when {
            !isValidLength || !hasUpperCase || !hasLowerCase || !hasDigit || !hasSpecialChar -> {
                "Password must be 8-16 characters and include uppercase, lowercase, number, and special character."
            }
            else -> null
        }
    }

    fun signUp(onSuccess: () -> Unit) {
        viewModelScope.launch {
            val email = _uiState.value.email.trim()
            val password = _uiState.value.password.trim()

            val emailError = validateEmail(email)
            val passwordError = validatePassword(password)

            if (email.isEmpty()) {
                _uiState.value = _uiState.value.copy(
                    emailError = "Email is required"
                )
                return@launch
            }

            if (password.isEmpty()) {
                _uiState.value = _uiState.value.copy(
                    passwordError = "Password is required"
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

            // Simulate signup API call
            kotlinx.coroutines.delay(1000)

            // TODO: Replace with actual registration logic
            if (email.isNotEmpty() && password.isNotEmpty()) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isSignUpSuccess = true
                )
                onSuccess()
            } else {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    emailError = "Registration failed"
                )
            }
        }
    }
}
