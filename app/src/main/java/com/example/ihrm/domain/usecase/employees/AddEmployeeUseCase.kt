package com.example.ihrm.domain.usecase.employees

import com.example.ihrm.data.remote.base.NetworkResult
import com.example.ihrm.domain.model.Employee
import com.example.ihrm.domain.repository.EmployeeRepository
import javax.inject.Inject

class AddEmployeeUseCase @Inject constructor(
    private val repository: EmployeeRepository
) {
    suspend operator fun invoke(employee: Employee): NetworkResult<Unit> {
        return repository.addEmployee(employee)
    }
}