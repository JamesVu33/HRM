package com.example.ihrm.util

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.colorspace.WhitePoint
import com.example.ihrm.ui.theme.DashboardTabActiveBlue
import com.example.ihrm.ui.theme.DrawerItemSelected
import com.example.ihrm.ui.theme.Primary200
import com.example.ihrm.ui.theme.Primary400
import com.example.ihrm.ui.theme.Primary50
import com.example.ihrm.ui.theme.Primary500
import com.example.ihrm.ui.theme.SplashBlue100
import com.example.ihrm.ui.theme.SplashTitleBlue

object DashboardBrush {
    val BaseBackground = Brush.verticalGradient(
        colorStops = arrayOf(
            0.00f to Primary500,
            0.50f to Primary400,
            0.75f to Primary200,
            1.00f to Primary50,
        )
    )

    val BaseCardBackground = Brush.verticalGradient(
        colorStops = arrayOf(
            0.00f to SplashBlue100,
            1.00f to SplashTitleBlue,
        )
    )

    /** UI aligned with [Figma 871:37416](https://www.figma.com/design/Q9qmml4Qj4FD73VnYshBsO/HRM-Mobile-App-Ver?node-id=871-37416). */
    val MyInfoHeaderBrush = Brush.linearGradient(
        colorStops = arrayOf(
            0f to Color(0xFF0747A6),
            0.5f to Color(0xFF2684FF),
            1f to Color(0xFFB3D4FF),
        ),
        start = Offset.Zero,
        end = Offset.Infinite,
    )

    val BaseBackgroundItemSelected = Brush.verticalGradient(
        colorStops = arrayOf(
            0.00f to DashboardTabActiveBlue.copy(0.1f),
            1.00f to DrawerItemSelected.copy(0.05f),
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

    val WhitePoint = Brush.horizontalGradient(
        listOf(
            Color(0xFFFFFFFF),
            Color(0xFFFFFFFF),
        )
    )
}