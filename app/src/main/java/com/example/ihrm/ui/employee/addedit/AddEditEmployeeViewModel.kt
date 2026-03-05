package com.example.ihrm.ui.employee.addedit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ihrm.domain.model.Employee
import com.example.ihrm.domain.usecase.AddEmployeeUseCase
import com.example.ihrm.domain.usecase.GetEmployeeByIdUseCase
import com.example.ihrm.domain.usecase.UpdateEmployeeUseCase
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
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddEditEmployeeUiState())
    val uiState: StateFlow<AddEditEmployeeUiState> = _uiState.asStateFlow()

    fun loadEmployee(employeeId: String?) {
        if (employeeId == null) {
            // Creating new employee
            _uiState.value = AddEditEmployeeUiState()
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                getEmployeeByIdUseCase(employeeId).collect { employee ->
                    _uiState.value = _uiState.value.copy(
                        employee = employee,
                        isLoading = false
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

    fun saveEmployee(
        name: String,
        email: String,
        phone: String,
        department: String?,
        position: String?,
        hireDate: String?,
        salary: Double?,
        address: String?,
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
                    department = department,
                    position = position,
                    hireDate = hireDate,
                    salary = salary,
                    address = address,
                    updatedAt = currentTime
                )
            } else {
                // Create new employee
                Employee(
                    id = UUID.randomUUID().toString(),
                    name = name,
                    email = email,
                    phone = phone,
                    department = department,
                    position = position,
                    hireDate = hireDate,
                    salary = salary,
                    address = address,
                    createdAt = currentTime,
                    updatedAt = currentTime
                )
            }

            val useCase = if (_uiState.value.employee != null) {
                updateEmployeeUseCase(employee)
            } else {
                addEmployeeUseCase(employee)
            }

            useCase.fold(
                onSuccess = {
                    _uiState.value = _uiState.value.copy(
                        isSaving = false,
                        isSuccess = true
                    )
                    onSuccess()
                },
                onFailure = { exception ->
                    _uiState.value = _uiState.value.copy(
                        isSaving = false,
                        error = exception.message ?: "Failed to save employee"
                    )
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