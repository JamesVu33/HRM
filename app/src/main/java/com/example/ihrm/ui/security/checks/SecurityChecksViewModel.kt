package com.example.ihrm.ui.security.checks

import android.util.Log
import com.example.ihrm.core.errorHandler.CommonErrorException
import com.example.ihrm.core.viewmodel.BaseViewmodel
import com.example.ihrm.core.viewmodel.CallbackWrapper
import com.example.ihrm.data.remote.securities.SecuritySubmissionRequest
import com.example.ihrm.domain.model.SecurityCheckSubmission
import com.example.ihrm.domain.model.SecurityCheckSubmissionsPage
import com.example.ihrm.domain.model.SecurityGroups
import com.example.ihrm.domain.model.SubmissionPaginationMeta
import com.example.ihrm.domain.model.securitycheck.SecuritySubmission
import com.example.ihrm.domain.model.securitycheck.SecurityTemplate
import com.example.ihrm.domain.model.securitycheck.SubmissionStat
import com.example.ihrm.domain.model.securitycheck.SubmissionStatus
import com.example.ihrm.domain.usecase.securities.SecuritiesUseCase
import com.example.ihrm.ui.common.toast.ToastState
import com.example.ihrm.ui.common.toast.ToastType
import com.example.ihrm.util.Constants.DEFAULT_LIMIT
import com.example.ihrm.util.Constants.DEFAULT_PAGE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class SecurityChecksUiState(
    val submissions: List<SecurityCheckSubmission> = emptyList(),
    val notSubmission: List<SecurityCheckSubmission> = emptyList(),
    val pagination: SubmissionPaginationMeta? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val approvedCount: Int = 0,
    val submittedCount: Int = 0,
    val rejectedCount: Int = 0,
    val notSubmittedCount: Int = 0,
)

data class SecurityGroupsUiState(
    val securityGroups: List<SecurityGroups> = emptyList(),
)

data class SecurityTemplateUiState(
    val template: SecurityTemplate? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)

