package com.example.ihrm.ui.dashboard

import com.example.ihrm.domain.model.Employee
import org.junit.Assert.assertEquals
import org.junit.Test

class DashboardViewModelTest {

    @Test
    fun filterEmployeesByQuery_blankQuery_returnsAll() {
        val employees = listOf(
            Employee(
                id = "1",
                name = "Alice",
                email = "a@b.com",
                phone = "1",
                levelId = null,
                department = "Dev",
                position = "Engineer",
                statusWorking = "-",
                salary = null,
                address = null,
                role = "-",
                level = "-",
            )
        )
        assertEquals(employees, filterEmployeesByQuery(employees, ""))
        assertEquals(employees, filterEmployeesByQuery(employees, "   "))
    }

    @Test
    fun filterEmployeesByQuery_nameMatch_returnsMatching() {
        val employees = listOf(
            Employee(
                id = "1",
                name = "Alexander Wright",
                email = "alex@co.com",
                phone = "1",
                levelId = null,
                department = "-",
                position = "Designer",
                statusWorking = "-",
                salary = null,
                address = null,
                role = "-",
                level = "-",
            ),
            Employee(
                id = "2",
                name = "Nguyen Van A",
                email = "nva@co.com",
                phone = "2",
                levelId = null,
                department = "-",
                position = "Developer",
                statusWorking = "-",
                salary = null,
                address = null,
                role = "-",
                level = "-",
            )
        )
        val result = filterEmployeesByQuery(employees, "alex")
        assertEquals(1, result.size)
        assertEquals("Alexander Wright", result[0].name)
    }

    @Test
    fun filterEmployeesByQuery_positionMatch_returnsMatching() {
        val employees = listOf(
            Employee(
                id = "1",
                name = "Alice",
                email = "a@b.com",
                phone = "1",
                levelId = null,
                department = "-",
                position = "Designer",
                statusWorking = "-",
                salary = null,
                address = null,
                role = "-",
                level = "-",
            ),
            Employee(
                id = "2",
                name = "Bob",
                email = "b@b.com",
                phone = "2",
                levelId = null,
                department = "-",
                position = "Developer",
                statusWorking = "-",
                salary = null,
                address = null,
                role = "-",
                level = "-",
            )
        )
        val result = filterEmployeesByQuery(employees, "designer")
        assertEquals(1, result.size)
        assertEquals("Designer", result[0].position)
    }

    @Test
    fun filterEmployeesByQuery_emailMatch_returnsMatching() {
        val employees = listOf(
            Employee(
                id = "1",
                name = "Alice",
                email = "alice@company.com",
                phone = "1",
                levelId = null,
                department = "HR",
                position = "-",
                statusWorking = "-",
                salary = null,
                address = null,
                role = "-",
                level = "-",
            )
        )
        val result = filterEmployeesByQuery(employees, "company")
        assertEquals(1, result.size)
    }
}
