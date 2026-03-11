package com.example.ihrm.data.repository

import com.example.ihrm.data.local.dao.EmployeeDao
import com.example.ihrm.data.remote.api.EmployeeApiService
import com.example.ihrm.data.remote.mapper.toEmployee
import com.example.ihrm.data.remote.mapper.toEmployeeEntity
import com.example.ihrm.domain.model.Employee
import com.example.ihrm.domain.repository.EmployeeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class EmployeeRepositoryImpl @Inject constructor(
    private val employeeDao: EmployeeDao,
    private val apiService: EmployeeApiService
) : EmployeeRepository {

    override fun getAllEmployees(): Flow<List<Employee>> {
        return employeeDao.getAllEmployees().map { entities ->
            entities.map { it.toEmployee() }
        }
    }

    override fun getEmployeeById(id: String): Flow<Employee?> {
        return employeeDao.getEmployeeById(id).map { it?.toEmployee() }
    }

    override suspend fun addEmployee(employee: Employee): Result<Unit> {
        return try {
            // Try to add to API first
            val dto = com.example.ihrm.data.remote.dto.EmployeeDto(
                id = employee.id,
                name = employee.name,
                email = employee.email,
                phone = employee.phone,
                department = employee.department,
                position = employee.position,
                hireDate = employee.hireDate,
                salary = employee.salary,
                address = employee.address,
                englishName = employee.englishName,
                gender = employee.gender,
                personalId = employee.personalId,
                idIssueDate = employee.idIssueDate,
                createdAt = employee.createdAt,
                updatedAt = employee.updatedAt
            )
            apiService.createEmployee(dto)
            
            // Save to local database
            employeeDao.insertEmployee(employee.toEmployeeEntity())
            Result.success(Unit)
        } catch (e: Exception) {
            // If API fails, still save locally
            try {
                employeeDao.insertEmployee(employee.toEmployeeEntity())
                Result.success(Unit)
            } catch (dbException: Exception) {
                Result.failure(dbException)
            }
        }
    }

    override suspend fun updateEmployee(employee: Employee): Result<Unit> {
        return try {
            // Try to update via API first
            val dto = com.example.ihrm.data.remote.dto.EmployeeDto(
                id = employee.id,
                name = employee.name,
                email = employee.email,
                phone = employee.phone,
                department = employee.department,
                position = employee.position,
                hireDate = employee.hireDate,
                salary = employee.salary,
                address = employee.address,
                englishName = employee.englishName,
                gender = employee.gender,
                personalId = employee.personalId,
                idIssueDate = employee.idIssueDate,
                createdAt = employee.createdAt,
                updatedAt = employee.updatedAt
            )
            apiService.updateEmployee(employee.id, dto)
            
            // Update local database
            employeeDao.updateEmployee(employee.toEmployeeEntity())
            Result.success(Unit)
        } catch (e: Exception) {
            // If API fails, still update locally
            try {
                employeeDao.updateEmployee(employee.toEmployeeEntity())
                Result.success(Unit)
            } catch (dbException: Exception) {
                Result.failure(dbException)
            }
        }
    }

    override suspend fun deleteEmployee(id: String): Result<Unit> {
        return try {
            // Try to delete from API first
            apiService.deleteEmployee(id)
            
            // Delete from local database
            employeeDao.deleteEmployee(id)
            Result.success(Unit)
        } catch (e: Exception) {
            // If API fails, still delete locally
            try {
                employeeDao.deleteEmployee(id)
                Result.success(Unit)
            } catch (dbException: Exception) {
                Result.failure(dbException)
            }
        }
    }

    override suspend fun syncEmployees(): Result<Unit> {
        return try {
            // Fetch from API
            val employees = apiService.getAllEmployees()
            
            // Clear local database and insert fresh data
            employeeDao.deleteAllEmployees()
            employeeDao.insertEmployees(employees.map { it.toEmployeeEntity() })
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}