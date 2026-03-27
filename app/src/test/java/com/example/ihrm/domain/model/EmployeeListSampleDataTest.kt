package com.example.ihrm.domain.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class EmployeeListSampleDataTest {

    @Test
    fun uiModels_matchesFigmaStyleTeamList() {
        val models = EmployeeListSampleData.uiModels
        assertEquals(3, models.size)
        assertEquals("Alexander Wright", models[0].employee.name)
        assertEquals("S2", models[0].levelCode)
        assertEquals("J1", models[1].levelCode)
        assertTrue(models.all { it.employee.email.isNotBlank() })
    }
}
