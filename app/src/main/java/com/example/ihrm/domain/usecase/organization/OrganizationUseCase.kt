package com.example.ihrm.domain.usecase.organization

import com.example.ihrm.core.usecase.BaseUseCase
import com.example.ihrm.data.remote.base.NetworkResult
import com.example.ihrm.domain.model.Department
import com.example.ihrm.domain.model.toFlatDepartmentList
import com.example.ihrm.domain.repository.LanguageRepository
import com.example.ihrm.domain.repository.OrganizationRepository
import javax.inject.Inject

class OrganizationUseCase @Inject constructor(
    private val organizationRepository: OrganizationRepository
) : BaseUseCase() {
    @Inject
    override lateinit var languageRepository: LanguageRepository



    suspend fun getOrganizations(): NetworkResult<List<Department>> {
        val result = organizationRepository.getOrganizations().map { it.toFlatDepartmentList() }
        return translateResponse(result)
    }
}