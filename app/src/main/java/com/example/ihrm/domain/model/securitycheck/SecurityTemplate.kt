package com.example.ihrm.domain.model.securitycheck

data class SecurityTemplate(
    val id: Int,
    val name: String,
    val isCurrent: Boolean,
    val createdAt: String,
    val updatedAt: String,
    val createdBy: UserInfo,
    val items: List<TemplateItem>,
    val divisions: List<TemplateDivision>
)

data class TemplateDivision(
    val id: Int,
    val titleEn: String,
    val titleVi: String,
    val order: Int,
    val items: List<TemplateItem>
)

data class UserInfo(
    val id: Int,
    val employeeId: String,
    val fullName: String,
    val email: String,
    val phoneNumber: String
)

data class TemplateItem(
    val id: Int,
    val divisionId: Int,
    val divisionOrder: Int,
    val key: String,
    val titleEn: String,
    val titleVi: String
)