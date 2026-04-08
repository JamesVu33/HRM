package com.example.ihrm.domain.usecase.employees

import com.example.ihrm.core.usecase.BaseUseCase
import com.example.ihrm.data.remote.dto.MeEmployeeResponse
import com.example.ihrm.data.remote.base.NetworkResult
import com.example.ihrm.data.remote.securities.MySecurityCheckResponse
import com.example.ihrm.domain.repository.EmployeeRepository
import com.example.ihrm.domain.repository.LanguageRepository
import com.example.ihrm.domain.repository.MySecurityCheckRepository
import javax.inject.Inject

class GetMeEmployeeInfoUseCase @Inject constructor(
    private val repository: EmployeeRepository,
    private val mySecurityCheckRepository: MySecurityCheckRepository
): BaseUseCase() {
    override lateinit var languageRepository: LanguageRepository
    suspend operator fun invoke(): NetworkResult<MeEmployeeResponse> {
        val result = repository.getMeEmployeeInfo()
        return translateResponse(result)
    }

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
}
