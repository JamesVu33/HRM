package com.example.ihrm.ui.myinfo

/**
 * At least 8 characters, 1 uppercase, 1 lowercase, 1 digit, 1 special character.
 */
private val CHANGE_PASSWORD_POLICY_REGEX =
    Regex("""^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[^A-Za-z0-9]).{8,}$""")

fun matchesMyInfoPasswordPolicy(password: String): Boolean =
    CHANGE_PASSWORD_POLICY_REGEX.matches(password)

enum class ChangePasswordValidationIssue {
    CURRENT_REQUIRED,
    NEW_REQUIRED,
    CONFIRM_REQUIRED,
    MISMATCH,
    POLICY,
}

fun validateChangePasswordInput(
    current: String,
    new: String,
    confirm: String,
): ChangePasswordValidationIssue? {
    if (current.isBlank()) return ChangePasswordValidationIssue.CURRENT_REQUIRED
    if (new.isBlank()) return ChangePasswordValidationIssue.NEW_REQUIRED
    if (confirm.isBlank()) return ChangePasswordValidationIssue.CONFIRM_REQUIRED
    if (new != confirm) return ChangePasswordValidationIssue.MISMATCH
    if (!matchesMyInfoPasswordPolicy(new)) return ChangePasswordValidationIssue.POLICY
    return null
}
