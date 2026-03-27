package com.example.ihrm.domain.usecase.employees

import com.example.ihrm.data.remote.base.NetworkResult
import com.example.ihrm.domain.model.Level
import com.example.ihrm.domain.repository.EmployeeRepository
import javax.inject.Inject

/**
 * Lấy level theo employee id: gọi GET /employees/{id}, trả về level từ response.
 */
class GetLevelByEmployeeIdUseCase @Inject constructor(
    private val repository: EmployeeRepository
) {
    suspend operator fun invoke(employeeId: String): NetworkResult<Level?> = repository.getLevelByEmployeeId(employeeId)
}
