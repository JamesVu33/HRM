package com.example.ihrm.domain.model

import com.example.ihrm.data.remote.dto.RoleShortDto

data class MyInfo(
    val employeeId: String,
    val fullName: String,
    val email: String,
    val phoneNumber: String,
    val id: Int,
    val roles: List<RoleShortDto>,
    val status: String,
    /** Hợp nhất từ GET /me (avatar) và GET /me/profile (chi tiết). */
    val profile: MyProfile?,
    val settings: Settings?,
)

data class Settings(
    val lang: String,
    val theme: String,
)

/**
 * Gộp dữ liệu profile từ `/me/profile` lên [MyInfo]; ưu tiên avatar từ profile API, fallback avatar từ GET /me.
 */
fun MyInfo.withMergedProfile(detail: MyProfile): MyInfo {
    val existing = profile
    return copy(
        profile = detail.copy(
            avatarUrl = detail.avatarUrl ?: existing?.avatarUrl,
        ),
    )
}