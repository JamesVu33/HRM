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


val LabelTextStyle13MediumGrey = TextStyle(
    fontFamily = InterFontFamily,
    fontWeight = FontWeight.Medium,
    fontSize = 13.sp,
    color = DashboardFigmaMuted,
    letterSpacing = (-0.0762).sp
)

val ValueTextStyle32BoldBlack = TextStyle(
    fontFamily = InterFontFamily,
    fontWeight = FontWeight.Bold,
    fontSize = 32.sp,
    color = DashboardFigmaInk,
    letterSpacing = 0.4.sp
)

val CaptionTextStyle12NormalGrey = TextStyle(
    fontFamily = InterFontFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 12.sp,
    color = DashboardFigmaMuted
)