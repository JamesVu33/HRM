package com.example.ihrm.domain.usecase

import com.example.ihrm.data.remote.dto.NetworkResult
import com.example.ihrm.data.remote.dto.UserMetaResponseDto
import com.example.ihrm.domain.repository.EmployeeRepository
import javax.inject.Inject

class GetEmployeesMetaUseCase @Inject constructor(
    private val repository: EmployeeRepository
) {
    suspend operator fun invoke(): NetworkResult<UserMetaResponseDto> =
        repository.getEmployeesMeta()
}
