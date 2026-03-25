package com.example.ihrm.ui.stats

import androidx.compose.ui.graphics.Color

data class SummaryCardData(
    val label: String,
    val value: String,
    val caption: String,
    val iconBackground: Color,
    val iconRes: Int,
    val iconTint: Color
)

