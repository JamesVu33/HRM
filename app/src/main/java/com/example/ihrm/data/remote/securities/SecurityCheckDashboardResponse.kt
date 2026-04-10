package com.example.ihrm.data.remote.securities

import com.google.gson.annotations.SerializedName

data class SecurityCheckDashboardResponse(
    @SerializedName("totalSubmissions")
    val totalSubmissions: Int? = null,
    
    @SerializedName("currentTemplate")
    val currentTemplate: TemplateDto? = null,
    
    @SerializedName("securityCheckSubmissions")
    val submissionSummary: SubmissionSummaryDto? = null,
    
    @SerializedName("securityCheckStatsThisMonth")
    val statsThisMonth: List<StatusCountDto> = emptyList(),
    
    @SerializedName("last4MonthsData")
    val last4MonthsData: List<MonthlyHistoryDto> = emptyList()
)

data class TemplateDto(
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("name")
    val name: String? = null,
)

data class SubmissionSummaryDto(
    @SerializedName("totalSubmissions")
    val totalSubmissions: Int? = null,
    @SerializedName("submittedCount")
    val submittedCount: Int? = null,
    @SerializedName("notSubmittedCount")
    val notSubmittedCount: Int? = null,
    @SerializedName("totalUsers")
    val totalUsers: Int? = null,
)

data class StatusCountDto(
    @SerializedName("status")
    val status: String? = null, // SUBMITTED, APPROVED, REJECTED
    @SerializedName("count")
    val count: Int? = null,
)

data class MonthlyHistoryDto(
    @SerializedName("SUBMITTED")
    val submitted: Int = 0,
    
    @SerializedName("APPROVED")
    val approved: Int = 0,
    
    @SerializedName("REJECTED")
    val rejected: Int = 0,
    
    @SerializedName("date")
    val date: String? = null, // Định dạng "dd/MM/yyyy"
)