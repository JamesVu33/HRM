package com.example.ihrm.util

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.example.ihrm.ui.theme.Primary200
import com.example.ihrm.ui.theme.Primary400
import com.example.ihrm.ui.theme.Primary50
import com.example.ihrm.ui.theme.Primary500

object DashboardBrush {
    val BaseBackground = Brush.linearGradient(
        colors = listOf(
            Primary500,
            Primary400,
            Primary200,
            Primary50
        )
    )

    val Approved = Brush.horizontalGradient(
        listOf(
            Color(0xFF51A2FF),
            Color(0xFF2B7FFF),
            Color(0xFF155DFC),
        )
    )

    val Rechecking = Brush.horizontalGradient(
        listOf(
            Color(0xFFFDC700),
            Color(0xFFF0B100),
            Color(0xFFFE9A00),
        )
    )

    val Rejected = Brush.horizontalGradient(
        listOf(
            Color(0xFFFF6467),
            Color(0xFFFB2C36),
            Color(0xFFEC003F),
        )
    )

    val Transparent = Brush.horizontalGradient(
        listOf(
            Color(0x00000000),
            Color(0x00000000),
        )
    )
}