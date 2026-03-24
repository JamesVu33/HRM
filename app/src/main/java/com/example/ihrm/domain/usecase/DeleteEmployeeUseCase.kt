package com.example.ihrm.domain.usecase

import com.example.ihrm.data.remote.base.NetworkResult
import com.example.ihrm.domain.repository.EmployeeRepository
import javax.inject.Inject

class DeleteEmployeeUseCase @Inject constructor(
    private val repository: EmployeeRepository
) {
    suspend operator fun invoke(id: String): NetworkResult<Unit> {
        return repository.deleteEmployee(id)
    }
}