package com.example.ihrm.domain.session

import com.example.ihrm.data.mock.LoginMockSession
import com.example.ihrm.data.remote.dto.RoleShortDto
import com.example.ihrm.data.remote.login.LoginResponse
import com.example.ihrm.domain.model.AccountType
import com.example.ihrm.domain.model.AppFeature

/**
 * Builds [ResolvedLoginSession] from login API payload, with [LoginMockSession] fallbacks.
 */
object LoginSessionResolver {

    private val extraRoleCodeHints = setOf(
        "ADMIN",
        "HR_ADMIN",
        "MANAGER",
        "EXTRA",
        "SUPER_ADMIN",
    )

    fun resolve(dto: LoginResponse): ResolvedLoginSession {
        val accountType = resolveAccountType(dto)
        val modifiable = resolveModifiableFeatures(dto, accountType)
        return ResolvedLoginSession(
            accountType = accountType,
            modifiableFeatures = modifiable,
        )
    }

    private fun resolveAccountType(dto: LoginResponse): AccountType {
        AccountType.fromApiValue(dto.accountType)?.let { return it }
        inferAccountTypeFromRoles(dto.roles)?.let { return it }
        return LoginMockSession.accountTypeWhenApiMissing(dto.user.employeeId)
    }

    private fun inferAccountTypeFromRoles(roles: List<RoleShortDto>): AccountType? {
        val codes = roles.map { it.code.uppercase() }
        if (codes.any { code -> extraRoleCodeHints.any { hint -> hint in code || code.contains(hint) } }) {
            return AccountType.Extra
        }
        return null
    }

    private fun resolveModifiableFeatures(
        dto: LoginResponse,
        accountType: AccountType,
    ): Set<AppFeature> {
        val raw = dto.modifiableFeatures
        if (raw != null) {
            return raw.mapNotNull { AppFeature.fromApiCode(it) }.toSet()
        }
        return LoginMockSession.modifiableFeaturesWhenApiMissing(
            accountType = accountType,
            employeeId = dto.user.employeeId,
        )
    }
}
