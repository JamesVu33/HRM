package com.example.ihrm.ui.localization

import com.example.ihrm.data.remote.language.LanguageResponse
import com.example.ihrm.data.remote.language.LanguageStatus
import org.junit.Assert.assertEquals
import org.junit.Test

class PickTranslationTest {

    private val row = LanguageResponse(
        id = "1",
        key = "test_key",
        namespace = "common",
        valueVi = "Xin chào",
        valueEn = "Hello",
        valueKr = "안녕",
        status = LanguageStatus.ACTIVE,
    )

    @Test
    fun `null row returns fallback`() {
        assertEquals("fb", pickTranslation(null, "en", "fb"))
    }

    @Test
    fun `vi picks valueVi`() {
        assertEquals("Xin chào", pickTranslation(row, "vi", "fb"))
    }

    @Test
    fun `en picks valueEn`() {
        assertEquals("Hello", pickTranslation(row, "en", "fb"))
    }

    @Test
    fun `ko picks valueKr`() {
        assertEquals("안녕", pickTranslation(row, "ko", "fb"))
    }

    @Test
    fun `blank vi falls back`() {
        val blankVi = row.copy(valueVi = "  ")
        assertEquals("fb", pickTranslation(blankVi, "vi", "fb"))
    }

    @Test
    fun `unknown code uses English`() {
        assertEquals("Hello", pickTranslation(row, "fr", "fb"))
    }
}
