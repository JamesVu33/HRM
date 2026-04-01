package com.example.ihrm.ui.myinfo

import com.example.ihrm.domain.model.MaritalStatus
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class MyInfoFormBaselineTest {

    private val baseline = MyInfoFormBaseline(
        fullName = "A",
        englishName = "B",
        email = "c@d.e",
        phone = "1",
        dob = "01/01/2000",
        identityId = "id",
        identityIssueDay = "02/02/2000",
        identityIssuePlace = "HCM",
        address = "addr",
        countryCode = "VN",
        gender = GenderOption.Male,
        marital = MaritalStatus.SINGLE,
    )

    private fun current(
        fullNameText: String = baseline.fullName,
        englishNameText: String = baseline.englishName,
        emailText: String = baseline.email,
        phoneText: String = baseline.phone,
        dobText: String = baseline.dob,
        identityIdText: String = baseline.identityId,
        identityIssueDayText: String = baseline.identityIssueDay,
        identityIssuePlaceText: String = baseline.identityIssuePlace,
        addressText: String = baseline.address,
        countryCodeText: String = baseline.countryCode,
        genderSelected: GenderOption = baseline.gender,
        selectedMaritalStatus: MaritalStatus? = baseline.marital,
    ) = baseline.differsFrom(
        fullNameText,
        englishNameText,
        emailText,
        phoneText,
        dobText,
        identityIdText,
        identityIssueDayText,
        identityIssuePlaceText,
        addressText,
        countryCodeText,
        genderSelected,
        selectedMaritalStatus,
    )

    @Test
    fun `differsFrom false when identical including trim-insignificant`() {
        assertFalse(current())
        assertFalse(
            current(
                fullNameText = "  ${baseline.fullName}  ",
                emailText = "  ${baseline.email}  ",
            ),
        )
    }

    @Test
    fun `differsFrom true when any field changes`() {
        assertTrue(current(fullNameText = "Other"))
        assertTrue(current(genderSelected = GenderOption.Female))
        assertTrue(current(selectedMaritalStatus = MaritalStatus.MARRIED))
        assertTrue(current(selectedMaritalStatus = null))
    }
}
