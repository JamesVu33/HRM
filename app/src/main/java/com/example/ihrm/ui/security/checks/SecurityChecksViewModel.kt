package com.example.ihrm.ui.security.checks

import android.content.Context
import android.util.Log
import com.example.ihrm.R
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
import com.example.ihrm.domain.usecase.securities.SecuritiesUseCase
import com.example.ihrm.ui.common.toast.ToastPosition
import com.example.ihrm.ui.common.toast.ToastState
import com.example.ihrm.ui.common.toast.ToastType
import com.example.ihrm.util.Constants.DEFAULT_LIMIT
import com.example.ihrm.util.Constants.DEFAULT_PAGE
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
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
    @ApplicationContext private val appContext: Context,
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
        loadSubmissions()
        getGroups()
    }

    fun loadSubmissions(
        page: Int = DEFAULT_PAGE,
        limit: Int = DEFAULT_LIMIT,
        fromDate: String? = null,
        toDate: String? = null,
        query: String? = null,
        orderBy: String? = null,
        sortBy: String? = null,
        status: String? = null,
        type: String? = null,
        monthCode: String? = null,
        groupId: String? = null,
    ) {
        _uiState.value = _uiState.value.copy(
            submissions = emptyList(),
            pagination = null,
            isLoading = true,
            errorMessage = null,
        )
        fetchData(
            fetching = {
                securitiesUseCase.getSubmissions(
                    fromDate = fromDate,
                    toDate = toDate,
                    query = query,
                    page = page,
                    limit = limit,
                    orderBy = orderBy,
                    sortBy = sortBy,
                    status = status,
                    type = type,
                    monthCode = monthCode,
                    groupId = groupId,
                )
            },
            callbackWrapper = object : CallbackWrapper<SecurityCheckSubmissionsPage> {
                override fun onSuccess(data: SecurityCheckSubmissionsPage) {
                    _uiState.update { state ->
                        state.copy(
                            submissions = data.items,
                            pagination = data.meta,
                            isLoading = false,
                            errorMessage = null
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

    fun loadNotSubmissions(
        page: Int = DEFAULT_PAGE,
        limit: Int = DEFAULT_LIMIT,
        fromDate: String? = null,
        toDate: String? = null,
        query: String? = null,
        orderBy: String? = null,
        sortBy: String? = null,
        status: String? = null,
        type: String? = null,
        monthCode: String? = null,
        groupId: String? = null,
    ) {
        _uiState.update { it.copy(isLoading = true) }
        fetchData(
            fetching = {
                securitiesUseCase.getNotSubmissions(
                    fromDate = fromDate,
                    toDate = toDate,
                    query = query,
                    page = page,
                    limit = limit,
                    orderBy = orderBy,
                    sortBy = sortBy,
                    status = status,
                    type = type,
                    monthCode = monthCode,
                    groupId = groupId,
                )
            },
            callbackWrapper = object : CallbackWrapper<SecurityCheckSubmissionsPage> {
                override fun onSuccess(data: SecurityCheckSubmissionsPage) {
                    _uiState.update {
                        it.copy(
                            notSubmission = data.items,
                            pagination = data.meta,
                            isLoading = false,
                            notSubmittedCount = data.meta?.total ?: 0
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

    fun loadSecurityCheck(selectedSummaryFilter: SecuritySummaryFilter) {
        when (selectedSummaryFilter) {
            SecuritySummaryFilter.NOT_SUBMITTED -> loadNotSubmissions()
            SecuritySummaryFilter.APPROVED -> loadSubmissions(status = SecuritySummaryFilter.APPROVED.name)
            SecuritySummaryFilter.SUBMITTED -> loadSubmissions(status = SecuritySummaryFilter.SUBMITTED.name)
            SecuritySummaryFilter.REJECTED -> loadSubmissions(status = SecuritySummaryFilter.REJECTED.name)
        }
    }

    fun getCurrentSecurityTemplate() {
        _templateUiState.update { it.copy(isLoading = true, errorMessage = null) }
        fetchData(
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
