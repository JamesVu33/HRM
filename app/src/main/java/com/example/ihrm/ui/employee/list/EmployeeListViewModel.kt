package com.example.ihrm.ui.employee.list

import com.example.ihrm.core.errorHandler.CommonErrorException
import com.example.ihrm.core.viewmodel.BaseViewmodel
import com.example.ihrm.core.viewmodel.CallbackWrapper
import com.example.ihrm.data.remote.mapper.toEmployeeUiModel
import com.example.ihrm.domain.model.Employee
import com.example.ihrm.domain.model.EmployeeUiModel
import com.example.ihrm.domain.model.Level
import com.example.ihrm.domain.usecase.employees.DeleteEmployeeUseCase
import com.example.ihrm.domain.usecase.employees.EmployeeListDto
import com.example.ihrm.domain.usecase.employees.GetEmployeesUseCase
import com.example.ihrm.domain.usecase.employees.GetLevelByIdUseCase
import com.example.ihrm.domain.usecase.employees.SyncEmployeesUseCase
import com.example.ihrm.util.Constants.DEFAULT_LIMIT
import com.example.ihrm.util.Constants.DEFAULT_PAGE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

/** UI state cho màn danh sách nhân viên: EmployeeDepartmentResponse + Level (gộp từ 2 API). */
data class EmployeeListUiState(
    val employeeUiModels: List<EmployeeUiModel> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val isRefreshing: Boolean = false
)

@HiltViewModel
class EmployeeListViewModel @Inject constructor(
    private val syncEmployeesUseCase: SyncEmployeesUseCase
) : BaseViewmodel() {

    private val _uiState = MutableStateFlow(EmployeeListUiState())
    val uiState: StateFlow<EmployeeListUiState> = _uiState.asStateFlow()

    fun refreshEmployees() {
        _uiState.update { it.copy(isLoading = true, error = null) }
        fetchData(
            fetching = { syncEmployeesUseCase(
                search = null,
                page = DEFAULT_PAGE,
                limit = DEFAULT_LIMIT,
                orderBy = null,
                sortBy = null,
                type = null,
                groupId = null,
                isLeader = null,
                status = null,
                jobTitles = null
            ) },
            callbackWrapper = object : CallbackWrapper<List<EmployeeListDto>> {
                override fun onSuccess(data: List<EmployeeListDto>) {
                    super.onSuccess(data)
                    _uiState.update {
                        it.copy(
                            employeeUiModels = data.map { dto -> dto.toEmployeeUiModel() },
                            isLoading = false,
                            isRefreshing = false
                        )
                    }
                }

                override fun onFail(e: CommonErrorException) {
                    super.onFail(e)
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = e.errorMsg ?: e.message ?: e.errorKey,
                        )
                    }
                }
            }
        )
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}

/**
 * Gộp danh sách EmployeeDepartmentResponse với levelMap (levelId -> Level). Pure function để dễ unit test.
 */
internal fun buildEmployeeUiModels(
    employees: List<Employee>,
    levelMap: Map<Int, Level>
): List<EmployeeUiModel> =
    employees.map { e -> EmployeeUiModel(employee = e, level = e.levelId?.let { levelMap[it] }) }
