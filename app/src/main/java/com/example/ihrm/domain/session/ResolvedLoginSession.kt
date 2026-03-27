package com.example.ihrm.domain.session

import com.example.ihrm.domain.model.AccountType
import com.example.ihrm.domain.model.AppFeature

data class ResolvedLoginSession(
    val accountType: AccountType,
    val modifiableFeatures: Set<AppFeature>,
)
