package com.example.ihrm.domain.usecase

import com.example.ihrm.domain.repository.EmployeeRepository
import javax.inject.Inject

/**
 * Fetches level id -> code map for badge display (e.g. GET /levels).
 */
class GetLevelsMapUseCase @Inject constructor(
    private val repository: EmployeeRepository
) {
    suspend operator fun invoke() = repository.getLevelsMap()
}
