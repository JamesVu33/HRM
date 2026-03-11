package com.example.ihrm.data.remote.mapper

import com.example.ihrm.data.local.entity.EmployeeEntity
import com.example.ihrm.data.remote.dto.EmployeeDto
import com.example.ihrm.domain.model.Employee

fun EmployeeDto.toEmployeeEntity(): EmployeeEntity {
    return EmployeeEntity(
        id = id,
        name = name,
        email = email,
        phone = phone,
        department = department,
        position = position,
        hireDate = hireDate,
        salary = salary,
        address = address,
        englishName = englishName,
        gender = gender,
        personalId = personalId,
        idIssueDate = idIssueDate,
        createdAt = createdAt ?: System.currentTimeMillis(),
        updatedAt = updatedAt ?: System.currentTimeMillis()
    )
}

fun EmployeeDto.toEmployee(): Employee {
    return Employee(
        id = id,
        name = name,
        email = email,
        phone = phone,
        department = department,
        position = position,
        hireDate = hireDate,
        salary = salary,
        address = address,
        englishName = englishName,
        gender = gender,
        personalId = personalId,
        idIssueDate = idIssueDate,
        createdAt = createdAt ?: System.currentTimeMillis(),
        updatedAt = updatedAt ?: System.currentTimeMillis()
    )
}

fun EmployeeEntity.toEmployee(): Employee {
    return Employee(
        id = id,
        name = name,
        email = email,
        phone = phone,
        department = department,
        position = position,
        hireDate = hireDate,
        salary = salary,
        address = address,
        englishName = englishName,
        gender = gender,
        personalId = personalId,
        idIssueDate = idIssueDate,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun Employee.toEmployeeEntity(): EmployeeEntity {
    return EmployeeEntity(
        id = id,
        name = name,
        email = email,
        phone = phone,
        department = department,
        position = position,
        hireDate = hireDate,
        salary = salary,
        address = address,
        englishName = englishName,
        gender = gender,
        personalId = personalId,
        idIssueDate = idIssueDate,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}