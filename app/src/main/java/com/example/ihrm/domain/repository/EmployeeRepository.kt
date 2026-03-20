package com.example.ihrm.domain.repository

import com.example.ihrm.data.remote.dto.MeEmployeeResponse
import com.example.ihrm.data.remote.dto.UserMetaResponseDto
import com.example.ihrm.domain.model.Employee
import com.example.ihrm.domain.model.Level
import kotlinx.coroutines.flow.Flow

interface EmployeeRepository {
    fun getAllEmployees(): Flow<List<Employee>>
    fun getEmployeeById(id: String): Flow<Employee?>
    suspend fun addEmployee(employee: Employee): Result<Unit>
    suspend fun updateEmployee(employee: Employee): Result<Unit>
    suspend fun deleteEmployee(id: String): Result<Unit>
    /** Syncs employees from API; [meta] is used for position mapping only. */
    suspend fun syncEmployees(meta: UserMetaResponseDto? = null): Result<Unit>
    /** Fetches all levels and returns map levelId -> code for badge display (e.g. GET /levels). */
    suspend fun getLevelsMap(): Result<Map<Int, String>>
    /** Fetches one level by id (GET /levels/{id}). Dùng để gộp với Employee mà không gọi trùng. */
    suspend fun getLevelById(id: Int): Result<Level?>
    /** Lấy level theo employee id: gọi GET /employees/{id}, trả về level từ response. */
    suspend fun getLevelByEmployeeId(employeeId: String): Result<Level?>
    suspend fun getMeEmployeeInfo(): Result<MeEmployeeResponse>
    suspend fun getEmployeesMeta(): Result<UserMetaResponseDto>
}