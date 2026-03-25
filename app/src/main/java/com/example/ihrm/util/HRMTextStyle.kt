package com.example.ihrm.util

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.ihrm.ui.theme.ColorBgSuccessGradientBottom
import com.example.ihrm.ui.theme.ColorBgSuccessGradientBottom80Percent
import com.example.ihrm.ui.theme.DashboardFigmaInk
import com.example.ihrm.ui.theme.DashboardFigmaMuted
import com.example.ihrm.ui.theme.DashboardTabActiveBlue
import com.example.ihrm.ui.theme.InterFontFamily

val statLabelStyle = TextStyle(
    fontFamily = InterFontFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 12.sp,
    color = DashboardFigmaInk
)

val statValueStyle = TextStyle(
    fontFamily = InterFontFamily,
    fontWeight = FontWeight.SemiBold,
    fontSize = 12.sp,
    color = DashboardFigmaInk
)

val statTitleStyle = TextStyle(
    fontFamily = InterFontFamily,
    fontWeight = FontWeight.SemiBold,
    fontSize = 14.sp,
    color = DashboardFigmaInk,
)

val txtInterRegular12 = TextStyle(
    fontFamily = InterFontFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 12.sp,
    color = DashboardFigmaMuted
)

val txtInterRegular14 = TextStyle(
    fontFamily = InterFontFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 14.sp,
    color = ColorBgSuccessGradientBottom80Percent
)

val txtInterMedium14 = TextStyle(
    fontFamily = InterFontFamily,
    fontWeight = FontWeight.Medium,
    fontSize = 14.sp,
    color = DashboardTabActiveBlue
)

val txtInterBold16 = TextStyle(
    fontFamily = InterFontFamily,
    fontWeight = FontWeight.SemiBold,
    fontSize = 16.sp,
    color = DashboardFigmaInk
)

val txtInterBold24 = TextStyle(
    fontFamily = InterFontFamily,
    fontWeight = FontWeight.SemiBold,
    fontSize = 24.sp,
    color = ColorBgSuccessGradientBottom
)