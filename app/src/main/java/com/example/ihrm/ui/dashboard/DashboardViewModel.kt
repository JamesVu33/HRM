package com.example.ihrm.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ihrm.domain.model.Employee
import com.example.ihrm.domain.usecase.DeleteEmployeeUseCase
import com.example.ihrm.domain.usecase.GetEmployeesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

data class DashboardUiState(
    val greeting: String = "",
    val dateText: String = "",
    val searchQuery: String = "",
    val totalEmployees: Int = 0,
    val activeToday: Int = 0,
    val employees: List<Employee> = emptyList(),
    val filteredEmployees: List<Employee> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val getEmployeesUseCase: GetEmployeesUseCase,
    private val deleteEmployeeUseCase: DeleteEmployeeUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    init {
        updateGreetingAndDate()
        loadEmployees()
    }

    private fun loadEmployees() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            getEmployeesUseCase()
                .catch { e ->
                    _uiState.update {
                        it.copy(error = e.message, isLoading = false)
                    }
                }
                .collect { employees ->
                    _uiState.update { state ->
                        state.copy(
                            employees = employees,
                            totalEmployees = employees.size,
                            activeToday = employees.size, // Placeholder: no "active today" in model
                            filteredEmployees = filterEmployees(employees, state.searchQuery),
                            isLoading = false
                        )
                    }
                }
        }
    }

    fun updateSearchQuery(query: String) {
        _uiState.update { state ->
            state.copy(
                searchQuery = query,
                filteredEmployees = filterEmployees(state.employees, query)
            )
        }
    }

    fun deleteEmployee(employeeId: String) {
        viewModelScope.launch {
            deleteEmployeeUseCase(employeeId).fold(
                onSuccess = { /* List updates via Flow */ },
                onFailure = { e ->
                    _uiState.update {
                        it.copy(error = e.message ?: "Failed to delete")
                    }
                }
            )
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    private fun updateGreetingAndDate() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val greeting = when {
            hour < 12 -> "Good Morning"
            hour < 17 -> "Good Afternoon"
            else -> "Good Evening"
        }
        val dateFormat = SimpleDateFormat("EEEE, d MMMM yyyy", Locale.getDefault())
        _uiState.update {
            it.copy(
                greeting = greeting,
                dateText = dateFormat.format(calendar.time)
            )
        }
    }

    private fun filterEmployees(employees: List<Employee>, query: String): List<Employee> {
        return filterEmployeesByQuery(employees, query)
    }
}

/**
 * Pure function for filtering employees by search query. Exposed for unit testing.
 */
internal fun filterEmployeesByQuery(employees: List<Employee>, query: String): List<Employee> {
    if (query.isBlank()) return employees
    val lower = query.lowercase()
    return employees.filter { emp ->
        emp.name.lowercase().contains(lower) ||
            (emp.position?.lowercase()?.contains(lower) == true) ||
            emp.email.lowercase().contains(lower)
    }
}
