package com.example.ihrm.data.remote.mapper

import com.example.ihrm.data.remote.dto.MetaDto
import com.example.ihrm.data.remote.dto.SecurityCheckGroupDto
import com.example.ihrm.data.remote.dto.SecurityCheckReviewerDto
import com.example.ihrm.data.remote.dto.SecurityCheckSubmissionDto
import com.example.ihrm.data.remote.dto.SecurityCheckSubmissionUserDto
import com.example.ihrm.data.remote.dto.SecurityCheckTemplateDto
import com.example.ihrm.domain.model.SecurityCheckGroup
import com.example.ihrm.domain.model.SecurityCheckReviewer
import com.example.ihrm.domain.model.SecurityCheckSubmission
import com.example.ihrm.domain.model.SecurityCheckSubmissionUser
import com.example.ihrm.domain.model.SecurityCheckTemplate
import com.example.ihrm.domain.model.SubmissionPaginationMeta

fun MetaDto.toSubmissionPaginationMeta(): SubmissionPaginationMeta =
    SubmissionPaginationMeta(
        page = page,
        limit = limit,
        total = total,
        totalPages = totalPages,
    )

fun SecurityCheckSubmissionDto.toDomain(): SecurityCheckSubmission =
    SecurityCheckSubmission(
        id = id,
        templateId = templateId,
        userId = userId,
        submittedAt = submittedAt,
        status = status,
        rejectReason = rejectReason,
        reviewedBy = reviewedBy?.toDomain(),
        createdAt = createdAt,
        user = user?.toDomain(),
        group = group?.toDomain(),
        template = template?.toDomain(),
        employeeId = employeeId,
    )

fun SecurityCheckSubmissionUserDto.toDomain(): SecurityCheckSubmissionUser =
    SecurityCheckSubmissionUser(
        id = id,
        employeeId = employeeId,
        fullName = fullName,
        avatarUrl = profile?.avatarUrl,
    )

fun SecurityCheckReviewerDto.toDomain(): SecurityCheckReviewer =
    SecurityCheckReviewer(
        id = id,
        employeeId = employeeId,
        fullName = fullName,
        email = email,
        phoneNumber = phoneNumber,
        status = status,
        isSystem = isSystem,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )

fun SecurityCheckTemplateDto.toDomain(): SecurityCheckTemplate =
    SecurityCheckTemplate(id = id, name = name)

fun SecurityCheckGroupDto.toDomain(): SecurityCheckGroup =
    SecurityCheckGroup(
        id = id,
        code = code,
        name = name,
        description = description,
        path = path,
        leaderId = leaderId,
    )
