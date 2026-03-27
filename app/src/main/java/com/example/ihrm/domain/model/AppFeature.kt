package com.example.ihrm.domain.model

/**
 * Master-data / admin areas whose **modify** rights can be granted per user (login payload or mock).
 * Aligns with backend feature codes.
 */
enum class AppFeature(val apiCode: String) {
    GROUP("GROUP"),
    PERMISSION("PERMISSION"),
    ROLE("ROLE"),
    USER("USER"),
    SECURITY_CHECK("SECURITY_CHECK"),
    TRANSLATION("TRANSLATION"),
    TITLE("TITLE"),
    LEVEL("LEVEL");

    companion object {
        fun fromApiCode(code: String): AppFeature? =
            entries.firstOrNull { it.apiCode.equals(code, ignoreCase = true) }
    }
}
