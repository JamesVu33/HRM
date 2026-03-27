package com.example.ihrm.ui.stats

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import com.example.ihrm.ui.theme.CardBorder
import com.example.ihrm.util.CaptionTextStyle12NormalGrey
import com.example.ihrm.util.LabelTextStyle13MediumGrey
import com.example.ihrm.util.ValueTextStyle32BoldBlack

data class SummaryCardData(
    val label: String,
    val value: String,
    val caption: String,
    val iconBackground: Color,
    val iconRes: Int,
    val iconTint: Color,
    val containerBrush: Brush? = null,
    val containerColor: Color = Color.White,
    val borderColor: Color = CardBorder,
    val textStyleLabel: TextStyle = LabelTextStyle13MediumGrey,
    val textStyleValue: TextStyle = ValueTextStyle32BoldBlack,
    val textStyleCaption: TextStyle = CaptionTextStyle12NormalGrey,
    val plainHeaderIcon: Boolean = false,
    val trendText: String? = null,
    val headerIconTint: Color = Color(0xFF8E8E93),
    val trendTint: Color = Color(0xFF34C759),
)

