package com.example.ihrm.data.remote.mapper

import com.example.ihrm.data.local.entity.EmployeeEntity
import com.example.ihrm.data.remote.dto.DepartmentDto
import com.example.ihrm.data.remote.dto.EmployeeDto
import com.example.ihrm.data.remote.dto.TitleLevelDto
import com.example.ihrm.data.remote.dto.UserMetaResponseDto
import com.example.ihrm.data.remote.dto.LevelResponseDto
import com.example.ihrm.data.remote.dto.LevelShortDto
import com.example.ihrm.data.remote.dto.UserResponseDto
import com.example.ihrm.data.remote.employee.EmployeeProfileResponse
import com.example.ihrm.domain.model.Employee
import com.example.ihrm.domain.model.EmployeeUiModel
import com.example.ihrm.domain.model.Level
import com.example.ihrm.domain.usecase.employees.EmployeeListDto
import com.example.ihrm.util.Constants.DASH
import com.example.ihrm.util.formatDateTime
import java.text.SimpleDateFormat
import java.time.Instant
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

fun parseIsoToLong(iso: String?): Long {
    if (iso.isNullOrBlank()) return System.currentTimeMillis()
    return try {
        Instant.parse(iso).toEpochMilli()
    } catch (_: Exception) {
        try {
            isoFormat.parse(iso)?.time ?: System.currentTimeMillis()
        } catch (_: Exception) {
            System.currentTimeMillis()
        }
    }
}

/** Serialize local epoch ms for outbound [EmployeeDto] (GET responses use ISO strings). */
fun Long.toIso8601UtcString(): String = Instant.ofEpochMilli(this).toString()

/** Level id for lookup via GET /levels/{id}; no mapping to code here, UI will call API. */
private fun UserResponseDto.resolveLevelId(): Int? = levelId ?: level?.id

/** Resolve position from level or role using meta. */
private fun UserResponseDto.resolvePositionName(meta: UserMetaResponseDto?): String? {
    val titleName = title?.let { meta?.titles?.find { t -> t.code == it.code }?.name ?: it.name }
    if (!titleName.isNullOrBlank()) return titleName
    val role = roles?.firstOrNull() ?: return null
    return meta?.roles?.find { it.code == role.code }?.name ?: role.name
}

/** Map GET /employees API item to domain EmployeeDepartmentResponse. levelId is for UI to call GET /levels/{id} for badge. */
fun UserResponseDto.toEmployee(meta: UserMetaResponseDto? = null): Employee {
    return Employee(
        id = employeeId,
        name = fullName,
        email = email,
        phone = phoneNumber,
        levelId = resolveLevelId(),
        department = DASH,
        position = resolvePositionName(meta)?: DASH,
        statusWorking = DASH,
        salary = null,
        address = null,
        englishName = null,
        gender = null,
        personalId = null,
        idIssueDate = null,
        createdAt = parseIsoToLong(createdAt),
        updatedAt = parseIsoToLong(updatedAt),
        role = DASH,
        level = DASH
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

fun EmployeeDto.toEmployee(): Employee {
    val resolvedId = employeeId?.takeIf { it.isNotBlank() } ?: id?.toString() ?: ""
    return Employee(
        id = resolvedId,
        name = fullName.orEmpty(),
        email = email.orEmpty(),
        phone = phoneNumber.orEmpty(),
        levelId = level?.id,
        department = membershipOf?.firstOrNull()?.name ?: DASH,
        position = title?.name?: DASH,
        statusWorking = DASH,
        salary = null,
        address = null,
        englishName = null,
        gender = null,
        personalId = null,
        idIssueDate = null,
        createdAt = parseIsoToLong(createdAt),
        updatedAt = parseIsoToLong(updatedAt),
        role = roles?.name ?: DASH,
        level = level?.name ?: DASH
    )
}

fun mapToEmployee(employee: EmployeeDto, profile: EmployeeProfileResponse): Employee {
    return Employee(
        id = employee.employeeId.toString(),
        name = employee.fullName.orEmpty(),
        email = employee.email.orEmpty(),
        phone = employee.phoneNumber.orEmpty(),
        levelId = employee.level?.id,
        department = employee.membershipOf?.firstOrNull()?.name ?: DASH,
        position = employee.title?.name ?: DASH,
        level = employee.level?.name ?: DASH,
        statusWorking = employee.status ?: DASH,
        salary = null,
        role = profile.roles?.name ?: profile.roles?.code ?: DASH,
        address = profile.address,
        englishName = profile.englishName,
        gender = profile.gender,
        personalId = profile.identityId,
        idIssueDate = profile.identityIdIssueDate.formatDateTime(),
        createdAt = parseIsoToLong(employee.createdAt),
        updatedAt = parseIsoToLong(employee.updatedAt)
    )
}


/**
 * Map domain [Employee] → [EmployeeDto] cho POST/PUT employees (schema be-nest-hrm).
 */
fun Employee.toEmployeeDto(): EmployeeDto {
    val parsedNumericId = id.toIntOrNull()
    return EmployeeDto(
        id = parsedNumericId,
        employeeId = id.takeIf { parsedNumericId == null },
        fullName = name,
        email = email,
        phoneNumber = phone,
        level = levelId?.let { TitleLevelDto(id = it, code = null, name = null) },
        title = position.takeIf { it.isNotBlank() }?.let {
            TitleLevelDto(id = null, code = null, name = it)
        },
        membershipOf = department?.takeIf { it.isNotBlank() }?.let { dept ->
            listOf(DepartmentDto(id = null, code = null, name = dept))
        },
        createdAt = createdAt.toIso8601UtcString(),
        updatedAt = updatedAt.toIso8601UtcString()
    )
}

fun EmployeeEntity.toEmployee(): Employee {
    return Employee(
        id = id,
        name = name,
        email = email,
        phone = phone,
        levelId = levelId,
        department = department ?: DASH,
        position = position?: DASH,
        statusWorking = hireDate ?: DASH,
        salary = salary,
        address = address,
        englishName = englishName,
        gender = gender,
        personalId = personalId,
        idIssueDate = idIssueDate,
        createdAt = createdAt,
        updatedAt = updatedAt,
        role = DASH,
        level = DASH
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
        hireDate = statusWorking,
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

fun EmployeeListDto.toEmployeeUiModel(): EmployeeUiModel {
    val employee = Employee(
        id = employeeId.toString(),
        name = fullName.orEmpty(),
        email = email.orEmpty(),
        phone = phoneNumber.orEmpty(),
        levelId = title?.id,
        department = membershipOf?.firstOrNull()?.name.orEmpty(),
        position = title?.name.orEmpty(),
        statusWorking = status.orEmpty(),
        salary = null,
        address = null,
        englishName = null,
        gender = null,
        personalId = null,
        idIssueDate = null,
        createdAt = parseIsoToLong(createdAt),
        updatedAt = parseIsoToLong(updatedAt),
        role = DASH,
        level = level?.code ?: DASH
    )
    return EmployeeUiModel(employee = employee)
}