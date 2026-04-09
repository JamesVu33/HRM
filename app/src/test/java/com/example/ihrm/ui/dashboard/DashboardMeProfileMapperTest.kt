package com.example.ihrm.ui.dashboard

import com.example.ihrm.data.remote.dto.LevelShortDto
import com.example.ihrm.data.remote.dto.MeEmployeeResponse
import com.example.ihrm.data.remote.dto.RoleShortDto
import com.example.ihrm.data.remote.dto.TitleShortDto
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

class DashboardMeProfileMapperTest {

    private val fallback = DashboardProfileModel(
        roleName = null,
        displayName = "Mock",
        employeeId = "00000000",
        departmentBadge = "Dept",
        email = "mock@test",
        phone = "000",
        joined = "joined",
        departmentDetail = "detail",
        avatarUrl = null,
        avatarInitials = "M",
        avatarSecurity = null
    )

    @Test
    fun mapMeEmployee_prefersSessionNameEmailAndId() {
        val me = MeEmployeeResponse(
            title = TitleShortDto(id = 1, code = "T1", name = "Engineer"),
            level = LevelShortDto(id = 1, code = "L1", name = "Senior"),
            roles = emptyList(),
            onBoardAt = "2024-06-01"
        )
        val out = mapMeEmployeeToDashboardProfile(
            me,
            fallback,
            displayName = "Real Name",
            email = "real@test",
            employeeId = "12345678",
            phone = "+1"
        )
        assertEquals("Real Name", out.displayName)
        assertEquals("12345678", out.employeeId)
        assertEquals("real@test", out.email)
        assertEquals("+1", out.phone)
        assertEquals("Senior", out.departmentDetail)
        val expectedJoined = LocalDate.parse("2024-06-01").format(
            DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.getDefault())
        )
        assertEquals(expectedJoined, out.joined)
    }

    @Test
    fun mapMeEmployee_usesRoleWhenTitleMissing() {
        val me = MeEmployeeResponse(
            title = null,
            level = null,
            roles = listOf(RoleShortDto(id = 1, code = "R", name = "Admin")),
            onBoardAt = null
        )
        val out = mapMeEmployeeToDashboardProfile(
            me,
            fallback,
            displayName = null,
            email = null,
            employeeId = null,
            phone = null
        )
        assertEquals("Admin", out.departmentBadge)
    }

    @Test
    fun formatOnBoardAt_parsesIsoDate() {
        val raw = "2023-01-15T00:00:00Z"
        val expected = LocalDate.parse("2023-01-15").format(
            DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.getDefault())
        )
        assertEquals(expected, formatOnBoardAt(raw))
    }
}
