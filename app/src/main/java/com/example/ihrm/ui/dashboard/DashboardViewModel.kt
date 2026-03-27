package com.example.ihrm.ui.dashboard

import android.os.Build
import androidx.lifecycle.viewModelScope
import com.example.ihrm.core.viewmodel.BaseViewmodel
import com.example.ihrm.core.viewmodel.CallbackWrapper
import com.example.ihrm.data.remote.dto.UserMetaResponseDto
import com.example.ihrm.domain.model.Employee
import com.example.ihrm.domain.model.Level
import com.example.ihrm.domain.usecase.employees.DeleteEmployeeUseCase
import com.example.ihrm.domain.usecase.employees.GetEmployeesMetaUseCase
import com.example.ihrm.domain.usecase.employees.GetEmployeesUseCase
import com.example.ihrm.domain.usecase.employees.GetLevelByEmployeeIdUseCase
import com.example.ihrm.domain.usecase.employees.GetMeEmployeeInfoUseCase
import com.example.ihrm.domain.usecase.employees.SyncEmployeesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalTime
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
    /** Map employeeId -> level code (lấy từ GET /employees/{id}). */
    val levelCodeByEmployeeId: Map<String, String> = emptyMap(),
    val isLoading: Boolean = false,
    val error: String? = null,
    /** true khi API trả 401: hiển thị thông báo đăng nhập lại thay vì raw "401". */
    val errorUnauthorized: Boolean = false
)

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val getEmployeesUseCase: GetEmployeesUseCase,
    private val syncEmployeesUseCase: SyncEmployeesUseCase,
    private val getLevelByEmployeeIdUseCase: GetLevelByEmployeeIdUseCase,
    private val getMeEmployeeInfoUseCase: GetMeEmployeeInfoUseCase,
    private val getEmployeesMetaUseCase: GetEmployeesMetaUseCase,
    private val deleteEmployeeUseCase: DeleteEmployeeUseCase
) : BaseViewmodel() {

    private val _uiState = MutableStateFlow(DashboardUiState())

    /** Cache employeeId -> level code; cập nhật khi có danh sách employees. */
    private val levelCodeByEmployeeId = MutableStateFlow<Map<String, String>>(emptyMap())
    private val levelCodeCache = mutableMapOf<String, String>()

    private val searchQuery = MutableStateFlow("")
    private val syncLoading = MutableStateFlow(false)
    private val syncError = MutableStateFlow<String?>(null)
    private val syncErrorUnauthorized = MutableStateFlow(false)

    /** Gộp 2 flow lỗi để combine tối đa 5 flow (combine không có overload 6 tham số). */
    private val syncErrorState = combine(syncError, syncErrorUnauthorized) { err, unauth -> err to unauth }

    private val employeesFlow = getEmployeesUseCase()

    /** Emit when returning to Dashboard to force reload from DB (e.g. after updating employee in detail). */
    private val refreshTrigger = MutableSharedFlow<Unit>(replay = 0)

    /** Merge continuous Flow with refresh: when refreshTrigger emits, re-collect from DB to get latest list. */
    private val employeesWithRefresh = merge(
        employeesFlow,
        refreshTrigger.flatMapLatest { getEmployeesUseCase() }
    )

    init {
        updateGreetingAndDate()
        loadEmployees()
        loadLevelCodesWhenEmployeesChange()
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

    val uiState: StateFlow<DashboardUiState> =
        combine(
            employeesWithRefresh,
            searchQuery,
            levelCodeByEmployeeId,
            syncLoading,
            syncErrorState
        ) { employees, query, levelCodes, loading, errorState ->

            val (error, errorUnauthorized) = errorState
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
                levelCodeByEmployeeId = levelCodes,
                totalEmployees = employees.size,
                activeToday = employees.size,
                isLoading = loading,
                error = error,
                errorUnauthorized = errorUnauthorized
            )
        }
            .stateIn(
                viewModelScope,
                SharingStarted.Eagerly,
                DashboardUiState(isLoading = true)
            )

    /** Fetches employees from API then refreshes the list and levels map. */
    fun refreshEmployees() {
        viewModelScope.launch {
            syncLoading.value = true
            syncError.value = null
            syncErrorUnauthorized.value = false
            val meta = getMetaIfS1OrS2()
            
            fetchData(
                fetching = { syncEmployeesUseCase(meta) },
                callbackWrapper = object : CallbackWrapper<Unit> {
                    override fun onSuccess(data: Unit) {
                        refreshTrigger.tryEmit(Unit)
                        syncLoading.value = false
                    }
                }
            )
        }
    }

    /** Khi danh sách employees đổi, gọi GET /employees/{id} cho từng id (chưa cache) để lấy level code. */
    private fun loadLevelCodesWhenEmployeesChange() {
        viewModelScope.launch(Dispatchers.IO) {
            employeesWithRefresh.collect { employees ->
                val ids = employees.map { it.id }.distinct()
                val missing = ids.filter { it !in levelCodeCache }
                for (id in missing) {
                    fetchData(
                        fetching = { getLevelByEmployeeIdUseCase(id) },
                        callbackWrapper = object : CallbackWrapper<Level?> {
                            override fun onSuccess(data: Level?) {
                                data?.code?.let { code ->
                                    levelCodeCache[id] = code
                                }
                                levelCodeByEmployeeId.value = levelCodeCache.toMap()
                            }
                        },
                    )
                }
            }
        }
    }

    /** If current user level is S1 or S2, fetches and returns employees meta for mapping; otherwise returns null. */
    private suspend fun getMetaIfS1OrS2(): UserMetaResponseDto? {
        val meResult = getMeEmployeeInfoUseCase().getOrNull() ?: return null
        val levelCode = meResult.level?.code ?: return null
        if (levelCode != "S1" && levelCode != "S2") return null
        return getEmployeesMetaUseCase().getOrNull()
    }

    fun updateSearchQuery(query: String) {
        searchQuery.value = query
    }

    fun deleteEmployee(id: String) {
        fetchData(
            fetching = { deleteEmployeeUseCase(id) },
            callbackWrapper = object : CallbackWrapper<Unit> {
                override fun onSuccess(data: Unit) {
                    refreshEmployees()
                }
            }
        )
    }


    private fun getGreeting(): String {
        val hour = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalTime.now().hour
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
            val formatter = DateTimeFormatter.ofPattern(
                "EEEE, dd MMM yyyy",
                Locale.getDefault()
            )
            LocalDate.now().format(formatter)
        } else {
            val formatter = SimpleDateFormat(
                "EEEE, dd MMM yyyy",
                Locale.getDefault()
            )
            formatter.format(Date())
        }
    }

    fun clearError() {
        syncError.value = null
        syncErrorUnauthorized.value = false
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
