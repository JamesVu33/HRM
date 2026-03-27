package com.example.ihrm.domain.model

/**
 * Account tier from login API (`accountType`) — drives dashboard shell (Basic vs Extra) and default
 * modify permissions when the API omits [modifiableFeatures].
 */
enum class AccountType {
    Basic,
    Extra;

    companion object {
        fun fromApiValue(raw: String?): AccountType? {
            if (raw.isNullOrBlank()) return null
            return when (raw.uppercase()) {
                "BASIC" -> Basic
                "EXTRA" -> Extra
                else -> null
            }
        }
    }

    fun toApiValue(): String = when (this) {
        Basic -> "BASIC"
        Extra -> "EXTRA"
    }
}
