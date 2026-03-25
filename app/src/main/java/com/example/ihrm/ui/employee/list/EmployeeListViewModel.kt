package com.example.ihrm.ui.employee.list

import androidx.lifecycle.viewModelScope
import com.example.ihrm.core.viewmodel.BaseViewmodel
import com.example.ihrm.core.viewmodel.CallbackWrapper
import com.example.ihrm.domain.model.Employee
import com.example.ihrm.domain.model.EmployeeUiModel
import com.example.ihrm.domain.model.Level
import com.example.ihrm.domain.usecase.DeleteEmployeeUseCase
import com.example.ihrm.domain.usecase.GetEmployeesUseCase
import com.example.ihrm.domain.usecase.GetLevelByIdUseCase
import com.example.ihrm.domain.usecase.SyncEmployeesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/** UI state cho màn danh sách nhân viên: Employee + Level (gộp từ 2 API). */
data class EmployeeListUiState(
    val employeeUiModels: List<EmployeeUiModel> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val isRefreshing: Boolean = false
)

@HiltViewModel
class EmployeeListViewModel @Inject constructor(
    private val getEmployeesUseCase: GetEmployeesUseCase,
    private val getLevelByIdUseCase: GetLevelByIdUseCase,
    private val deleteEmployeeUseCase: DeleteEmployeeUseCase,
    private val syncEmployeesUseCase: SyncEmployeesUseCase
) : BaseViewmodel() {

    private val _uiState = MutableStateFlow(EmployeeListUiState())
    val uiState: StateFlow<EmployeeListUiState> = _uiState.asStateFlow()

    /**
     * Cache levelId -> Level để tránh gọi getLevelById trùng cho cùng id.
     * Chỉ cập nhật từ một coroutine (collect employees) nên không cần thread-safe.
     */
    private val levelCache = mutableMapOf<Int, Level>()

    init {
        loadEmployeesWithLevels()
    }

    /**
     * Flow: 1) Collect employees (Flow từ DB) -> 2) Lấy distinct levelIds -> 3) Gọi getLevelById
     * song song cho các id chưa có trong cache -> 4) Cập nhật cache -> 5) Build List<EmployeeUiModel> -> emit.
     */
    private fun loadEmployeesWithLevels() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                getEmployeesUseCase().collect { employees ->
                    val uiModels = mergeEmployeesWithLevels(employees)
                    _uiState.update {
                        it.copy(employeeUiModels = uiModels, isLoading = false)
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        error = e.message ?: "Failed to load employees",
                        isLoading = false
                    )
                }
            }
        }
    }

    /**
     * Gộp danh sách Employee với Level: chỉ gọi getLevelById cho levelId chưa có trong cache,
     * gọi song song (async) để tối ưu performance.
     */
    private suspend fun mergeEmployeesWithLevels(employees: List<Employee>): List<EmployeeUiModel> =
        coroutineScope {
            val levelIds = employees.mapNotNull { it.levelId }.distinct()
            val missingIds = levelIds.filter { it !in levelCache }
            if (missingIds.isNotEmpty()) {
                val fetched = missingIds.map { id ->
                    async { getLevelByIdUseCase(id).getOrNull() }
                }.awaitAll()
                missingIds.zip(fetched).forEach { (id, level) ->
                    if (level != null) levelCache[id] = level
                }
            }

            buildEmployeeUiModels(employees, levelCache)
        }

    fun refreshEmployees() {
        fetchData(
            fetching = { syncEmployeesUseCase() },
            callbackWrapper = object : CallbackWrapper<Unit> {

            }
        )
    }

    fun deleteEmployee(employeeId: String) {
        fetchData(
            fetching = { deleteEmployeeUseCase(employeeId) },
            callbackWrapper = object : CallbackWrapper<Unit> {

            }
        )
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}

/**
 * Gộp danh sách Employee với levelMap (levelId -> Level). Pure function để dễ unit test.
 */
internal fun buildEmployeeUiModels(
    employees: List<Employee>,
    levelMap: Map<Int, Level>
): List<EmployeeUiModel> =
    employees.map { e -> EmployeeUiModel(employee = e, level = e.levelId?.let { levelMap[it] }) }
