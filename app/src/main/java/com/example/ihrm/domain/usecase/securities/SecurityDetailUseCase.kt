package com.example.ihrm.domain.usecase.securities

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import com.example.ihrm.R
import com.example.ihrm.core.usecase.BaseUseCase
import com.example.ihrm.data.remote.base.NetworkResult
import com.example.ihrm.domain.model.securitycheck.SecurityCheckDetail
import com.example.ihrm.domain.model.securitycheck.SecurityTemplate
import com.example.ihrm.domain.repository.LanguageRepository
import com.example.ihrm.domain.repository.SecurityCheckDetailRepository
import javax.inject.Inject

enum class SecurityStatus(
    val key: String,
    @param:StringRes val labelRes: Int,
    val backgroundColor: Color,
    val contentColor: Color,
    @param:DrawableRes val iconRes: Int
) {
    APPROVED(
        key = "approved",
        labelRes = R.string.security_checks_approved_by_param,
        backgroundColor = Color(0xFFD7FFE1),
        contentColor = Color(0xFF006D35),
        iconRes = R.drawable.ic_approved
    ),
    REJECTED(
        key = "rejected",
        labelRes = R.string.security_checks_rejected_by_param,
        backgroundColor = Color(0xFFFFE4E4),
        contentColor = Color(0xFFC62828),
        iconRes = R.drawable.ic_rejected
    ),
    SUBMITTED(
        key = "submitted",
        labelRes = R.string.my_security_check_submitted,
        backgroundColor = Color(0xFFDCEAF8),
        contentColor = Color(0xFF0747A6),
        iconRes = R.drawable.ic_submitted
    );

    companion object {
        fun fromKey(key: String?): SecurityStatus {
            return SecurityStatus.entries.find { it.key.equals(key, ignoreCase = true) } ?: SUBMITTED
        }
    }
}

data class SecurityDetailUiModel(
    val userName: String? = null,
    val employeeId: String? = null,
    val status: String? = null,
    val submittedAt: String? = null,
    val templateName: String? = null,
    val approveBy: String? = null,
    val checkList: List<CheckItemUiModel> = emptyList()
)

data class CheckItemUiModel(
    val key: String,
    val titleVi: String,
    val titleEn: String,
    val isChecked: Boolean,
    val remark: String?
)

class SecurityDetailUseCase @Inject constructor(
    private val securityDetailRepository: SecurityCheckDetailRepository
) : BaseUseCase() {
    @Inject
    override lateinit var languageRepository: LanguageRepository

    suspend fun getSecurityCheckDetail(id: String): NetworkResult<SecurityCheckDetail> {
        val result = securityDetailRepository.getSecurityCheckDetail(id)
        return translateResponse(result)
    }

    suspend fun getSecurityTemplates(id: Int): NetworkResult<SecurityTemplate> {
        val result = securityDetailRepository.getSecurityTemplates(id)
        return translateResponse(result)
    }

    fun mapToUiState(
        detail: SecurityCheckDetail,
        template: SecurityTemplate
    ): SecurityDetailUiModel {
        val checkList = template.items.map { item ->
            val answer = detail.answers.find { it.key == item.key }

            CheckItemUiModel(
                key = item.key,
                titleVi = item.titleVi,
                isChecked = answer?.value ?: false,
                remark = answer?.remark,
                titleEn = item.titleEn
            )
        }

        return SecurityDetailUiModel(
            userName = detail.userName,
            employeeId = detail.userEmployeeId,
            status = detail.status,
            submittedAt = formatDateTime(detail.submittedAt),
            templateName = detail.templateName,
            checkList = checkList,
            approveBy = detail.approveBy
        )
    }

    fun formatDateTime(isoString: String?): String {
        return try {
            val parsed = java.time.OffsetDateTime.parse(isoString)
            val formatter = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
            parsed.format(formatter)
        } catch (e: Exception) {
            isoString ?: ("" + e)
        }
    }
}