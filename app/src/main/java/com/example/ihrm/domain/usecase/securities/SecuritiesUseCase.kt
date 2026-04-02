package com.example.ihrm.domain.usecase.securities

import com.example.ihrm.core.usecase.BaseUseCase
import com.example.ihrm.data.remote.base.NetworkResult
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

    suspend fun getSubmissions(page: Int, limit: Int): NetworkResult<SecurityCheckSubmissionsPage> {
        val result = securityCheckRepository.getSubmissions(page, limit)
        return translateResponse(result)
    }

    suspend fun getGroups(): NetworkResult<List<SecurityGroups>> {
        val result = securityCheckRepository.getGroups()
        return translateResponse(result)
    }
}