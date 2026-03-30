package com.example.ihrm.domain.model

import com.example.ihrm.data.remote.dto.RoleShortDto
import org.junit.Assert.assertEquals
import org.junit.Test

class MyInfoMergeTest {

    @Test
    fun `withMergedProfile keeps avatar from base when detail avatar is null`() {
        val base = sampleMyInfo(
            profile = MyProfile.fromAvatarOnly("https://me/avatar.jpg"),
        )
        val detail = sampleMyProfile(avatarUrl = null, address = "HN")
        val merged = base.withMergedProfile(detail)
        assertEquals("https://me/avatar.jpg", merged.profile?.avatarUrl)
        assertEquals("HN", merged.profile?.address)
    }

    @Test
    fun `withMergedProfile prefers detail avatar when both present`() {
        val base = sampleMyInfo(
            profile = MyProfile.fromAvatarOnly("https://me/old.jpg"),
        )
        val detail = sampleMyProfile(avatarUrl = "https://profile/new.jpg")
        val merged = base.withMergedProfile(detail)
        assertEquals("https://profile/new.jpg", merged.profile?.avatarUrl)
    }

    private fun sampleMyInfo(profile: MyProfile?) = MyInfo(
        employeeId = "e1",
        fullName = "Name",
        email = "e@mail.com",
        phoneNumber = "1",
        id = 1,
        roles = emptyList<RoleShortDto>(),
        status = "ACTIVE",
        profile = profile,
        settings = null,
    )

    private fun sampleMyProfile(
        avatarUrl: String?,
        address: String? = null,
    ) = MyProfile(
        address = address,
        avatarUrl = avatarUrl,
        dateOfBirth = null,
        englishName = null,
        gender = Gender.UNKNOWN,
        identityId = null,
        identityIdIssueDate = null,
        identityIdIssuePlace = null,
        identityIdExpiryDate = null,
        maritalStatus = MaritalStatus.UNKNOWN,
        nationality = null,
        ethnic = null,
        religion = null,
        placeOfBirth = null,
        createdAt = null,
        updatedAt = null,
        level = null,
        title = null,
    )
}
