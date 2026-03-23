package com.example.ihrm.ui.dashboard.extra

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ihrm.R
import com.example.ihrm.ui.dashboard.DashboardProfileModel
import com.example.ihrm.ui.dashboard.SecurityMonthlyModel
import com.example.ihrm.ui.dashboard.securityRejectedShare
import com.example.ihrm.ui.theme.DashboardFigmaInk
import com.example.ihrm.ui.theme.DashboardFigmaMuted
import com.example.ihrm.ui.theme.DashboardLeaveRedEnd
import com.example.ihrm.ui.theme.DashboardLeaveRedStart
import com.example.ihrm.ui.theme.DashboardSecurityTrack
import com.example.ihrm.ui.theme.DashboardShieldCyan
import com.example.ihrm.ui.theme.DashboardTabActiveBlue
import com.example.ihrm.ui.theme.InterFontFamily
import com.example.ihrm.util.Avatar

@Composable
fun DashboardSecurityCardManagement(
    monthly: SecurityMonthlyModel,
    profile: DashboardProfileModel,
    modifier: Modifier = Modifier
) {
    val share = securityRejectedShare(monthly.approved, monthly.rechecking, monthly.rejected)
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.6f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .shadow(6.dp, RoundedCornerShape(14.dp))
                        .clip(RoundedCornerShape(14.dp))
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(DashboardShieldCyan, DashboardTabActiveBlue)
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Column {
                    Text(
                        text = stringResource(R.string.dashboard_security_title),
                        style = TextStyle(
                            fontFamily = InterFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            lineHeight = 21.sp,
                            color = DashboardFigmaInk,
                            letterSpacing = (-0.31).sp
                        )
                    )
                    Text(
                        text = stringResource(R.string.dashboard_security_subtitle),
                        style = TextStyle(
                            fontFamily = InterFontFamily,
                            fontWeight = FontWeight.Normal,
                            fontSize = 11.sp,
                            lineHeight = 16.5.sp,
                            color = DashboardFigmaMuted
                        )
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Avatar(
                    imageUrl = profile.avatarSecurity,
                    initials = profile.avatarInitials,
                    isSecurity = true,
                    placeholderImage = painterResource(R.drawable.ic_avatar_security),
                    errorImage = painterResource(R.drawable.ic_avatar_security)
                )
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(14.dp))
                        .border(1.dp, Color(0xFFDBEAFE), RoundedCornerShape(14.dp))
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    Color(0xFFEFF6FF),
                                    Color(0xFFEFF7FF),
                                    Color(0xFFEFF8FF),
                                    Color(0xFFEFF9FF),
                                    Color(0xFFEFFAFF),
                                    Color(0xFFEDFBFF),
                                    Color(0xFFEDFCFF),
                                    Color(0xFFECFDFF),
                                    Color(0xFFECFEFF)
                                )
                            )
                        )
                        .padding(12.dp)
                ) {
                    Text(
                        text = stringResource(R.string.dashboard_security_in_review),
                        style = TextStyle(
                            fontFamily = InterFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.sp,
                            lineHeight = 19.5.sp,
                            color = DashboardFigmaInk,
                            letterSpacing = (-0.31).sp
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = stringResource(R.string.dashboard_security_in_review_body),
                        style = TextStyle(
                            fontFamily = InterFontFamily,
                            fontWeight = FontWeight.Normal,
                            fontSize = 11.sp,
                            lineHeight = 16.sp,
                            color = DashboardFigmaMuted
                        )
                    )
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = stringResource(R.string.dashboard_security_this_month),
                    style = TextStyle(
                        fontFamily = InterFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp,
                        lineHeight = 19.5.sp,
                        color = DashboardFigmaInk,
                        letterSpacing = (-0.31).sp
                    )
                )
                SecurityStatRowManagement(
                    label = stringResource(R.string.dashboard_security_approved),
                    value = monthly.approved.toString()
                )
                SecurityStatRowManagement(
                    label = stringResource(R.string.dashboard_security_rechecking),
                    value = monthly.rechecking.toString()
                )
                Column {
                    SecurityStatRowManagement(
                        label = stringResource(R.string.dashboard_security_rejected),
                        value = monthly.rejected.toString()
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(50))
                            .background(DashboardSecurityTrack)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(share.coerceIn(0f, 1f))
                                .height(6.dp)
                                .clip(RoundedCornerShape(50))
                                .background(
                                    Brush.horizontalGradient(
                                        listOf(DashboardLeaveRedStart, DashboardLeaveRedEnd)
                                    )
                                )
                        )
                    }
                }
            }
        }
    }
}