package com.example.ihrm.domain.repository

import com.example.ihrm.domain.model.Employee
import kotlinx.coroutines.flow.Flow

interface EmployeeRepository {
    fun getAllEmployees(): Flow<List<Employee>>
    fun getEmployeeById(id: String): Flow<Employee?>
    suspend fun addEmployee(employee: Employee): Result<Unit>
    suspend fun updateEmployee(employee: Employee): Result<Unit>
    suspend fun deleteEmployee(id: String): Result<Unit>
    suspend fun syncEmployees(): Result<Unit>
}