package com.example.ihrm.ui.dashboard

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ihrm.domain.model.Employee
import com.example.ihrm.domain.usecase.DeleteEmployeeUseCase
import com.example.ihrm.domain.usecase.GetEmployeesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
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

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val getEmployeesUseCase: GetEmployeesUseCase,
    private val deleteEmployeeUseCase: DeleteEmployeeUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())

    init {
        updateGreetingAndDate()
        loadEmployees()
    }

    private fun loadEmployees() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            getEmployeesUseCase().collect { employees ->
                _uiState.update { state ->
                    val filtered = filterEmployees(employees, state.searchQuery)

                    state.copy(
                        employees = employees,
                        filteredEmployees = filtered,
                        totalEmployees = employees.size
                    )
                }
            }
        }
    }

    private val searchQuery = MutableStateFlow("")

    private val employeesFlow = getEmployeesUseCase()

    /** Emit when returning to Dashboard to force reload from DB (e.g. after updating employee in detail). */
    private val refreshTrigger = MutableSharedFlow<Unit>(replay = 0)

    /** Merge continuous Flow with refresh: when refreshTrigger emits, re-collect from DB to get latest list. */
    private val employeesWithRefresh = merge(
        employeesFlow,
        refreshTrigger.flatMapLatest { getEmployeesUseCase() }
    )

    val uiState: StateFlow<DashboardUiState> =
        combine(
            employeesWithRefresh,
            searchQuery
        ) { employees, query ->

            val filtered = if (query.isBlank()) {
                employees
            } else {
                employees.filter {
                    it.name.contains(query, ignoreCase = true)
                }
            }

            DashboardUiState(
                greeting = getGreeting(),
                dateText = getTodayDate(),
                searchQuery = query,
                employees = employees,
                filteredEmployees = filtered,
                totalEmployees = employees.size,
                activeToday = employees.size // demo
            )
        }
            .stateIn(
                viewModelScope,
                SharingStarted.Eagerly,
                DashboardUiState(isLoading = true)
            )

    /** Call when Dashboard is shown (e.g. after back from Detail) to reload list from DB. */
    fun refreshEmployees() {
        viewModelScope.launch {
            refreshTrigger.emit(Unit)
        }
    }

    fun updateSearchQuery(query: String) {
        searchQuery.value = query
    }

    fun deleteEmployee(id: String) {
        viewModelScope.launch {
            deleteEmployeeUseCase(id)
        }
    }


    private fun getGreeting(): String {
        val hour = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            java.time.LocalTime.now().hour
        } else {
            Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        }

        return when (hour) {
            in 5..11 -> "Good Morning"
            in 12..17 -> "Good Afternoon"
            in 18..21 -> "Good Evening"
            else -> "Good Night"
        }
    }

    private fun getTodayDate(): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val formatter = java.time.format.DateTimeFormatter.ofPattern(
                "EEEE, dd MMM yyyy",
                Locale.getDefault()
            )
            java.time.LocalDate.now().format(formatter)
        } else {
            val formatter = SimpleDateFormat(
                "EEEE, dd MMM yyyy",
                Locale.getDefault()
            )
            formatter.format(Date())
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
