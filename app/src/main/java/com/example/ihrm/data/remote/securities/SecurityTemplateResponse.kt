package com.example.ihrm.data.remote.securities

import com.example.ihrm.data.remote.base.ResponseToInfoMapper
import com.example.ihrm.domain.model.securitycheck.SecurityTemplate
import com.example.ihrm.domain.model.securitycheck.TemplateItem
import com.example.ihrm.domain.model.securitycheck.UserInfo
import com.google.gson.annotations.SerializedName

data class SecurityTemplateResponse(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("isCurrent")
    val isCurrent: Boolean?,
    @SerializedName("createdAt")
    val createdAt: String?,
    @SerializedName("updatedAt")
    val updatedAt: String?,
    @SerializedName("createdBy")
    val createdBy: UserInfoResponse?,
    @SerializedName("items")
    val items: List<TemplateItemResponse>?,
    @SerializedName("divisions")
    val divisions: List<Any>?
): ResponseToInfoMapper<SecurityTemplate> {
    override fun fromResponseToInfo(): SecurityTemplate {
        return SecurityTemplate(
            id = id ?: 0,
            name = name ?: "",
            isCurrent = isCurrent ?: false,
            createdAt = createdAt ?: "",
            updatedAt = updatedAt ?: "",
            items = items?.map { it.fromResponseToInfo() } ?: emptyList(),
            divisions = divisions ?: emptyList(),
            createdBy = createdBy?.fromResponseToInfo() ?: UserInfo(
                id = id ?: 0,
                employeeId = "",
                fullName = "",
                email = "",
                phoneNumber = ""
            )
        )
    }
}

data class UserInfoResponse(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("employeeId")
    val employeeId: String?,
    @SerializedName("fullName")
    val fullName: String?,
    @SerializedName("email")
    val email: String?,
    @SerializedName("phoneNumber")
    val phoneNumber: String?
): ResponseToInfoMapper<UserInfo> {
    override fun fromResponseToInfo(): UserInfo {
        return UserInfo(
            id = id ?: 0,
            employeeId = employeeId ?: "",
            fullName = fullName ?: "",
            email = email ?: "",
            phoneNumber = phoneNumber ?: ""
        )
    }
}

data class TemplateItemResponse(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("divisionId")
    val divisionId: Int?,
    @SerializedName("divisionOrder")
    val divisionOrder: Int?,
    @SerializedName("key")
    val key: String?,
    @SerializedName("titleEn")
    val titleEn: String?,
    @SerializedName("titleVi")
    val titleVi: String?
): ResponseToInfoMapper<TemplateItem>{
    override fun fromResponseToInfo(): TemplateItem {
        return TemplateItem(
            id = id ?: 0,
            divisionId = divisionId ?: 0,
            divisionOrder = divisionOrder ?: 0,
            key = key ?: "",
            titleEn = titleEn ?: "",
            titleVi = titleVi ?: ""
        )
    }

}