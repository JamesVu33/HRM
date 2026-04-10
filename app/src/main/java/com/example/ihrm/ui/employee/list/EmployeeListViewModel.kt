package com.example.ihrm.ui.employee.list

import com.example.ihrm.core.errorHandler.CommonErrorException
import com.example.ihrm.core.viewmodel.BaseViewmodel
import com.example.ihrm.core.viewmodel.CallbackWrapper
import com.example.ihrm.data.remote.mapper.toEmployeeUiModel
import com.example.ihrm.domain.model.Employee
import com.example.ihrm.domain.model.EmployeeUiModel
import com.example.ihrm.domain.model.Level
import com.example.ihrm.data.remote.base.PaginatedApiData
import com.example.ihrm.data.remote.dto.MetaDto
import com.example.ihrm.domain.usecase.employees.EmployeeListDto
import com.example.ihrm.domain.usecase.employees.SyncEmployeesUseCase
import com.example.ihrm.util.Constants.DEFAULT_PAGE
import com.example.ihrm.util.Constants.EMPLOYEE_LIST_PAGE_LIMIT
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.concurrent.atomic.AtomicInteger
import javax.inject.Inject

/** UI state cho màn danh sách nhân viên: EmployeeDepartmentResponse + Level (gộp từ 2 API). */
data class EmployeeListUiState(
    val employeeUiModels: List<EmployeeUiModel> = emptyList(),
    /** Phân trang từ `meta` của GET employees list (null nếu API không gửi). */
    val listMeta: MetaDto? = null,
    val isLoading: Boolean = false,
    /** Đang tải trang tiếp theo (load more). */
    val isLoadingMore: Boolean = false,
    /** Còn trang để gọi load more (theo meta hoặc heuristic khi không có meta). */
    val hasMorePages: Boolean = false,
    val error: String? = null,
    val isRefreshing: Boolean = false
)

@HiltViewModel
class EmployeeListViewModel @Inject constructor(
    private val syncEmployeesUseCase: SyncEmployeesUseCase
) : BaseViewmodel() {
    private val _uiState = MutableStateFlow(EmployeeListUiState())
    val uiState: StateFlow<EmployeeListUiState> = _uiState.asStateFlow()

    /** Query đang áp dụng cho danh sách (đồng bộ với lần refresh mới nhất). */
    private var activeSearchQuery: String? = null

    /** Tăng mỗi lần refresh để bỏ qua response của request cũ khi user gõ nhanh. */
    private val latestRefreshId = AtomicInteger(0)

    init {
        refreshEmployees(search = null)
    }

    /**
     * Tải lại từ trang 1. [search] gửi lên API dưới dạng `query` (trim, rỗng → null).
     */
    fun refreshEmployees(search: String? = null) {
        val normalized = search?.trim()?.takeIf { it.isNotEmpty() }
        val requestId = latestRefreshId.incrementAndGet()
        activeSearchQuery = normalized
        _uiState.update {
            it.copy(
                isLoading = true,
                isLoadingMore = false,
                error = null,
            )
        }
        fetchData(
            fetching = {
                syncEmployeesUseCase(
                    search = normalized,
                    page = DEFAULT_PAGE,
                    limit = EMPLOYEE_LIST_PAGE_LIMIT,
                    orderBy = null,
                    sortBy = null,
                    type = null,
                    groupId = null,
                    isLeader = null,
                    status = null,
                    jobTitles = null
                )
            },
            callbackWrapper = object : CallbackWrapper<PaginatedApiData<List<EmployeeListDto>>> {
                override fun onSuccess(data: PaginatedApiData<List<EmployeeListDto>>) {
                    super.onSuccess(data)
                    if (requestId != latestRefreshId.get()) return
                    val rows = data.data.orEmpty()
                    _uiState.update {
                        it.copy(
                            employeeUiModels = rows.map { dto -> dto.toEmployeeUiModel() },
                            listMeta = data.meta,
                            hasMorePages = computeHasMorePages(data.meta, rows.size, EMPLOYEE_LIST_PAGE_LIMIT),
                            isLoading = false,
                            isLoadingMore = false,
                            isRefreshing = false
                        )
                    }
                }

                override fun onFail(e: CommonErrorException) {
                    super.onFail(e)
                    if (requestId != latestRefreshId.get()) return
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isLoadingMore = false,
                            error = e.errorMsg ?: e.message ?: e.errorKey,
                        )
                    }
                }
            }
        )
    }

    fun loadMoreEmployees() {
        val state = _uiState.value
        if (state.isLoading || state.isLoadingMore || !state.hasMorePages) return
        val nextPage = (state.listMeta?.page ?: DEFAULT_PAGE) + 1
        val searchSnapshot = activeSearchQuery
        val refreshGenSnapshot = latestRefreshId.get()
        _uiState.update { it.copy(isLoadingMore = true, error = null) }
        fetchData(
            onLoading = {},
            fetching = {
                syncEmployeesUseCase(
                    search = searchSnapshot,
                    page = nextPage,
                    limit = EMPLOYEE_LIST_PAGE_LIMIT,
                    orderBy = null,
                    sortBy = null,
                    type = null,
                    groupId = null,
                    isLeader = null,
                    status = null,
                    jobTitles = null
                )
            },
            callbackWrapper = object : CallbackWrapper<PaginatedApiData<List<EmployeeListDto>>> {
                override fun onSuccess(data: PaginatedApiData<List<EmployeeListDto>>) {
                    super.onSuccess(data)
                    if (refreshGenSnapshot != latestRefreshId.get()) return
                    if (searchSnapshot != activeSearchQuery) return
                    val newRows = data.data.orEmpty().map { dto -> dto.toEmployeeUiModel() }
                    _uiState.update { prev ->
                        val existingIds = prev.employeeUiModels.map { it.employee.id }.toSet()
                        val merged = prev.employeeUiModels + newRows.filter { it.employee.id !in existingIds }
                        prev.copy(
                            employeeUiModels = merged,
                            listMeta = data.meta,
                            hasMorePages = computeHasMorePages(data.meta, newRows.size, EMPLOYEE_LIST_PAGE_LIMIT),
                            isLoadingMore = false,
                        )
                    }
                }

                override fun onFail(e: CommonErrorException) {
                    super.onFail(e)
                    if (refreshGenSnapshot != latestRefreshId.get()) return
                    if (searchSnapshot != activeSearchQuery) return
                    _uiState.update {
                        it.copy(
                            isLoadingMore = false,
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
 * Còn trang kế: ưu tiên [MetaDto.page] / [MetaDto.totalPages]; nếu không có meta thì heuristic theo số bản ghi vừa tải.
 */
internal fun computeHasMorePages(meta: MetaDto?, fetchedItemCount: Int, pageLimit: Int): Boolean {
    if (meta != null && meta.totalPages > 0) {
        return meta.page < meta.totalPages
    }
    return fetchedItemCount >= pageLimit
}

/**
 * Gộp danh sách EmployeeDepartmentResponse với levelMap (levelId -> Level). Pure function để dễ unit test.
 */
internal fun buildEmployeeUiModels(
    employees: List<Employee>,
    levelMap: Map<Int, Level>
): List<EmployeeUiModel> =
    employees.map { e -> EmployeeUiModel(employee = e, level = e.levelId?.let { levelMap[it] }) }
