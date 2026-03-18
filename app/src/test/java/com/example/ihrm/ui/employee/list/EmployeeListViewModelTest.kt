package com.example.ihrm.ui.employee.list

import com.example.ihrm.domain.model.Employee
import com.example.ihrm.domain.model.Level
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class EmployeeListViewModelTest {

    @Test
    fun buildEmployeeUiModels_emptyList_returnsEmpty() {
        val result = buildEmployeeUiModels(emptyList(), emptyMap())
        assertEquals(0, result.size)
    }

    @Test
    fun buildEmployeeUiModels_withLevelMap_resolvesLevelCode() {
        val employees = listOf(
            Employee("1", "Alice", "a@b.com", "1", levelId = 10, null, "Dev", null, null, null),
            Employee("2", "Bob", "b@b.com", "2", levelId = 20, null, "QA", null, null, null)
        )
        val levelMap = mapOf(
            10 to Level(10, "S1", "Senior 1"),
            20 to Level(20, "J2", "Junior 2")
        )
        val result = buildEmployeeUiModels(employees, levelMap)
        assertEquals(2, result.size)
        assertEquals("S1", result[0].levelCode)
        assertEquals("J2", result[1].levelCode)
    }

    @Test
    fun buildEmployeeUiModels_missingLevelId_levelCodeNull() {
        val employees = listOf(
            Employee("1", "Alice", "a@b.com", "1", levelId = 99, null, "Dev", null, null, null)
        )
        val result = buildEmployeeUiModels(employees, emptyMap())
        assertEquals(1, result.size)
        assertNull(result[0].levelCode)
    }

    @Test
    fun buildEmployeeUiModels_nullLevelId_levelCodeNull() {
        val employees = listOf(
            Employee("1", "Alice", "a@b.com", "1", levelId = null, null, "Dev", null, null, null)
        )
        val result = buildEmployeeUiModels(employees, mapOf(10 to Level(10, "S1", "Senior")))
        assertEquals(1, result.size)
        assertNull(result[0].levelCode)
    }
}
