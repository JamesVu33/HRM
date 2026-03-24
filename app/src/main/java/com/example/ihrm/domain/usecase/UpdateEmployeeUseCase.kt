package com.example.ihrm.domain.usecase

import com.example.ihrm.data.remote.dto.NetworkResult
import com.example.ihrm.domain.model.Employee
import com.example.ihrm.domain.repository.EmployeeRepository
import javax.inject.Inject

class UpdateEmployeeUseCase @Inject constructor(
    private val repository: EmployeeRepository
) {
    suspend operator fun invoke(employee: Employee): NetworkResult<Unit> {
        return repository.updateEmployee(employee)
    }
}
