package com.example.ihrm.ui.login

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class LoginTestViewModelTest {

    @Test
    fun updateEmail_empty_doesNotShowError() {
        val vm = LoginViewModel()
        vm.updateEmail("")

        assertNull(vm.uiState.value.emailError)
    }

    @Test
    fun updateEmail_invalid_setsInvalidFormat() {
        val vm = LoginViewModel()
        vm.updateEmail("invalid-email")

        assertEquals(LoginFieldError.InvalidFormat, vm.uiState.value.emailError)
    }

    @Test
    fun updatePassword_empty_doesNotShowError() {
        val vm = LoginViewModel()
        vm.updatePassword("")

        assertNull(vm.uiState.value.passwordError)
    }

    @Test
    fun updatePassword_weak_setsInvalidRules() {
        val vm = LoginViewModel()
        vm.updatePassword("abc")

        assertEquals(LoginFieldError.InvalidRules, vm.uiState.value.passwordError)
    }

    @Test
    fun togglePasswordVisibility_togglesState() {
        val vm = LoginViewModel()
        assertEquals(false, vm.uiState.value.isPasswordVisible)

        vm.togglePasswordVisibility()
        assertEquals(true, vm.uiState.value.isPasswordVisible)

        vm.togglePasswordVisibility()
        assertEquals(false, vm.uiState.value.isPasswordVisible)
    }
}

