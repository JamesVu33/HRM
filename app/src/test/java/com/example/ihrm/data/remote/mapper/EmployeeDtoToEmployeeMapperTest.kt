package com.example.ihrm.data.remote.mapper

import com.example.ihrm.data.remote.dto.EmployeeDto
import com.example.ihrm.data.remote.dto.TitleLevelDto
import com.example.ihrm.domain.model.Employee
import org.junit.Assert.assertEquals
import org.junit.Test

class EmployeeDtoToEmployeeMapperTest {

    @Test
    fun toEmployee_prefersEmployeeIdAndFullNameFromApiShape() {
        val dto = EmployeeDto(
            id = null,
            employeeId = "emp-1",
            fullName = "Nguyen A",
            email = "a@co.com",
            phoneNumber = "0901",
        )
        val e = dto.toEmployee()
        assertEquals("emp-1", e.id)
        assertEquals("Nguyen A", e.name)
        assertEquals("a@co.com", e.email)
        assertEquals("0901", e.phone)
    }

    @Test
    fun toEmployee_positionFallsBackToTitleName() {
        val dto = EmployeeDto(
            id = 1,
            employeeId = "e1",
            title = TitleLevelDto(id = 1, code = "DEV", name = "Developer"),
        )
        assertEquals("Developer", dto.toEmployee().position)
    }

    @Test
    fun toEmployeeDto_roundTripCoreFields() {
        val e = Employee(
            id = "uuid-1",
            name = "Jane",
            email = "j@co.com",
            phone = "01",
            levelId = 5,
            department = "IT",
            position = "Lead",
            statusWorking = null,
            salary = null,
            address = null,
        )
        val dto = e.toEmployeeDto()
        assertEquals("uuid-1", dto.employeeId)
        assertEquals("Jane", dto.fullName)
        assertEquals(5, dto.level?.id)
        assertEquals("Lead", dto.title?.name)
        assertEquals("IT", dto.membershipOf?.singleOrNull()?.name)
    }
}
