package com.example.ihrm.domain.repository

import com.example.ihrm.data.remote.dto.MeEmployeeResponse
import com.example.ihrm.data.remote.base.NetworkResult
import com.example.ihrm.data.remote.dto.UserMetaResponseDto
import com.example.ihrm.domain.model.Employee
import com.example.ihrm.domain.model.Level
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody

interface EmployeeRepository {
    fun getAllEmployees(): Flow<List<Employee>>
    fun getEmployeeById(id: String): Flow<Employee?>
    suspend fun addEmployee(employee: Employee): NetworkResult<Unit>
    suspend fun updateEmployee(employee: Employee): NetworkResult<Unit>
    suspend fun deleteEmployee(id: String): NetworkResult<Unit>
    /** Syncs employees from API; [meta] is used for position mapping only. */
    suspend fun syncEmployees(meta: UserMetaResponseDto? = null): NetworkResult<Unit>
    /** Fetches all levels and returns map levelId -> code for badge display (e.g. GET /levels). */
    suspend fun getLevelsMap(): NetworkResult<Map<Int, String>>
    /** Fetches one level by id (GET /levels/{id}). Dùng để gộp với EmployeeDepartmentResponse mà không gọi trùng. */
    suspend fun getLevelById(id: Int): NetworkResult<Level?>
    /** Lấy level theo employee id: gọi GET /employees/{id}, trả về level từ response. */
    suspend fun getLevelByEmployeeId(employeeId: String): NetworkResult<Level?>
    suspend fun getMeEmployeeInfo(): NetworkResult<MeEmployeeResponse>
    suspend fun getEmployeesMeta(): NetworkResult<UserMetaResponseDto>
    suspend fun changeAvatar(avatar: MultipartBody.Part): NetworkResult<Unit>
}
