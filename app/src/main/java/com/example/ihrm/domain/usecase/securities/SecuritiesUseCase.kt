package com.example.ihrm.domain.usecase.securities

import com.example.ihrm.core.usecase.BaseUseCase
import com.example.ihrm.data.remote.base.NetworkResult
import com.example.ihrm.data.remote.dto.ChecksSecuritySubmissionResponse
import com.example.ihrm.domain.model.SecurityCheckSubmissionsPage
import com.example.ihrm.domain.model.SecurityGroups
import com.example.ihrm.domain.repository.LanguageRepository
import com.example.ihrm.domain.repository.SecurityCheckRepository
import javax.inject.Inject

class SecuritiesUseCase @Inject constructor(
    private val securityCheckRepository: SecurityCheckRepository,
) : BaseUseCase() {
    @Inject
    override lateinit var languageRepository: LanguageRepository

    suspend fun getSubmissions(
        fromDate: String?,
        toDate: String?,
        query: String?,
        page: Int?,
        limit: Int?,
        orderBy: String?,
        sortBy: String?,
        status: String?,
        type: String?,
        monthCode: String?,
        groupId: String?,
    ): NetworkResult<SecurityCheckSubmissionsPage> {
        val result = securityCheckRepository.getSubmissions(
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
        return translateResponse(result)
    }

    suspend fun getNotSubmissions(
        fromDate: String?,
        toDate: String?,
        query: String?,
        page: Int?,
        limit: Int?,
        orderBy: String?,
        sortBy: String?,
        status: String?,
        type: String?,
        monthCode: String?,
        groupId: String?,
    ): NetworkResult<SecurityCheckSubmissionsPage> {
        val result = securityCheckRepository.getNotSubmissions(
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
        return translateResponse(result)
    }

    suspend fun getGroups(): NetworkResult<List<SecurityGroups>> {
        val result = securityCheckRepository.getGroups()
        return translateResponse(result)
    }
}