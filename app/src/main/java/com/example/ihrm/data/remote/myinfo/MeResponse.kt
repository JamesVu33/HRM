package com.example.ihrm.data.remote.myinfo

import com.example.ihrm.data.remote.base.ResponseToInfoMapper
import com.example.ihrm.data.remote.dto.RoleShortDto
import com.example.ihrm.domain.model.MyInfo
import com.example.ihrm.domain.model.MyProfile
import com.example.ihrm.domain.model.Settings
import com.google.gson.annotations.SerializedName

data class MeResponse(
    @SerializedName("employeeId")
    val employeeId: String? = null,

    @SerializedName("fullName")
    val fullName: String? = null,

    @SerializedName("email")
    val email: String? = null,

    @SerializedName("phoneNumber")
    val phoneNumber: String? = null,

    @SerializedName("id")
    val id: Int? = null,

    @SerializedName("roles")
    val roles: List<RoleShortDto>? = null,

    @SerializedName("status")
    val status: String? = null,

    @SerializedName("profile")
    val profile: ProfileResponse? = null,

    @SerializedName("settings")
    val settings: SettingsResponse? = null
) : ResponseToInfoMapper<MyInfo> {
    override fun fromResponseToInfo(): MyInfo {
        return MyInfo(
            employeeId = employeeId ?: "",
            fullName = fullName ?: "",
            email = email ?: "",
            phoneNumber = phoneNumber ?: "",
            id = id ?: 0,
            roles = roles ?: emptyList(),
            status = status ?: "",
            profile = profile?.fromResponseToInfo(),
            settings = settings?.fromResponseToInfo()
        )
    }
}

data class ProfileResponse(
    @SerializedName("avatarUrl")
    val avatarUrl: String? = null,
) : ResponseToInfoMapper<MyProfile?> {
    override fun fromResponseToInfo(): MyProfile? = MyProfile.fromAvatarOnly(avatarUrl)
}


data class SettingsResponse(
    @SerializedName("lang")
    val lang: String? = null,

    @SerializedName("theme")
    val theme: String? = null
) : ResponseToInfoMapper<Settings> {
    override fun fromResponseToInfo(): Settings {
        return Settings(
            lang = lang ?: "",
            theme = theme ?: ""
        )
    }
}
