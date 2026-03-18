package com.example.ihrm.data.repository

import com.example.ihrm.data.local.dao.EmployeeDao
import com.example.ihrm.data.remote.api.EmployeeApiService
import com.example.ihrm.data.remote.base.safeApiCall
import com.example.ihrm.data.remote.base.safeApiCallList
import com.example.ihrm.data.remote.dto.MeEmployeeResponse
import com.example.ihrm.data.remote.dto.UserMetaResponseDto
import com.example.ihrm.data.remote.mapper.toEmployee
import com.example.ihrm.data.remote.mapper.toEmployeeEntity
import com.example.ihrm.data.remote.mapper.toLevel
import com.example.ihrm.domain.model.Employee
import com.example.ihrm.domain.model.Level
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
        val entity = employee.toEmployeeEntity()
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
            // Update local database (REPLACE ensures row is overwritten)
            employeeDao.insertEmployee(entity)
            Result.success(Unit)
        } catch (e: Exception) {
            // If API fails, still update locally
            try {
                employeeDao.insertEmployee(entity)
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

    override suspend fun syncEmployees(meta: UserMetaResponseDto?): Result<Unit> =
        safeApiCallList { apiService.getAllEmployees() }.fold(
            onSuccess = { list ->
                employeeDao.deleteAllEmployees()
                employeeDao.insertEmployees(list.map { it.toEmployeeEntity(meta) })
                Result.success(Unit)
            },
            onFailure = { Result.failure(it) }
        )

    override suspend fun getLevelsMap(): Result<Map<Int, String>> =
        safeApiCallList { apiService.getAllLevels() }.map { list -> list.associate { it.id to it.code } }

    override suspend fun getLevelById(id: Int): Result<Level?> =
        safeApiCall { apiService.getLevelById(id) }.map { dto -> dto?.toLevel() }

    override suspend fun getLevelByEmployeeId(employeeId: String): Result<Level?> =
        safeApiCall { apiService.getEmployeeById(employeeId) }.map { dto -> dto?.level?.toLevel() }

    override suspend fun getMeEmployeeInfo(): Result<MeEmployeeResponse> =
        safeApiCall { apiService.getMeEmployeeInfo() }.fold(
            onSuccess = { data -> if (data != null) Result.success(data) else Result.failure(Exception("No employee info")) },
            onFailure = { Result.failure(it) }
        )

    override suspend fun getEmployeesMeta(): Result<UserMetaResponseDto> =
        safeApiCall { apiService.getEmployeesMeta() }.fold(
            onSuccess = { data -> if (data != null) Result.success(data) else Result.failure(Exception("No meta")) },
            onFailure = { Result.failure(it) }
        )
}