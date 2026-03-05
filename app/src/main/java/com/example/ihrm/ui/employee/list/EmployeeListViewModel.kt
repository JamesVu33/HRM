package com.example.ihrm.ui.employee.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ihrm.domain.model.Employee
import com.example.ihrm.domain.usecase.DeleteEmployeeUseCase
import com.example.ihrm.domain.usecase.GetEmployeesUseCase
import com.example.ihrm.domain.usecase.SyncEmployeesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class EmployeeListUiState(
    val employees: List<Employee> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val isRefreshing: Boolean = false
)

@HiltViewModel
class EmployeeListViewModel @Inject constructor(
    private val getEmployeesUseCase: GetEmployeesUseCase,
    private val deleteEmployeeUseCase: DeleteEmployeeUseCase,
    private val syncEmployeesUseCase: SyncEmployeesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(EmployeeListUiState())
    val uiState: StateFlow<EmployeeListUiState> = _uiState.asStateFlow()

    init {
        loadEmployees()
    }

    private fun loadEmployees() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true, error = null)
            }
//            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                getEmployeesUseCase().collect { employees ->
//                    _uiState.value = _uiState.value.copy(
//                        employees = employees,
//                        isLoading = false
//                    )
                    _uiState.update {
                        it.copy(employees = employees, isLoading = false)
                    }
                }
            } catch (e: Exception) {
//                _uiState.value = _uiState.value.copy(
//                    isLoading = false,
//                    error = e.message ?: "Failed to load employees"
//                )
                _uiState.update {
                    it.copy(
                        error = e.message ?: "Failed to load employees",
                        isLoading = false
                    )
                }
            }
        }
    }

    fun refreshEmployees() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isRefreshing = true)
            syncEmployeesUseCase().fold(
                onSuccess = {
                    _uiState.value = _uiState.value.copy(isRefreshing = false)
                },
                onFailure = { exception ->
                    _uiState.value = _uiState.value.copy(
                        isRefreshing = false,
                        error = exception.message ?: "Failed to sync employees"
                    )
                }
            )
        }
    }

    fun deleteEmployee(employeeId: String) {
        viewModelScope.launch {
            deleteEmployeeUseCase(employeeId).fold(
                onSuccess = {
                    // Employee list will automatically update via Flow
                },
                onFailure = { exception ->
                    _uiState.value = _uiState.value.copy(
                        error = exception.message ?: "Failed to delete employee"
                    )
                }
            )
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}