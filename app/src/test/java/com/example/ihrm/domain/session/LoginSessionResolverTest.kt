package com.example.ihrm.domain.session

import com.example.ihrm.data.remote.login.LoginResponse
import com.example.ihrm.data.remote.dto.RoleShortDto
import com.example.ihrm.data.remote.dto.UserShortDto
import com.example.ihrm.domain.model.AccountType
import com.example.ihrm.domain.model.AppFeature
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class LoginSessionResolverTest {

    private fun baseUser(employeeId: String = "11111111") = UserShortDto(
        id = 1,
        employeeId = employeeId,
        fullName = "Test User",
        email = "test@example.com",
        phoneNumber = null,
    )

    private fun baseDto(
        user: UserShortDto = baseUser(),
        roles: List<RoleShortDto> = emptyList(),
        accountType: String? = null,
        modifiableFeatures: List<String>? = null,
    ) = LoginResponse(
        accessToken = "a",
        refreshToken = "r",
        accessTokenExp = 0L,
        refreshTokenExp = 0L,
        user = user,
        roles = roles,
        accountType = accountType,
        modifiableFeatures = modifiableFeatures,
    )

    @Test
    fun resolve_usesAccountTypeFromApi() {
        val session = LoginSessionResolver.resolve(
            baseDto(accountType = "EXTRA", modifiableFeatures = listOf("USER", "ROLE"))
        )
        assertEquals(AccountType.Extra, session.accountType)
        assertEquals(setOf(AppFeature.USER, AppFeature.ROLE), session.modifiableFeatures)
    }

    @Test
    fun resolve_modifiableFeaturesFromApi_emptyList_honored() {
        val session = LoginSessionResolver.resolve(
            baseDto(accountType = "EXTRA", modifiableFeatures = emptyList())
        )
        assertTrue(session.modifiableFeatures.isEmpty())
    }

    @Test
    fun resolve_infersExtraFromRoleCode() {
        val session = LoginSessionResolver.resolve(
            baseDto(
                roles = listOf(
                    RoleShortDto(
                        id = 1,
                        code = "ADMIN",
                        name = "Admin",
                        description = null,
                    ),
                ),
            )
        )
        assertEquals(AccountType.Extra, session.accountType)
    }

    @Test
    fun resolve_mockExtraEmployeeId_whenApiMissing() {
        val session = LoginSessionResolver.resolve(
            baseDto(user = baseUser(employeeId = "00000001")),
        )
        assertEquals(AccountType.Extra, session.accountType)
        assertEquals(AppFeature.entries.toSet(), session.modifiableFeatures)
    }

    @Test
    fun resolve_mockBasicEmployeeId_whenApiMissing() {
        val session = LoginSessionResolver.resolve(
            baseDto(user = baseUser(employeeId = "99999999")),
        )
        assertEquals(AccountType.Basic, session.accountType)
        assertTrue(session.modifiableFeatures.isEmpty())
    }
}
