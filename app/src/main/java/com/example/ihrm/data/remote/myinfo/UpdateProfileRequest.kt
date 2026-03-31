package com.example.ihrm.data.remote.myinfo

data class UpdateProfileRequest(
    val gender: String? = null,
    val avatarUrl: String? = null,
    val dateOfBirth: String? = null,
    val nationality: String? = null,
    val identityId: String? = null,
    val identityIdIssueDate: String? = null,
    val identityIdIssuePlace: String? = null,
    val englishName: String? = null,
    val address: String? = null,
    val maritalStatus: String? = null
)