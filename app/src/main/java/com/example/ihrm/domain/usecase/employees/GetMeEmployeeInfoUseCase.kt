package com.example.ihrm.domain.usecase.employees

import com.example.ihrm.data.remote.dto.MeEmployeeResponse
import com.example.ihrm.data.remote.base.NetworkResult
import com.example.ihrm.domain.repository.EmployeeRepository
import javax.inject.Inject

class GetMeEmployeeInfoUseCase @Inject constructor(
    private val repository: EmployeeRepository
) {
    suspend operator fun invoke(): NetworkResult<MeEmployeeResponse> =
        repository.getMeEmployeeInfo()
}
