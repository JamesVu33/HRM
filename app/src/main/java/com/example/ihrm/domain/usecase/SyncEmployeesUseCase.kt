package com.example.ihrm.domain.usecase

import com.example.ihrm.domain.repository.EmployeeRepository
import javax.inject.Inject

class SyncEmployeesUseCase @Inject constructor(
    private val repository: EmployeeRepository
) {
    suspend operator fun invoke(): Result<Unit> {
        return repository.syncEmployees()
    }
}