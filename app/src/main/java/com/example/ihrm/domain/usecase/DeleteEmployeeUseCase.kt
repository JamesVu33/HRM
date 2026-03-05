package com.example.ihrm.domain.usecase

import com.example.ihrm.domain.repository.EmployeeRepository
import javax.inject.Inject

class DeleteEmployeeUseCase @Inject constructor(
    private val repository: EmployeeRepository
) {
    suspend operator fun invoke(id: String): Result<Unit> {
        return repository.deleteEmployee(id)
    }
}