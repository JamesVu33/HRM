package com.example.ihrm.data.repository

import com.example.ihrm.data.local.dao.EmployeeDao
import com.example.ihrm.data.remote.api.EmployeeApiService
import com.example.ihrm.data.remote.base.safeApiCall
import com.example.ihrm.data.remote.base.safeApiCallRaw
import com.example.ihrm.data.remote.dto.MeEmployeeResponse
import com.example.ihrm.data.remote.base.NetworkResult
import com.example.ihrm.data.remote.dto.UserMetaResponseDto
import com.example.ihrm.data.remote.mapper.toEmployee
import com.example.ihrm.data.remote.mapper.toEmployeeEntity
import com.example.ihrm.data.remote.mapper.toLevel
import com.example.ihrm.di.NetworkModule
import com.example.ihrm.domain.model.Employee
import com.example.ihrm.domain.model.Level
import com.example.ihrm.domain.repository.EmployeeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import okhttp3.MultipartBody
import retrofit2.Retrofit
import javax.inject.Inject

class EmployeeRepositoryImpl @Inject constructor(
    private val employeeDao: EmployeeDao,
    private val apiService: EmployeeApiService,
    @param:NetworkModule.InternalApi private val retrofit: Retrofit,
) : EmployeeRepository {

    override fun getAllEmployees(): Flow<List<Employee>> {
        return employeeDao.getAllEmployees().map { entities ->
            entities.map { it.toEmployee() }
        }
    }

    override fun getEmployeeById(id: String): Flow<Employee?> {
        return employeeDao.getEmployeeById(id).map { it?.toEmployee() }
    }

    override suspend fun addEmployee(employee: Employee): NetworkResult<Unit> {
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
        val result = safeApiCall(retrofit) { apiService.createEmployee(dto) }
        if (result is NetworkResult.Success) {
            employeeDao.insertEmployee(employee.toEmployeeEntity())
            return NetworkResult.Success(Unit)
        }
        return result.map { Unit }
    }

    override suspend fun updateEmployee(employee: Employee): NetworkResult<Unit> {
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
        val result = safeApiCall(retrofit) { apiService.updateEmployee(employee.id, dto) }
        if (result is NetworkResult.Success) {
            employeeDao.insertEmployee(employee.toEmployeeEntity())
            return NetworkResult.Success(Unit)
        }
        return result.map { Unit }
    }

    override suspend fun deleteEmployee(id: String): NetworkResult<Unit> {
        val result = safeApiCall(retrofit) { apiService.deleteEmployee(id) }
        if (result is NetworkResult.Success) {
            employeeDao.deleteEmployee(id)
            return NetworkResult.Success(Unit)
        }
        return result.map { Unit }
    }

    override suspend fun syncEmployees(meta: UserMetaResponseDto?): NetworkResult<Unit> {
        val result = safeApiCall(retrofit) { apiService.getAllEmployees() }
        return when (result) {
            is NetworkResult.Success -> {
                employeeDao.deleteAllEmployees()
                employeeDao.insertEmployees(result.data.map { it.toEmployeeEntity(meta) })
                NetworkResult.Success(Unit)
            }

            is NetworkResult.Failure -> NetworkResult.Failure(result.error)
            is NetworkResult.Exception -> NetworkResult.Exception(result.e)
        }
    }

    override suspend fun getLevelsMap(): NetworkResult<Map<Int, String>> {
        val result = safeApiCall(retrofit) { apiService.getAllLevels() }
        return when (result) {
            is NetworkResult.Success -> {
                val map = result.data.associate { it.id to it.code }
                NetworkResult.Success(map)
            }

            is NetworkResult.Failure -> NetworkResult.Failure(result.error)
            is NetworkResult.Exception -> NetworkResult.Exception(result.e)
        }
    }

    override suspend fun getLevelById(id: Int): NetworkResult<Level?> {
        val result = safeApiCall(retrofit) { apiService.getLevelById(id) }
        return when (result) {
            is NetworkResult.Success -> NetworkResult.Success(result.data.toLevel())
            is NetworkResult.Failure -> NetworkResult.Failure(result.error)
            is NetworkResult.Exception -> NetworkResult.Exception(result.e)
        }
    }

    override suspend fun getLevelByEmployeeId(employeeId: String): NetworkResult<Level?> {
        val result = safeApiCall(retrofit) { apiService.getEmployeeById(employeeId) }
        return when (result) {
            is NetworkResult.Success -> NetworkResult.Success(
                result.data.level?.toLevel()
            )

            is NetworkResult.Failure -> NetworkResult.Failure(result.error)
            is NetworkResult.Exception -> NetworkResult.Exception(result.e)
        }
    }

    override suspend fun getMeEmployeeInfo(): NetworkResult<MeEmployeeResponse> =
        safeApiCall(retrofit) { apiService.getMeEmployeeInfo() }

    override suspend fun getEmployeesMeta(): NetworkResult<UserMetaResponseDto> =
        safeApiCall(retrofit) { apiService.getEmployeesMeta() }

    override suspend fun changeAvatar(avatar: MultipartBody.Part): NetworkResult<Unit> {
        val result = safeApiCallRaw { apiService.changeAvatar(avatar) }
        return result.map { Unit }
    }

    private fun <T, R> NetworkResult<T>.map(transform: (T) -> R): NetworkResult<R> {
        return when (this) {
            is NetworkResult.Success -> NetworkResult.Success(transform(data))
            is NetworkResult.Failure -> NetworkResult.Failure(error)
            is NetworkResult.Exception -> NetworkResult.Exception(e)
        }
    }
}
