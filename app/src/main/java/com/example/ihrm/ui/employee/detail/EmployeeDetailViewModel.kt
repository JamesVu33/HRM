package com.example.ihrm.ui.employee.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ihrm.domain.model.Employee
import com.example.ihrm.domain.usecase.DeleteEmployeeUseCase
import com.example.ihrm.domain.usecase.GetEmployeeByIdUseCase
import com.example.ihrm.domain.usecase.UpdateEmployeeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class EmployeeDetailUiState(
    val employee: Employee? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val updateSuccess: Boolean = false
)

@HiltViewModel
class EmployeeDetailViewModel @Inject constructor(
    private val getEmployeeByIdUseCase: GetEmployeeByIdUseCase,
    private val deleteEmployeeUseCase: DeleteEmployeeUseCase,
    private val updateEmployeeUseCase: UpdateEmployeeUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(EmployeeDetailUiState())
    val uiState: StateFlow<EmployeeDetailUiState> = _uiState.asStateFlow()

    fun loadEmployee(employeeId: String) {
        viewModelScope.launch {
            val preserveSuccess = _uiState.value.updateSuccess
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                getEmployeeByIdUseCase(employeeId).collect { employee ->
                    _uiState.value = _uiState.value.copy(
                        employee = employee,
                        isLoading = false,
                        updateSuccess = preserveSuccess
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Failed to load employee"
                )
            }
        }
    }

    fun deleteEmployee(employeeId: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            deleteEmployeeUseCase(employeeId).fold(
                onSuccess = {
                    onSuccess()
                },
                onFailure = { exception ->
                    _uiState.value = _uiState.value.copy(
                        error = exception.message ?: "Failed to delete employee"
                    )
                }
            )
        }
    }

    fun updateEmployee(employee: Employee, onSuccess: () -> Unit) {
        viewModelScope.launch {
            updateEmployeeUseCase(employee).fold(
                onSuccess = {
                    _uiState.value = _uiState.value.copy(updateSuccess = true)
                    onSuccess()
                },
                onFailure = { e ->
                    _uiState.value = _uiState.value.copy(
                        error = e.message ?: "Failed to update"
                    )
                }
            )
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    fun clearUpdateSuccess() {
        _uiState.value = _uiState.value.copy(updateSuccess = false)
    }
}