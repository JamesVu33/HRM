package com.example.ihrm.data.remote.securities

import com.example.ihrm.data.remote.base.ResponseToInfoMapper
import com.example.ihrm.data.remote.dto.SecurityCheckReviewerDto
import com.example.ihrm.domain.model.securitycheck.Answer
import com.example.ihrm.domain.model.securitycheck.SecurityCheckDetail
import com.google.gson.annotations.SerializedName

data class SecurityCheckDetailResponse(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("userEmployeeId")
    val userEmployeeId: String?,
    @SerializedName("userName")
    val userName: String?,
    @SerializedName("templateId")
    val templateId: Int?,
    @SerializedName("templateName")
    val templateName: String?,
    @SerializedName("groupName")
    val groupName: String?,
    @SerializedName("submittedAt")
    val submittedAt: String?,
    @SerializedName("answers")
    val answers: List<AnswerResponse>?,
    @SerializedName("status")
    val status: String?,
    @SerializedName("rejectReason")
    val rejectReason: String?,
    @SerializedName("reviewedBy")
    val reviewedBy: SecurityCheckReviewerDto?
) : ResponseToInfoMapper<SecurityCheckDetail> {
    override fun fromResponseToInfo(): SecurityCheckDetail {
        return SecurityCheckDetail(
            id = id ?: 0,
            userEmployeeId = userEmployeeId ?: "",
            userName = userName ?: "",
            templateId = templateId ?: 0,
            templateName = templateName ?: "",
            groupName = groupName ?: "",
            submittedAt = submittedAt ?: "",
            answers = answers?.map { it.fromResponseToInfo() } ?: emptyList(),
            status = status ?: "",
            rejectReason = rejectReason ?: "",
            approveBy = reviewedBy?.fullName ?: ""
        )
    }

    data class AnswerResponse(
        @SerializedName("key")
        val key: String?,
        @SerializedName("value")
        val value: Boolean?,
        @SerializedName("remark")
        val remark: String?
    ) : ResponseToInfoMapper<Answer> {
        override fun fromResponseToInfo(): Answer {
            return Answer(
                key = key ?: "",
                value = value ?: false,
                remark = remark ?: ""
            )
        }
    }
}