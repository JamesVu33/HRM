package com.example.ihrm.domain.model.securitycheck

data class SecuritySubmission(
    val id: Int,
    val templateId: Int,
    val userId: Int,
    val month: String,
    val answers: List<SecurityAnswer>,
    val submittedAt: String,
    val status: String
)

data class SecurityAnswer(
    val key: String,
    val value: Boolean,
    val remark: String
)