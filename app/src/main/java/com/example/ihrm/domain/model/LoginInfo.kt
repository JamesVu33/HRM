package com.example.ihrm.domain.model

import com.example.ihrm.data.remote.dto.RoleShortDto

data class LoginInfo(
    val id: Int,
    val employeeId: String,
    val fullName: String,
    val email: String,
    val phoneNumber: String?,
    val roles: List<RoleShortDto>,
    val accountType: AccountType = AccountType.Basic,

    // ????
    val modifiableFeatures: List<String>? = null,
)