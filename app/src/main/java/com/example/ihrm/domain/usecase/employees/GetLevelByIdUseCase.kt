package com.example.ihrm.domain.usecase.employees

import com.example.ihrm.data.remote.base.NetworkResult
import com.example.ihrm.domain.model.Level
import com.example.ihrm.domain.repository.EmployeeRepository
import javax.inject.Inject

/**
 * Gọi GET /levels/{id}; dùng kèm cache trong ViewModel để tránh gọi trùng cho cùng levelId.
 */
class GetLevelByIdUseCase @Inject constructor(
    private val repository: EmployeeRepository
) {
    suspend operator fun invoke(id: Int): NetworkResult<Level?> = repository.getLevelById(id)
}
