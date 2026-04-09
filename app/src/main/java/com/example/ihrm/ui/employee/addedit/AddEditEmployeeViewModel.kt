package com.example.ihrm.ui.employee.addedit

import androidx.lifecycle.viewModelScope
import com.example.ihrm.core.errorHandler.CommonErrorException
import com.example.ihrm.core.viewmodel.BaseViewmodel
import com.example.ihrm.core.viewmodel.CallbackWrapper
import com.example.ihrm.data.remote.base.NetworkResult
import com.example.ihrm.domain.model.Employee
import com.example.ihrm.domain.usecase.employees.AddEmployeeUseCase
import com.example.ihrm.domain.usecase.employees.GetEmployeeByIdUseCase
import com.example.ihrm.domain.usecase.employees.UpdateEmployeeUseCase
import com.example.ihrm.util.Constants.DASH
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

data class AddEditEmployeeUiState(
    val employee: Employee? = null,
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false
)

@HiltViewModel
class AddEditEmployeeViewModel @Inject constructor(
    private val getEmployeeByIdUseCase: GetEmployeeByIdUseCase,
    private val addEmployeeUseCase: AddEmployeeUseCase,
    private val updateEmployeeUseCase: UpdateEmployeeUseCase
) : BaseViewmodel() {

    private val _uiState = MutableStateFlow(AddEditEmployeeUiState())
    val uiState: StateFlow<AddEditEmployeeUiState> = _uiState.asStateFlow()

    fun loadEmployee(employeeId: String?) {
        if (employeeId == null) {
            // Creating new employee
            _uiState.value = AddEditEmployeeUiState()
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null, employee = null)
            when (val result = getEmployeeByIdUseCase(employeeId)) {
                is NetworkResult.Success -> {
                    _uiState.value = _uiState.value.copy(
                        employee = result.data,
                        isLoading = false
                    )
                }
                is NetworkResult.Failure -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = employeeLoadErrorMessage(result.error),
                        employee = null
                    )
                }
                is NetworkResult.Exception -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = employeeLoadErrorMessage(result.e),
                        employee = null
                    )
                }
            }
        }
    }

    private fun employeeLoadErrorMessage(e: CommonErrorException): String =
        e.errorMsg?.takeIf { it.isNotBlank() }
            ?: e.message?.takeIf { !it.isNullOrBlank() }
            ?: e.errorKey.takeIf { it.isNotBlank() }
            ?: "Failed to load employee"

    fun saveEmployee(
        name: String,
        email: String,
        phone: String,
        department: String?,
        position: String?,
        hireDate: String?,
        salary: Double?,
        address: String?,
        englishName: String? = null,
        gender: String? = null,
        personalId: String? = null,
        idIssueDate: String? = null,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSaving = true, error = null, isSuccess = false)

            val currentTime = System.currentTimeMillis()
            val employee = if (_uiState.value.employee != null) {
                // Update existing employee
                _uiState.value.employee!!.copy(
                    name = name,
                    email = email,
                    phone = phone,
                    department = department ?: DASH,
                    position = position ?: DASH,
                    statusWorking = hireDate ?: DASH,
                    salary = salary,
                    address = address,
                    englishName = englishName,
                    gender = gender,
                    personalId = personalId,
                    idIssueDate = idIssueDate,
                    updatedAt = currentTime
                )
            } else {
                // Create new employee
                Employee(
                    id = UUID.randomUUID().toString(),
                    name = name,
                    email = email,
                    phone = phone,
                    department = department ?: DASH,
                    position = position ?: DASH,
                    statusWorking = hireDate ?: DASH,
                    salary = salary,
                    address = address,
                    englishName = englishName,
                    gender = gender,
                    personalId = personalId,
                    idIssueDate = idIssueDate,
                    createdAt = currentTime,
                    updatedAt = currentTime,
                    levelId = 1,
                    role = DASH,
                    level =DASH
                )
            }

            val result = if (_uiState.value.employee != null) {
                updateEmployeeUseCase(employee)
            } else {
                addEmployeeUseCase(employee)
            }

            fetchData(
                fetching = { result },
                callbackWrapper = object : CallbackWrapper<Unit> {
                    override fun onSuccess(data: Unit) {
                        _uiState.value = _uiState.value.copy(
                            isSaving = false,
                            isSuccess = true
                        )
                        onSuccess()
                    }

                    override fun onFail(e: CommonErrorException) {
                        _uiState.value = _uiState.value.copy(isSaving = false)
                    }
                }
            )
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    fun clearSuccess() {
        _uiState.value = _uiState.value.copy(isSuccess = false)
    }
}
