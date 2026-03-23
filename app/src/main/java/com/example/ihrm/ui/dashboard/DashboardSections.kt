package com.example.ihrm.ui.dashboard

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Phone
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ihrm.R
import com.example.ihrm.ui.theme.DashboardFigmaInk
import com.example.ihrm.ui.theme.DashboardFigmaMuted
import com.example.ihrm.ui.theme.DashboardLeaveAmberEnd
import com.example.ihrm.ui.theme.DashboardLeaveAmberStart
import com.example.ihrm.ui.theme.DashboardLeaveBlueEnd
import com.example.ihrm.ui.theme.DashboardLeaveBlueStart
import com.example.ihrm.ui.theme.DashboardLeaveGreenEnd
import com.example.ihrm.ui.theme.DashboardLeaveGreenStart
import com.example.ihrm.ui.theme.DashboardLeaveRedEnd
import com.example.ihrm.ui.theme.DashboardLeaveRedStart
import com.example.ihrm.ui.theme.DashboardProfileCellBg
import com.example.ihrm.ui.theme.DashboardProfileTitleBlue
import com.example.ihrm.ui.theme.DashboardSecurityTrack
import com.example.ihrm.ui.theme.DashboardShieldCyan
import com.example.ihrm.ui.theme.DashboardTabActiveBlue
import com.example.ihrm.ui.theme.InterFontFamily
import com.example.ihrm.ui.theme.PrimaryTint
import com.example.ihrm.util.Avatar

