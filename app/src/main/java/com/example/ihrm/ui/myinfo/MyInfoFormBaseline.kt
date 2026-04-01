package com.example.ihrm.ui.myinfo

import com.example.ihrm.domain.model.Gender
import com.example.ihrm.domain.model.MaritalStatus

internal enum class GenderOption {
    Male,
    Female,
}

internal fun uiGenderToRequestValue(option: GenderOption): String {
    return when (option) {
        GenderOption.Male -> Gender.MALE.name
        GenderOption.Female -> Gender.FEMALE.name
    }
}

internal fun domainGenderToUiOption(g: Gender?): GenderOption {
    return when (g) {
        Gender.MALE -> GenderOption.Male
        Gender.FEMALE -> GenderOption.Female
        Gender.OTHER, Gender.UNKNOWN, null -> GenderOption.Male
    }
}

/** Snapshot form sau mỗi lần đồng bộ từ server; dùng để bật nút Change info khi user sửa khác baseline. */
internal data class MyInfoFormBaseline(
    val fullName: String,
    val englishName: String,
    val email: String,
    val phone: String,
    val dob: String,
    val identityId: String,
    val identityIssueDay: String,
    val identityIssuePlace: String,
    val address: String,
    val countryCode: String,
    val gender: GenderOption,
    val marital: MaritalStatus?,
) {
    fun differsFrom(
        fullNameText: String,
        englishNameText: String,
        emailText: String,
        phoneText: String,
        dobText: String,
        identityIdText: String,
        identityIssueDayText: String,
        identityIssuePlaceText: String,
        addressText: String,
        countryCodeText: String,
        genderSelected: GenderOption,
        selectedMaritalStatus: MaritalStatus?,
    ): Boolean {
        if (fullName.trim() != fullNameText.trim()) return true
        if (englishName.trim() != englishNameText.trim()) return true
        if (email.trim() != emailText.trim()) return true
        if (phone.trim() != phoneText.trim()) return true
        if (dob.trim() != dobText.trim()) return true
        if (identityId.trim() != identityIdText.trim()) return true
        if (identityIssueDay.trim() != identityIssueDayText.trim()) return true
        if (identityIssuePlace.trim() != identityIssuePlaceText.trim()) return true
        if (address.trim() != addressText.trim()) return true
        if (countryCode.trim() != countryCodeText.trim()) return true
        if (gender != genderSelected) return true
        if (marital != selectedMaritalStatus) return true
        return false
    }
}
