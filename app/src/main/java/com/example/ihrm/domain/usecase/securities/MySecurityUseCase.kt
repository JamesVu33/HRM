package com.example.ihrm.domain.usecase.securities

import com.example.ihrm.core.usecase.BaseUseCase
import com.example.ihrm.data.remote.base.NetworkResult
import com.example.ihrm.data.remote.securities.MySecurityCheckResponse
import com.example.ihrm.domain.repository.LanguageRepository
import com.example.ihrm.domain.repository.MySecurityCheckRepository
import com.example.ihrm.ui.security.mysecurity.MySecurityCheckUiState
import com.example.ihrm.util.Constants.DASH
import com.example.ihrm.util.formatDateTime
import com.example.ihrm.util.legendByKey
import javax.inject.Inject

class MySecurityUseCase @Inject constructor(
    private val mySecurityCheckRepository: MySecurityCheckRepository
) : BaseUseCase() {
    @Inject
    override lateinit var languageRepository: LanguageRepository

    suspend fun getMySecurityCheck(
        year: Int?,
        page: Int?,
        limit: Int?,
        orderBy: String?,
        sortBy: String?,
        status: String?,
    ): NetworkResult<List<MySecurityCheckResponse>> {
        val result = mySecurityCheckRepository.getMySecurityCheck(
            year = year,
            page = page,
            limit = limit,
            orderBy = orderBy,
            sortBy = sortBy,
            status = status
        )
        return translateResponse(result)
    }

    fun mapToUiState(
        detail: MySecurityCheckResponse,
    ): MySecurityCheckUiState {
        val status = legendByKey(detail.status ?: "")
        return MySecurityCheckUiState(
            id = detail.id.toString(),
            userName = detail.user?.fullName ?: DASH,
            employeeId = detail.user?.employeeId ?: DASH,
            status = status,
            submittedAt = detail.submittedAt.formatDateTime(),
            templateName = detail.template?.templateName ?: DASH,
        )
    }
}