@Composable
fun DashboardProfileCard(
    profile: DashboardProfileModel,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.6f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.2f),
                            Color.Transparent
                        )
                    )
                )
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Avatar(
                    imageUrl = profile.avatarUrl,
                    initials = profile.avatarInitials,
                    isOnline = true
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = profile.displayName,
                        style = TextStyle(
                            fontFamily = InterFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            lineHeight = 24.sp,
                            color = DashboardProfileTitleBlue,
                            letterSpacing = (-0.31).sp
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = stringResource(R.string.dashboard_id_format, profile.employeeId),
                        style = TextStyle(
                            fontFamily = InterFontFamily,
                            fontWeight = FontWeight.Normal,
                            fontSize = 12.sp,
                            lineHeight = 18.sp,
                            color = DashboardFigmaMuted
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .background(PrimaryTint)
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = profile.departmentBadge,
                            style = TextStyle(
                                fontFamily = InterFontFamily,
                                fontWeight = FontWeight.Medium,
                                fontSize = 10.sp,
                                lineHeight = 15.sp,
                                color = DashboardTabActiveBlue
                            )
                        )
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ProfileInfoCell(
                    icon = {
                        Icon(
                            Icons.Default.Email,
                            contentDescription = null,
                            tint = DashboardTabActiveBlue,
                            modifier = Modifier.size(14.dp)
                        )
                    },
                    label = stringResource(R.string.dashboard_profile_email_label),
                    value = profile.email,
                    valueSemiBold = true,
                    modifier = Modifier.weight(1f)
                )
                ProfileInfoCell(
                    icon = {
                        Icon(
                            Icons.Default.Phone,
                            contentDescription = null,
                            tint = DashboardTabActiveBlue,
                            modifier = Modifier.size(14.dp)
                        )
                    },
                    label = stringResource(R.string.dashboard_profile_phone_label),
                    value = profile.phone,
                    valueSemiBold = true,
                    modifier = Modifier.weight(1f)
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ProfileInfoCell(
                    icon = {
                        Icon(
                            Icons.Default.Edit,
                            contentDescription = null,
                            tint = DashboardTabActiveBlue,
                            modifier = Modifier.size(14.dp)
                        )
                    },
                    label = stringResource(R.string.dashboard_profile_joined_label),
                    value = profile.joined,
                    valueSemiBold = true,
                    modifier = Modifier.weight(1f)
                )
                ProfileInfoCell(
                    icon = {
                        Icon(
                            Icons.Default.Home,
                            contentDescription = null,
                            tint = DashboardTabActiveBlue,
                            modifier = Modifier.size(14.dp)
                        )
                    },
                    label = stringResource(R.string.dashboard_profile_department_label),
                    value = profile.departmentDetail,
                    valueSemiBold = true,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun ProfileInfoCell(
    icon: @Composable () -> Unit,
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    valueSemiBold: Boolean = false
) {
    Row(
        modifier = modifier
            .height(48.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(DashboardProfileCellBg)
            .padding(horizontal = 8.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(PrimaryTint),
            contentAlignment = Alignment.Center
        ) {
            icon()
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                style = TextStyle(
                    fontFamily = InterFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 9.sp,
                    lineHeight = 13.5.sp,
                    color = DashboardFigmaMuted
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = value,
                style = TextStyle(
                    fontFamily = InterFontFamily,
                    fontWeight = if (valueSemiBold) FontWeight.SemiBold else FontWeight.Medium,
                    fontSize = 11.sp,
                    lineHeight = 16.5.sp,
                    color = DashboardFigmaInk
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun DashboardLeaveSection(
    stats: List<LeaveStatModel>,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = stringResource(R.string.dashboard_leave_section_title),
            style = TextStyle(
                fontFamily = InterFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                lineHeight = 24.sp,
                color = Color.White,
                letterSpacing = (-0.44).sp
            )
        )
        Spacer(modifier = Modifier.height(12.dp))
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            stats.chunked(2).forEach { row ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    row.forEach { stat ->
                        LeaveStatCard(
                            stat = stat,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    if (row.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Composable
private fun LeaveStatCard(
    stat: LeaveStatModel,
    modifier: Modifier = Modifier
) {
    val (g1, g2) = when (stat.accent) {
        LeaveStatAccent.Blue -> DashboardLeaveBlueStart to DashboardLeaveBlueEnd
        LeaveStatAccent.Red -> DashboardLeaveRedStart to DashboardLeaveRedEnd
        LeaveStatAccent.Amber -> DashboardLeaveAmberStart to DashboardLeaveAmberEnd
        LeaveStatAccent.Green -> DashboardLeaveGreenStart to DashboardLeaveGreenEnd
    }
    val icon = when (stat.accent) {
        LeaveStatAccent.Blue -> ImageVector.vectorResource(R.drawable.ic_total_allowance)
        LeaveStatAccent.Red -> ImageVector.vectorResource(R.drawable.ic_used)
        LeaveStatAccent.Amber -> ImageVector.vectorResource(R.drawable.ic_pending)
        LeaveStatAccent.Green -> ImageVector.vectorResource(R.drawable.ic_remaining)
    }
    Card(
        modifier = modifier.height(148.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.6f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(stat.titleRes),
                    style = TextStyle(
                        fontFamily = InterFontFamily,
                        fontWeight = FontWeight.Medium,
                        fontSize = 12.sp,
                        lineHeight = 18.sp,
                        color = DashboardFigmaMuted
                    ),
                    modifier = Modifier.weight(1f, fill = false),
                    maxLines = 2
                )
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .shadow(4.dp, RoundedCornerShape(14.dp))
                        .clip(RoundedCornerShape(14.dp))
                        .background(Brush.linearGradient(listOf(g1, g2))),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = stat.value.toString(),
                style = TextStyle(
                    fontFamily = InterFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 32.sp,
                    lineHeight = 36.sp,
                    brush = Brush.linearGradient(listOf(g1, g2)),
                    letterSpacing = 0.37.sp
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(stat.descriptionRes),
                style = TextStyle(
                    fontFamily = InterFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 11.sp,
                    lineHeight = 16.sp,
                    color = DashboardFigmaMuted
                ),
                maxLines = 3
            )
        }
    }
}

@Composable
fun DashboardSecurityCard(
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
        border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.6f))
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
                SecurityStatRow(
                    label = stringResource(R.string.dashboard_security_approved),
                    value = monthly.approved.toString()
                )
                SecurityStatRow(
                    label = stringResource(R.string.dashboard_security_rechecking),
                    value = monthly.rechecking.toString()
                )
                Column {
                    SecurityStatRow(
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

@Composable
private fun SecurityStatRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = TextStyle(
                fontFamily = InterFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp,
                lineHeight = 18.sp,
                color = DashboardFigmaInk
            )
        )
        Text(
            text = value,
            style = TextStyle(
                fontFamily = InterFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp,
                lineHeight = 18.sp,
                color = DashboardFigmaInk
            )
        )
    }
}
