package com.example.ihrm.ui.employee.detail

import androidx.lifecycle.viewModelScope
import com.example.ihrm.core.viewmodel.BaseViewmodel
import com.example.ihrm.core.viewmodel.CallbackWrapper
import com.example.ihrm.domain.model.Employee
import com.example.ihrm.domain.usecase.employees.DeleteEmployeeUseCase
import com.example.ihrm.domain.usecase.employees.GetEmployeeByIdUseCase
import com.example.ihrm.domain.usecase.employees.UpdateEmployeeUseCase
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
) : BaseViewmodel() {

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
        fetchData(
            fetching = { deleteEmployeeUseCase(employeeId) },
            callbackWrapper = object : CallbackWrapper<Unit> {
                override fun onSuccess(data: Unit) {
                    onSuccess()
                }
            }
        )
    }

    fun updateEmployee(employee: Employee, onSuccess: () -> Unit) {
        fetchData(
            fetching = { updateEmployeeUseCase(employee) },
            callbackWrapper = object : CallbackWrapper<Unit> {
                override fun onSuccess(data: Unit) {
                    _uiState.value = _uiState.value.copy(updateSuccess = true)
                    onSuccess()
                }
            }
        )
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    fun clearUpdateSuccess() {
        _uiState.value = _uiState.value.copy(updateSuccess = false)
    }
}
