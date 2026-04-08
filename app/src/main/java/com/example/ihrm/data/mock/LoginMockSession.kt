package com.example.ihrm.data.mock

import com.example.ihrm.domain.model.AccountType
import com.example.ihrm.domain.model.AppFeature

/**
 * Mock account tier and feature modify rights when [com.example.ihrm.data.remote.dto.LoginResponseDto]
 * does not include `accountType` / `modifiableFeatures` (dev or older API).
 *
 * Adjust [mockExtraEmployeeIds] / [mockModifiableOverrides] to test flows locally.
 */
object LoginMockSession {

    /** EmployeeDepartmentResponse IDs treated as **Extra** when API omits `accountType`. */
    private val mockExtraEmployeeIds: Set<String> = setOf(
        "00000001",
        "12345678",
    )

    /**
     * Per-employee overrides for **modifiable** features (API omitted list).
     * Keys: employeeId. Values: exact set of features allowed to modify.
     */
    private val mockModifiableOverrides: Map<String, Set<AppFeature>> = mapOf(
        // Example: Basic-like account but can edit users + security checks only
        "87654321" to setOf(AppFeature.USER, AppFeature.SECURITY_CHECK),
    )

    fun accountTypeWhenApiMissing(employeeId: String): AccountType {
        return if (employeeId.trim() in mockExtraEmployeeIds) AccountType.Extra else AccountType.Basic
    }

    fun modifiableFeaturesWhenApiMissing(
        accountType: AccountType,
        employeeId: String,
    ): Set<AppFeature> {
        mockModifiableOverrides[employeeId.trim()]?.let { return it }
        return when (accountType) {
            AccountType.Basic -> emptySet()
            AccountType.Extra -> AppFeature.entries.toSet()
        }
    }
}
