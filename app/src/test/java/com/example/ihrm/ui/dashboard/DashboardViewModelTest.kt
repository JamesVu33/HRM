package com.example.ihrm.ui.dashboard

import com.example.ihrm.domain.model.Employee
import org.junit.Assert.assertEquals
import org.junit.Test

class DashboardViewModelTest {

    @Test
    fun filterEmployeesByQuery_blankQuery_returnsAll() {
        val employees = listOf(
            Employee("1", "Alice", "a@b.com", "1", "Dev", "Engineer", null, null, null)
        )
        assertEquals(employees, filterEmployeesByQuery(employees, ""))
        assertEquals(employees, filterEmployeesByQuery(employees, "   "))
    }

    @Test
    fun filterEmployeesByQuery_nameMatch_returnsMatching() {
        val employees = listOf(
            Employee("1", "Alexander Wright", "alex@co.com", "1", null, "Designer", null, null, null),
            Employee("2", "Nguyen Van A", "nva@co.com", "2", null, "Developer", null, null, null)
        )
        val result = filterEmployeesByQuery(employees, "alex")
        assertEquals(1, result.size)
        assertEquals("Alexander Wright", result[0].name)
    }

    @Test
    fun filterEmployeesByQuery_positionMatch_returnsMatching() {
        val employees = listOf(
            Employee("1", "Alice", "a@b.com", "1", null, "Designer", null, null, null),
            Employee("2", "Bob", "b@b.com", "2", null, "Developer", null, null, null)
        )
        val result = filterEmployeesByQuery(employees, "designer")
        assertEquals(1, result.size)
        assertEquals("Designer", result[0].position)
    }

    @Test
    fun filterEmployeesByQuery_emailMatch_returnsMatching() {
        val employees = listOf(
            Employee("1", "Alice", "alice@company.com", "1", null, "HR", null, null, null)
        )
        val result = filterEmployeesByQuery(employees, "company")
        assertEquals(1, result.size)
    }
}
