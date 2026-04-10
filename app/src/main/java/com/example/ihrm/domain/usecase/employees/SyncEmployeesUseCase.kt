package com.example.ihrm.domain.usecase.employees

import com.example.ihrm.core.usecase.BaseUseCase
import com.example.ihrm.data.remote.base.NetworkResult
import com.example.ihrm.data.remote.base.PaginatedApiData
import com.example.ihrm.data.remote.dto.UserMetaResponseDto
import com.example.ihrm.data.remote.mapper.parseIsoToLong
import com.example.ihrm.domain.model.Employee
import com.example.ihrm.domain.model.EmployeeUiModel
import com.example.ihrm.domain.repository.EmployeeRepository
import com.example.ihrm.domain.repository.LanguageRepository
import com.example.ihrm.util.Constants.DASH
import com.example.ihrm.util.getTodayDate
import javax.inject.Inject

class SyncEmployeesUseCase @Inject constructor(
    private val repository: EmployeeRepository
): BaseUseCase() {
    @Inject
    override lateinit var languageRepository: LanguageRepository

    suspend operator fun invoke(
        search: String?,
        page: Int?,
        limit: Int?,
        orderBy: String?,
        sortBy: String?,
        type: String?,
        groupId: String?,
        isLeader: Boolean?,
        status: String?,
        jobTitles: List<String>?,
    ): NetworkResult<PaginatedApiData<List<EmployeeListDto>>> {
        val result = repository.getEmployeesList(
            search = search,
            page = page,
            limit = limit,
            orderBy = orderBy,
            sortBy = sortBy,
            type = type,
            groupId = groupId,
            isLeader = isLeader,
            status = status,
            jobTitles = jobTitles
        )
        return translateResponse(result)
    }
}