@HiltViewModel
class SecurityChecksViewModel @Inject constructor(
    private val securitiesUseCase: SecuritiesUseCase,
) : BaseViewmodel() {

    private val _uiState = MutableStateFlow(SecurityChecksUiState())
    val uiState: StateFlow<SecurityChecksUiState> = _uiState.asStateFlow()
    private val _uiStateSecurityGroups = MutableStateFlow(SecurityGroupsUiState())
    val uiStateSecurityGroups: StateFlow<SecurityGroupsUiState> =
        _uiStateSecurityGroups.asStateFlow()

    private val _templateUiState = MutableStateFlow(SecurityTemplateUiState())
    val templateUiState: StateFlow<SecurityTemplateUiState> = _templateUiState.asStateFlow()

    private val _isPostSubmissionSuccess = MutableStateFlow(false)
    val isPostSubmissionSuccess: StateFlow<Boolean> = _isPostSubmissionSuccess.asStateFlow()

    init {
        getGroups()
    }

    /**
     * Đồng bộ: GET stats + danh sách (submissions hoặc missing) theo [summaryTab],
     * cùng bộ [filters] + [searchQuery] (đã debounce ở UI nếu cần).
     */
    fun loadSecurityChecksData(
        searchQuery: String,
        filters: SecurityChecksActiveFilters,
        summaryTab: SecuritySummaryFilter,
    ) {
        val api = filters.toApiQuery(searchQuery)
        loadSubmissionStats(api)
        when (summaryTab) {
            SecuritySummaryFilter.NOT_SUBMITTED -> loadNotSubmissionsInternal(api)
            SecuritySummaryFilter.APPROVED -> loadSubmissionsInternal(api, status = "APPROVED")
            SecuritySummaryFilter.SUBMITTED -> loadSubmissionsInternal(api, status = "SUBMITTED")
            SecuritySummaryFilter.REJECTED -> loadSubmissionsInternal(api, status = "REJECTED")
        }
    }

    private fun loadSubmissionStats(api: SecurityChecksApiQuery) {
        fetchData(
            onLoading = {},
            fetching = {
                securitiesUseCase.getSubmissionStat(
                    fromDate = api.fromDate,
                    toDate = api.toDate,
                    query = api.query,
                    type = api.type,
                    monthCode = api.monthCode,
                    groupId = api.groupId,
                )
            },
            callbackWrapper = object : CallbackWrapper<List<SubmissionStat>> {
                override fun onSuccess(data: List<SubmissionStat>) {
                    _uiState.update { state ->
                        state.copy(
                            approvedCount = data.countFor(SubmissionStatus.APPROVED),
                            submittedCount = data.countFor(SubmissionStatus.SUBMITTED),
                            rejectedCount = data.countFor(SubmissionStatus.REJECTED),
                            notSubmittedCount = data.countFor(SubmissionStatus.NOT_SUBMITTED),
                        )
                    }
                }

                override fun onFail(e: CommonErrorException) {
                    Log.d("apiFlows", "getSubmissionStat onFail: $e")
                }
            },
        )
    }

    private fun loadSubmissionsInternal(
        api: SecurityChecksApiQuery,
        status: String,
        page: Int = DEFAULT_PAGE,
        limit: Int = DEFAULT_LIMIT,
    ) {
        _uiState.update {
            it.copy(
                submissions = emptyList(),
                pagination = null,
                isLoading = true,
                errorMessage = null,
            )
        }
        fetchData(
            onLoading = {},
            fetching = {
                securitiesUseCase.getSubmissions(
                    fromDate = api.fromDate,
                    toDate = api.toDate,
                    query = api.query,
                    page = page,
                    limit = limit,
                    orderBy = null,
                    sortBy = null,
                    status = status,
                    type = api.type,
                    monthCode = api.monthCode,
                    groupId = api.groupId,
                )
            },
            callbackWrapper = object : CallbackWrapper<SecurityCheckSubmissionsPage> {
                override fun onSuccess(data: SecurityCheckSubmissionsPage) {
                    _uiState.update { state ->
                        state.copy(
                            submissions = data.items,
                            pagination = data.meta,
                            isLoading = false,
                            errorMessage = null,
                        )
                    }
                }

                override fun onFail(e: CommonErrorException) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = e.errorMsg ?: e.message ?: e.errorKey,
                        )
                    }
                }
            },
        )
    }

    private fun loadNotSubmissionsInternal(
        api: SecurityChecksApiQuery,
        page: Int = DEFAULT_PAGE,
        limit: Int = DEFAULT_LIMIT,
    ) {
        _uiState.update {
            it.copy(
                notSubmission = emptyList(),
                isLoading = true,
                errorMessage = null,
            )
        }
        fetchData(
            onLoading = {},
            fetching = {
                securitiesUseCase.getNotSubmissions(
                    fromDate = api.fromDate,
                    toDate = api.toDate,
                    query = api.query,
                    page = page,
                    limit = limit,
                    orderBy = null,
                    sortBy = null,
                    status = null,
                    type = api.type,
                    monthCode = api.monthCode,
                    groupId = api.groupId,
                )
            },
            callbackWrapper = object : CallbackWrapper<SecurityCheckSubmissionsPage> {
                override fun onSuccess(data: SecurityCheckSubmissionsPage) {
                    _uiState.update {
                        it.copy(
                            notSubmission = data.items,
                            pagination = data.meta,
                            isLoading = false,
                            errorMessage = null,
                        )
                    }
                }

                override fun onFail(e: CommonErrorException) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = e.errorMsg ?: e.message ?: e.errorKey,
                        )
                    }
                }
            },
        )
    }

    fun getGroups() {
        fetchData(
            onLoading = {},
            fetching = { securitiesUseCase.getGroups() },
            callbackWrapper = object : CallbackWrapper<List<SecurityGroups>> {
                override fun onSuccess(data: List<SecurityGroups>) {
                    _uiStateSecurityGroups.update { it.copy(securityGroups = data) }
                }

                override fun onFail(e: CommonErrorException) {
                    Log.d("apiFlows", "onFail getGroups: $e")
                }
            },
        )
    }

    fun getCurrentSecurityTemplate() {
        _templateUiState.update { it.copy(isLoading = true, errorMessage = null) }
        fetchData(
            onLoading = {},
            fetching = { securitiesUseCase.getCurrentSecurityTemplate() },
            callbackWrapper = object : CallbackWrapper<SecurityTemplate> {
                override fun onSuccess(data: SecurityTemplate) {
                    _templateUiState.update { it.copy(template = data, isLoading = false) }
                }

                override fun onFail(e: CommonErrorException) {
                    _templateUiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = e.errorMsg ?: e.message ?: e.errorKey
                        )
                    }
                }
            }
        )
    }

    fun postSubmission(request: SecuritySubmissionRequest) {
        fetchData(
            onLoading = {},
            fetching = { securitiesUseCase.postSubmission(request) },
            callbackWrapper = object : CallbackWrapper<SecuritySubmission> {
                override fun onSuccess(data: SecuritySubmission) {
                    _isPostSubmissionSuccess.value = true
                    showToastMessage(
                        message = "Submission successful",
                        type = ToastType.SUCCESS,
                    )
                }

                override fun onFail(e: CommonErrorException) {
                    showToastMessage(
                        message = e.errorMsg ?: "Submission failed",
                        type = ToastType.ERROR,
                    )
                }
            }
        )
    }

    fun resetPostSubmissionSuccess() {
        _isPostSubmissionSuccess.value = false
    }
}

private fun List<SubmissionStat>.countFor(status: SubmissionStatus): Int =
    firstOrNull { it.status == status }?.count ?: 0
