package com.example.ihrm.data.remote.securities

import com.example.ihrm.data.remote.base.ResponseToInfoMapper
import com.example.ihrm.domain.model.securitycheck.SecurityAnswer
import com.example.ihrm.domain.model.securitycheck.SecuritySubmission
import com.google.gson.annotations.SerializedName

data class SecuritySubmissionResponse(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("templateId")
    val templateId: Int?,
    @SerializedName("userId")
    val userId: Int?,
    @SerializedName("month")
    val month: String?, // Ví dụ: "2601"
    @SerializedName("answers")
    val answers: List<SecurityAnswerResponse>?,
    @SerializedName("submittedAt")
    val submittedAt: String?,
    @SerializedName("status")
    val status: String? // Ví dụ: "SUBMITTED"
): ResponseToInfoMapper<SecuritySubmission> {
    override fun fromResponseToInfo(): SecuritySubmission {
        return SecuritySubmission(
            id = id ?: 0,
            templateId = templateId ?: 0,
            userId = userId ?: 0,
            month = month ?: "",
            answers = answers?.map { it.fromResponseToInfo() } ?: emptyList(),
            submittedAt = submittedAt ?: "",
            status = status ?: "UNKNOWN"
        )
    }
}

data class SecurityAnswerResponse(
    @SerializedName("key")
    val key: String?,
    @SerializedName("value")
    val value: Boolean?,
    @SerializedName("remark")
    val remark: String?
): ResponseToInfoMapper<SecurityAnswer> {
    override fun fromResponseToInfo(): SecurityAnswer {
        return SecurityAnswer(
            key = key ?: "",
            value = value ?: false,
            remark = remark ?: ""
        )
    }
}