package com.example.ihrm.ui.login

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.ihrm.core.errorHandler.CommonErrorException
import com.example.ihrm.core.viewmodel.BaseViewmodel
import com.example.ihrm.core.viewmodel.CallbackWrapper
import com.example.ihrm.data.remote.login.LoginResponse
import com.example.ihrm.data.remote.login.PermissionResponse
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
)

/** API requires employeeId exactly 8 characters. */
const val EMPLOYEE_ID_EXACT_LENGTH = 8

sealed interface LoginFieldError {
    data object Required : LoginFieldError
    data object TooShort : LoginFieldError
    data object InvalidLength : LoginFieldError  // not exactly 8 chars
    data object InvalidRules : LoginFieldError
    data class ServerMsg(val msg: String) : LoginFieldError
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

            val errorPwd = loginUseCase.validatePassword(password)
            val errorId = loginUseCase.validateEmployeeId(employeeId)
            if (errorPwd != null || errorId != null) {
                _uiState.value = _uiState.value.copy(
                    employeeIdError = errorId,
                    passwordError = errorPwd
                )
                return@launch
            }

            val employeeIdError = loginUseCase.validateEmployeeId(employeeId)
            val passwordError = loginUseCase.validatePassword(password)
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
            )

            fetchData(
                fetching = { loginUseCase.login(employeeId, password) },
                callbackWrapper = object : CallbackWrapper<LoginResponse> {
                    override fun onSuccess(data: LoginResponse) {
                        Log.d("loginTest", "onSuccess: $data")
                        getPermission(
                            employeeId = data.roles.first().id,
                            onSuccess = onSuccess
                        )
                    }

                    override suspend fun doOnBackground(data: LoginResponse) {
                        loginUseCase.storeUserInfo(data)
                        AuthManager.saveTokens(data)
                    }

                    override fun onFail(e: CommonErrorException) {
                        _uiState.value = _uiState.value.copy(isLoading = false)
                        Log.d("loginTest", "onFail: ${e.errorMsg}")
                    }
                }
            )
        }
    }

    fun getPermission(
        employeeId: Int,
        onSuccess: () -> Unit
    ) {
        ///** GET /permissions/roles/{id} */
        /*fetchData(
            fetching = { loginUseCase.getPermission(employeeId) },
            callbackWrapper = object : CallbackWrapper<PermissionResponse> {
                override fun onSuccess(data: PermissionResponse) {
                    Log.d("loginTest", "onSuccess: $data")
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
        )*/
        fetchData(
            fetching = { loginUseCase.getPermission(employeeId) },
            callbackWrapper = object : CallbackWrapper<List<PermissionResponse>> {
                override fun onSuccess(data: List<PermissionResponse>) {
                    Log.d("loginTest", "onSuccess: $data")
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isLoginSuccess = true
                    )
                    if (data.isNotEmpty()) {
                        loginUseCase.updateUserInfoFrom(data.first())
                    }
                    onSuccess()
                }

                override fun onFail(e: CommonErrorException) {
//                        _uiState.value = _uiState.value.copy(isLoading = false,)
                    _uiState.value =
                        loginUseCase.getErrorField(_uiState.value, e).copy(isLoading = false)
                    Log.d("loginTest", "onFail: ${e.errorMsg}")
                }
            }
        )
    }

    fun reset() {
        _uiState.value = LoginUiState()
    }
}
