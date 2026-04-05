package com.example.ihrm.domain.model.securitycheck

data class SecurityCheckDetail(
    val id: Int,
    val userEmployeeId: String,
    val userName: String,
    val templateId: Int,
    val templateName: String,
    val groupName: String,
    val submittedAt: String,
    val answers: List<Answer>,
    val status: String,
    val rejectReason: String,
    val approveBy: String,
)

data class Answer(
    val key: String,
    val value: Boolean,
    val remark: String
)