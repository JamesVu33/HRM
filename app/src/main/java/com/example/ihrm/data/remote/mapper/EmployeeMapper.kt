package com.example.ihrm.data.remote.mapper

import com.example.ihrm.data.local.entity.EmployeeEntity
import com.example.ihrm.data.remote.dto.EmployeeDto
import com.example.ihrm.data.remote.dto.UserMetaResponseDto
import com.example.ihrm.data.remote.dto.LevelResponseDto
import com.example.ihrm.data.remote.dto.LevelShortDto
import com.example.ihrm.data.remote.dto.UserResponseDto
import com.example.ihrm.domain.model.Employee
import com.example.ihrm.domain.model.Level
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

fun LevelResponseDto.toLevel(): Level = Level(
    id = id,
    code = code,
    name = name,
    description = description
)

fun LevelShortDto.toLevel(): Level = Level(
    id = id,
    code = code,
    name = name,
    description = description
)

private val isoFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US).apply {
    timeZone = TimeZone.getTimeZone("UTC")
}

private fun parseIsoToLong(iso: String?): Long {
    if (iso.isNullOrBlank()) return System.currentTimeMillis()
    return try {
        isoFormat.parse(iso)?.time ?: System.currentTimeMillis()
    } catch (_: Exception) {
        System.currentTimeMillis()
    }
}

/** Level id for lookup via GET /levels/{id}; no mapping to code here, UI will call API. */
private fun UserResponseDto.resolveLevelId(): Int? = levelId ?: level?.id

/** Resolve position from title or role using meta. */
private fun UserResponseDto.resolvePositionName(meta: UserMetaResponseDto?): String? {
    val titleName = title?.let { meta?.titles?.find { t -> t.code == it.code }?.name ?: it.name }
    if (!titleName.isNullOrBlank()) return titleName
    val role = roles?.firstOrNull() ?: return null
    return meta?.roles?.find { it.code == role.code }?.name ?: role.name
}

/** Map GET /employees API item to domain Employee. levelId is for UI to call GET /levels/{id} for badge. */
fun UserResponseDto.toEmployee(meta: UserMetaResponseDto? = null): Employee {
    return Employee(
        id = employeeId,
        name = fullName,
        email = email,
        phone = phoneNumber,
        levelId = resolveLevelId(),
        department = null,
        position = resolvePositionName(meta),
        hireDate = null,
        salary = null,
        address = null,
        englishName = null,
        gender = null,
        personalId = null,
        idIssueDate = null,
        createdAt = parseIsoToLong(createdAt),
        updatedAt = parseIsoToLong(updatedAt)
    )
}

fun UserResponseDto.toEmployeeEntity(meta: UserMetaResponseDto? = null): EmployeeEntity {
    val created = parseIsoToLong(createdAt)
    val updated = parseIsoToLong(updatedAt)
    return EmployeeEntity(
        id = employeeId,
        name = fullName,
        email = email,
        phone = phoneNumber,
        levelId = resolveLevelId(),
        department = null,
        position = resolvePositionName(meta),
        hireDate = null,
        salary = null,
        address = null,
        englishName = null,
        gender = null,
        personalId = null,
        idIssueDate = null,
        createdAt = created,
        updatedAt = updated
    )
}

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
    val resolvedLevelId = levelId ?: level?.id
    return Employee(
        id = id,
        name = name,
        email = email,
        phone = phone,
        levelId = resolvedLevelId,
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
        levelId = levelId,
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
        levelId = levelId,
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