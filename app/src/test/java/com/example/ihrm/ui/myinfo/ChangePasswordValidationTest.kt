package com.example.ihrm.ui.myinfo

import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Test

class ChangePasswordValidationTest {

    @Test
    fun matchesMyInfoPasswordPolicy_acceptsValidPassword() {
        assertTrue(matchesMyInfoPasswordPolicy("Aa1!aaaa"))
        assertTrue(matchesMyInfoPasswordPolicy("Str0ng#Pass"))
    }

    @Test
    fun matchesMyInfoPasswordPolicy_rejectsTooShortOrMissingClasses() {
        assertFalse(matchesMyInfoPasswordPolicy("Aa1!aaa")) // 7 chars
        assertFalse(matchesMyInfoPasswordPolicy("aaaaaaa1!")) // no upper
        assertFalse(matchesMyInfoPasswordPolicy("AAAAAAA1!")) // no lower
        assertFalse(matchesMyInfoPasswordPolicy("Aaaaaaa!")) // no digit
        assertFalse(matchesMyInfoPasswordPolicy("Aaaaaaa1")) // no special
    }

    @Test
    fun validateChangePasswordInput_returnsFirstIssueInOrder() {
        assertSame(
            ChangePasswordValidationIssue.CURRENT_REQUIRED,
            validateChangePasswordInput("", "Aa1!aaaa", "Aa1!aaaa"),
        )
        assertSame(
            ChangePasswordValidationIssue.NEW_REQUIRED,
            validateChangePasswordInput("old", "", ""),
        )
        assertSame(
            ChangePasswordValidationIssue.CONFIRM_REQUIRED,
            validateChangePasswordInput("old", "Aa1!aaaa", ""),
        )
        assertSame(
            ChangePasswordValidationIssue.MISMATCH,
            validateChangePasswordInput("old", "Aa1!aaaa", "Aa1!aaab"),
        )
        assertSame(
            ChangePasswordValidationIssue.POLICY,
            validateChangePasswordInput("old", "weak", "weak"),
        )
        assertNull(validateChangePasswordInput("old", "Aa1!aaaa", "Aa1!aaaa"))
    }
}
