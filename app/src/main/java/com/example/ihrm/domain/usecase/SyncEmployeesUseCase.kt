package com.example.ihrm.domain.usecase

import com.example.ihrm.data.remote.dto.NetworkResult
import com.example.ihrm.data.remote.dto.UserMetaResponseDto
import com.example.ihrm.domain.repository.EmployeeRepository
import javax.inject.Inject

class SyncEmployeesUseCase @Inject constructor(
    private val repository: EmployeeRepository
) {
    /** When [meta] is not null (e.g. user level S1/S2), employees are mapped with meta for level/role/title names. */
    suspend operator fun invoke(meta: UserMetaResponseDto? = null): NetworkResult<Unit> {
        return repository.syncEmployees(meta)
    }
}
