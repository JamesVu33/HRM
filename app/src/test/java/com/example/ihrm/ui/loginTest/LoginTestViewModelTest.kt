package com.example.ihrm.ui.loginTest

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class LoginTestViewModelTest {

    @Test
    fun updateEmail_empty_doesNotShowError() {
        val vm = LoginTestViewModel()
        vm.updateEmail("")

        assertNull(vm.uiState.value.emailError)
    }

    @Test
    fun updateEmail_invalid_setsInvalidFormat() {
        val vm = LoginTestViewModel()
        vm.updateEmail("invalid-email")

        assertEquals(LoginTestFieldError.InvalidFormat, vm.uiState.value.emailError)
    }

    @Test
    fun updatePassword_empty_doesNotShowError() {
        val vm = LoginTestViewModel()
        vm.updatePassword("")

        assertNull(vm.uiState.value.passwordError)
    }

    @Test
    fun updatePassword_weak_setsInvalidRules() {
        val vm = LoginTestViewModel()
        vm.updatePassword("abc")

        assertEquals(LoginTestFieldError.InvalidRules, vm.uiState.value.passwordError)
    }

    @Test
    fun togglePasswordVisibility_togglesState() {
        val vm = LoginTestViewModel()
        assertEquals(false, vm.uiState.value.isPasswordVisible)

        vm.togglePasswordVisibility()
        assertEquals(true, vm.uiState.value.isPasswordVisible)

        vm.togglePasswordVisibility()
        assertEquals(false, vm.uiState.value.isPasswordVisible)
    }
}

