package com.example.ihrm.ui.security.checks

import android.util.Log
import com.example.ihrm.core.errorHandler.CommonErrorException
import com.example.ihrm.core.viewmodel.BaseViewmodel
import com.example.ihrm.core.viewmodel.CallbackWrapper
import com.example.ihrm.domain.model.SecurityCheckSubmission
import com.example.ihrm.domain.model.SecurityCheckSubmissionsPage
import com.example.ihrm.domain.model.SecurityGroups
import com.example.ihrm.domain.model.SubmissionPaginationMeta
import com.example.ihrm.domain.usecase.securities.SecuritiesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class SecurityChecksUiState(
    val submissions: List<SecurityCheckSubmission> = emptyList(),
    val pagination: SubmissionPaginationMeta? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)

data class SecurityGroupsUiState(
    val securityGroups: List<SecurityGroups> = emptyList(),
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

    init {
        loadSubmissions()
        getGroups()
    }

    /**
     * GET /security-check/submissions — mặc định page=1, limit=100.
     */
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
                    Log.d("apiFlows", "success get submissions: $data")
                    _uiState.update {
                        it.copy(
                            submissions = data.items,
                            pagination = data.meta,
                            isLoading = false,
                            errorMessage = null,
                        )
                    }
                }

                override fun onFail(e: CommonErrorException) {
                    Log.d("apiFlows", "onFail get submissions: $e")
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = e.errorMsg
                                ?: e.message
                                ?: e.errorKey,
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
                    super.onSuccess(data)
                    Log.d("apiFlows", "code go here: data")
                    _uiStateSecurityGroups.update {
                        it.copy(
                            securityGroups = data
                        )
                    }
                }

                override fun onFail(e: CommonErrorException) {
                    super.onFail(e)
                    Log.d("apiFlows", "onFail: $e")
                }
            },
        )
    }

    private companion object {
        const val DEFAULT_PAGE = 1
        const val DEFAULT_LIMIT = 100
    }
}
