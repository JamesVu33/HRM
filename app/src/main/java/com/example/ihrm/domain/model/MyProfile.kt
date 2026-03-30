package com.example.ihrm.domain.model

data class MyProfile(
    val address: String?,
    val avatarUrl: String?,
    val dateOfBirth: String?,
    val englishName: String?,
    val gender: Gender,
    val identityId: String?,
    val identityIdIssueDate: String?,
    val identityIdIssuePlace: String?,
    val identityIdExpiryDate: String?,
    val maritalStatus: MaritalStatus,
    val nationality: String?,
    val ethnic: String?,
    val religion: String?,
    val placeOfBirth: String?,
    val createdAt: String?,
    val updatedAt: String?,
    val level: JobLevel?,
    val title: JobTitle?,
) {
    companion object {
        /** Chỉ có avatar từ GET /me; các trường khác mặc định. */
        fun fromAvatarOnly(avatarUrl: String?): MyProfile? {
            if (avatarUrl.isNullOrBlank()) return null
            return MyProfile(
                address = null,
                avatarUrl = avatarUrl,
                dateOfBirth = null,
                englishName = null,
                gender = Gender.UNKNOWN,
                identityId = null,
                identityIdIssueDate = null,
                identityIdIssuePlace = null,
                identityIdExpiryDate = null,
                maritalStatus = MaritalStatus.UNKNOWN,
                nationality = null,
                ethnic = null,
                religion = null,
                placeOfBirth = null,
                createdAt = null,
                updatedAt = null,
                level = null,
                title = null,
            )
        }
    }
}

data class JobLevel(
    val id: Int,
    val code: String,
    val name: String,
    val description: String,
)

data class JobTitle(
    val id: Int,
    val code: String,
    val name: String,
    val description: String,
)
