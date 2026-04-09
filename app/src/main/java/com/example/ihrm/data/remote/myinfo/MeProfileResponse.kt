package com.example.ihrm.data.remote.myinfo

import com.example.ihrm.data.remote.base.ResponseToInfoMapper
import com.example.ihrm.domain.model.Gender
import com.example.ihrm.domain.model.JobLevel
import com.example.ihrm.domain.model.JobTitle
import com.example.ihrm.domain.model.MaritalStatus
import com.example.ihrm.domain.model.MyProfile
import com.google.gson.annotations.SerializedName

data class MeProfileResponse(
    @SerializedName("address")
    val address: String? = null,

    @SerializedName("avatarUrl")
    val avatarUrl: String? = null,

    @SerializedName("dateOfBirth")
    val dateOfBirth: String? = null,

    @SerializedName("englishName")
    val englishName: String? = null,

    @SerializedName("gender")
    val gender: String? = null,

    @SerializedName("identityId")
    val identityId: String? = null,

    @SerializedName("identityIdIssueDate")
    val identityIdIssueDate: String? = null,

    @SerializedName("identityIdIssuePlace")
    val identityIdIssuePlace: String? = null,

    @SerializedName("identityIdExpiryDate")
    val identityIdExpiryDate: String? = null,

    @SerializedName("maritalStatus")
    val maritalStatus: String? = null,

    @SerializedName("nationality")
    val nationality: String? = null,

    @SerializedName("ethnic")
    val ethnic: String? = null,

    @SerializedName("religion")
    val religion: String? = null,

    @SerializedName("placeOfBirth")
    val placeOfBirth: String? = null,

    @SerializedName("createdAt")
    val createdAt: String? = null,

    @SerializedName("updatedAt")
    val updatedAt: String? = null,

    @SerializedName("level")
    val level: LevelResponse? = null,

    @SerializedName("level")
    val title: TitleResponse? = null,
) : ResponseToInfoMapper<MyProfile> {
    override fun fromResponseToInfo(): MyProfile {
        return MyProfile(
            address = address,
            avatarUrl = avatarUrl,
            dateOfBirth = dateOfBirth,
            englishName = englishName,
            gender = Gender.from(gender),
            identityId = identityId,
            identityIdIssueDate = identityIdIssueDate,
            identityIdIssuePlace = identityIdIssuePlace,
            identityIdExpiryDate = identityIdExpiryDate,
            maritalStatus = MaritalStatus.from(maritalStatus),
            nationality = nationality,
            ethnic = ethnic,
            religion = religion,
            placeOfBirth = placeOfBirth,
            createdAt = createdAt,
            updatedAt = updatedAt,
            level = level?.toJobLevel(),
            title = title?.toJobTitle(),
        )
    }
}

data class LevelResponse(
    @SerializedName("id")
    val id: Int? = null,

    @SerializedName("code")
    val code: String? = null,

    @SerializedName("name")
    val name: String? = null,

    @SerializedName("description")
    val description: String? = null,
) {
    fun toJobLevel(): JobLevel? {
        val id = id ?: return null
        return JobLevel(
            id = id,
            code = code.orEmpty(),
            name = name.orEmpty(),
            description = description.orEmpty(),
        )
    }
}

data class TitleResponse(
    @SerializedName("id")
    val id: Int? = null,

    @SerializedName("code")
    val code: String? = null,

    @SerializedName("name")
    val name: String? = null,

    @SerializedName("description")
    val description: String? = null,
) {
    fun toJobTitle(): JobTitle? {
        val id = id ?: return null
        return JobTitle(
            id = id,
            code = code.orEmpty(),
            name = name.orEmpty(),
            description = description.orEmpty(),
        )
    }
}
