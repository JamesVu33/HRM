package com.example.ihrm.data.remote.mapper

import com.example.ihrm.data.remote.dto.MetaDto
import com.example.ihrm.data.remote.dto.ProfileAvatarResponseDto
import com.example.ihrm.data.remote.dto.SecurityCheckSubmissionDto
import com.example.ihrm.data.remote.dto.SecurityCheckSubmissionUserDto
import com.example.ihrm.data.remote.dto.SecurityCheckTemplateDto
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class SecurityCheckSubmissionMapperTest {

    @Test
    fun toDomain_mapsUserTemplateAndAvatar() {
        val dto = SecurityCheckSubmissionDto(
            id = 32,
            templateId = 15,
            userId = 96,
            submittedAt = "2026-04-01T04:35:06.605Z",
            status = "SUBMITTED",
            rejectReason = null,
            reviewedBy = null,
            createdAt = "2026-04-01T04:35:06.605Z",
            user = SecurityCheckSubmissionUserDto(
                id = 96,
                employeeId = "11111111",
                fullName = "Nguyễn Chu Toàn Vẹn",
                profile = ProfileAvatarResponseDto(avatarUrl = null),
            ),
            group = null,
            template = SecurityCheckTemplateDto(id = 15, name = "Security Check Template Official"),
        )

        val domain = dto.toDomain()

        assertEquals(32, domain.id)
        assertEquals("SUBMITTED", domain.status)
        assertEquals("11111111", domain.user?.employeeId)
        assertEquals("Security Check Template Official", domain.template?.name)
        assertNull(domain.user?.avatarUrl)
    }

    @Test
    fun metaDto_toSubmissionPaginationMeta() {
        val meta = MetaDto(page = 1, limit = 100, total = 32, totalPages = 1).toSubmissionPaginationMeta()
        assertEquals(1, meta.page)
        assertEquals(100, meta.limit)
        assertEquals(32, meta.total)
        assertEquals(1, meta.totalPages)
    }
}
