package com.example.ihrm.domain.usecase.employees

import com.example.ihrm.domain.model.Employee
import com.example.ihrm.domain.repository.EmployeeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetEmployeesUseCase @Inject constructor(
    private val repository: EmployeeRepository
) {
    operator fun invoke(): Flow<List<Employee>> = repository.getAllEmployees()
}