package com.example.ihrm.domain.usecase.employees

import com.example.ihrm.core.usecase.BaseUseCase
import com.example.ihrm.data.remote.base.NetworkResult
import com.example.ihrm.data.remote.dto.EmployeeDto
import com.example.ihrm.data.remote.mapper.mapToEmployee
import com.example.ihrm.domain.model.Employee
import com.example.ihrm.domain.repository.EmployeeRepository
import com.example.ihrm.domain.repository.LanguageRepository
import com.example.ihrm.util.combineResult
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class GetEmployeeByIdUseCase @Inject constructor(
    private val repository: EmployeeRepository
): BaseUseCase() {
    @Inject
    override lateinit var languageRepository: LanguageRepository

    suspend operator fun invoke(id: String): NetworkResult<Employee> = coroutineScope {
        val employeeDeferred = async { translateResponse(repository.getEmployeeDetailById(id)) }
        val employeeProfileDeferred = async { translateResponse(repository.getEmployeeProfileById(id)) }
        val employeeResult = employeeDeferred.await()
        val employeeProfileResult = employeeProfileDeferred.await()
        combineResult(employeeResult, employeeProfileResult) { employee, profile ->
            mapToEmployee(employee, profile)
        }
    }

}