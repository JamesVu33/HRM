package com.example.ihrm.data.repository

import com.example.ihrm.data.local.dao.EmployeeDao
import com.example.ihrm.data.remote.api.EmployeeApiService
import com.example.ihrm.data.remote.base.safeApiCall
import com.example.ihrm.data.remote.base.safeApiCallRaw
import com.example.ihrm.data.remote.dto.MeEmployeeResponse
import com.example.ihrm.data.remote.base.NetworkResult
import com.example.ihrm.data.remote.dto.EmployeeDto
import com.example.ihrm.data.remote.dto.UserMetaResponseDto
import com.example.ihrm.data.remote.employee.EmployeeProfileResponse
import com.example.ihrm.data.remote.mapper.toEmployee
import com.example.ihrm.data.remote.mapper.toEmployeeDto
import com.example.ihrm.data.remote.mapper.toEmployeeEntity
import com.example.ihrm.data.remote.mapper.toLevel
import com.example.ihrm.di.NetworkModule
import com.example.ihrm.domain.model.Employee
import com.example.ihrm.domain.model.Level
import com.example.ihrm.domain.repository.EmployeeRepository
import com.example.ihrm.domain.usecase.employees.EmployeeListDto
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
        val dto = employee.toEmployeeDto()
        val result = safeApiCall(retrofit) { apiService.createEmployee(dto) }
        if (result is NetworkResult.Success) {
            employeeDao.insertEmployee(result.data.toEmployee().toEmployeeEntity())
            return NetworkResult.Success(Unit)
        }
        return result.map { Unit }
    }

    override suspend fun updateEmployee(employee: Employee): NetworkResult<Unit> {
        val dto = employee.toEmployeeDto()
        val result = safeApiCall(retrofit) { apiService.updateEmployee(employee.id, dto) }
        if (result is NetworkResult.Success) {
            employeeDao.insertEmployee(result.data.toEmployee().toEmployeeEntity())
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

    override suspend fun getEmployeeDetailById(employeeId: String): NetworkResult<EmployeeDto> =
        safeApiCall(retrofit) { apiService.getEmployeeById(employeeId) }

    override suspend fun getEmployeeProfileById(employeeId: String): NetworkResult<EmployeeProfileResponse> =
        safeApiCall(retrofit) { apiService.getEmployeeProfileById(employeeId) }

    override suspend fun getMeEmployeeInfo(): NetworkResult<MeEmployeeResponse> =
        safeApiCall(retrofit) { apiService.getMeEmployeeInfo() }

    override suspend fun getEmployeesMeta(): NetworkResult<UserMetaResponseDto> =
        safeApiCall(retrofit) { apiService.getEmployeesMeta() }

    override suspend fun changeAvatar(avatar: MultipartBody.Part): NetworkResult<Unit> {
        val result = safeApiCallRaw { apiService.changeAvatar(avatar) }
        return result.map { Unit }
    }

    override suspend fun getEmployeesList(
        search: String?,
        page: Int?,
        limit: Int?,
        orderBy: String?,
        sortBy: String?,
        type: String?,
        groupId: String?,
        isLeader: Boolean?,
        status: String?,
        jobTitles: List<String>?
    ): NetworkResult<List<EmployeeListDto>> = safeApiCall(retrofit) {
        apiService.getEmployeesList(
            search = search,
            page = page,
            limit = limit,
            orderBy = orderBy,
            sortBy = sortBy,
            type = type,
            groupId = groupId,
            isLeader = isLeader,
            status = status,
            jobTitles = jobTitles
        )
    }

    private fun <T, R> NetworkResult<T>.map(transform: (T) -> R): NetworkResult<R> {
        return when (this) {
            is NetworkResult.Success -> NetworkResult.Success(transform(data))
            is NetworkResult.Failure -> NetworkResult.Failure(error)
            is NetworkResult.Exception -> NetworkResult.Exception(e)
        }
    }
}
