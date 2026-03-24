package com.example.ihrm.ui.dashboard.extra

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ihrm.R
import com.example.ihrm.ui.dashboard.DashboardManagementUiModel
import com.example.ihrm.ui.dashboard.ManagementCalendarUiModel
import com.example.ihrm.ui.dashboard.ManagementSecurityUiModel
import com.example.ihrm.ui.theme.DashboardFigmaInk
import com.example.ihrm.ui.theme.DashboardFigmaMuted
import com.example.ihrm.ui.theme.DashboardLeaveBlueEnd
import com.example.ihrm.ui.theme.DashboardLeaveBlueStart
import com.example.ihrm.ui.theme.DashboardTabActiveBlue
import com.example.ihrm.ui.theme.InterFontFamily
import com.example.ihrm.ui.theme.Primary400
import kotlin.math.min

private val MgmtPresentGreen = Color(0xFF00BC7D)
private val MgmtAbsentRed = Color(0xFFFF6467)
private val MgmtSecuritySliceDark = Color(0xFF155DFC)
private val MgmtSecuritySliceMid = Color(0xFF2B7FFF)
private val MgmtSecuritySliceLight = Color(0xFF93C5FD)

@Composable
fun DashboardManagementTabContent(
    ui: DashboardManagementUiModel,
    onNavigate: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        ManagementCalendarCard(
            data = ui.calendar,
            onChevronClick = onNavigate
        )
        ManagementSecurityCard(
            data = ui.security,
            onChevronClick = onNavigate
        )
        Spacer(modifier = Modifier.height(72.dp))
    }
}

@Composable
private fun ManagementCalendarCard(
    data: ManagementCalendarUiModel,
    onChevronClick: () -> Unit
) {
    val presentPct = data.attendanceRatePercent.coerceIn(0, 100)
    val absentPct = (100 - presentPct).coerceIn(0, 100)
    val fPresent = data.presentCount.toFloat() / data.totalHeadcount
    val fAbsent = data.absentCount.toFloat() / data.totalHeadcount
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.6f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
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
                                Brush.linearGradient(
                                    listOf(DashboardLeaveBlueStart, DashboardLeaveBlueEnd)
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.ic_total_allowance),
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Column {
                        Text(
                            text = stringResource(R.string.dashboard_mgmt_calendar_title),
                            style = TextStyle(
                                fontFamily = InterFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = DashboardFigmaInk,
                                letterSpacing = (-0.31).sp
                            )
                        )
                        Text(
                            text = stringResource(R.string.dashboard_mgmt_calendar_subtitle),
                            style = TextStyle(
                                fontFamily = InterFontFamily,
                                fontWeight = FontWeight.Normal,
                                fontSize = 11.sp,
                                color = DashboardFigmaMuted
                            )
                        )
                    }
                }
                IconButton(onClick = onChevronClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = stringResource(R.string.dashboard_cd_navigate_forward),
                        tint = DashboardFigmaMuted,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        modifier = Modifier.offset(y= (-8).dp),
                        text = data.dayOfMonth,
                        style = TextStyle(
                            fontFamily = InterFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 32.sp,
                            brush = Brush.linearGradient(listOf(DashboardFigmaInk, DashboardFigmaInk))
                        )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = data.monthLabel,
                            style = TextStyle(
                                fontFamily = InterFontFamily,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 13.sp,
                                color = DashboardFigmaInk
                            )
                        )
                        Text(
                            text = data.weekdayLabel,
                            style = TextStyle(
                                fontFamily = InterFontFamily,
                                fontWeight = FontWeight.Normal,
                                fontSize = 11.sp,
                                color = DashboardFigmaMuted
                            )
                        )
                    }
                }
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    MiniStatPill(
                        modifier = Modifier.weight(1f),
                        label = stringResource(R.string.dashboard_mgmt_present),
                        value = data.presentCount,
                        tint = MgmtPresentGreen,
                        iconTint = MgmtPresentGreen,
                        background = Brush.linearGradient(
                            listOf(Color(0xFFF0FDF4), Color(0xFFECFDF5))
                        )
                    )
                    MiniStatPill(
                        modifier = Modifier.weight(1f),
                        label = stringResource(R.string.dashboard_mgmt_absent),
                        value = data.absentCount,
                        tint = MgmtAbsentRed,
                        iconTint = MgmtAbsentRed,
                        background = Brush.linearGradient(
                            listOf(Color(0xFFFEF2F2), Color(0xFFFFE9E9))
                        ),
                        isPresent = false
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(Color(0xFFFAFAFA), Color(0xFFF5F5F5))
                        )
                    )
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column {
                        Text(
                            text = stringResource(R.string.dashboard_mgmt_team_attendance),
                            style = TextStyle(
                                fontFamily = InterFontFamily,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 13.sp,
                                color = DashboardFigmaInk
                            )
                        )
                        Text(
                            text = stringResource(R.string.dashboard_mgmt_today_overview),
                            style = TextStyle(
                                fontFamily = InterFontFamily,
                                fontWeight = FontWeight.Normal,
                                fontSize = 11.sp,
                                color = DashboardFigmaMuted
                            )
                        )
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = stringResource(R.string.dashboard_mgmt_rate),
                            style = TextStyle(
                                fontFamily = InterFontFamily,
                                fontWeight = FontWeight.Normal,
                                fontSize = 10.sp,
                                color = DashboardFigmaMuted
                            )
                        )
                        Text(
                            text = "${data.attendanceRatePercent}%",
                            style = TextStyle(
                                fontFamily = InterFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = DashboardFigmaInk
                            )
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier.size(100.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        DonutChartTriple(
                            modifier = Modifier.size(100.dp),
                            fractions = listOf(fPresent, fAbsent),
                            colors = listOf(
                                MgmtPresentGreen,
                                MgmtAbsentRed,
                            ),
                            strokeWidth = 16.dp
                        )
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "${data.totalHeadcount}",
                                style = TextStyle(
                                    fontFamily = InterFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp,
                                    color = DashboardFigmaInk
                                )
                            )
                            Text(
                                text = stringResource(R.string.dashboard_mgmt_total),
                                style = TextStyle(
                                    fontFamily = InterFontFamily,
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 9.sp,
                                    color = DashboardFigmaMuted
                                )
                            )
                        }
                    }

                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        AttendanceLegendRow(
                            dotColor = MgmtPresentGreen,
                            label = stringResource(R.string.dashboard_mgmt_present),
                            usersText = stringResource(
                                R.string.dashboard_mgmt_users_format,
                                data.presentCount
                            ),
                            percentText = "$presentPct%",
                            percentColor = MgmtPresentGreen
                        )
                        AttendanceLegendRow(
                            dotColor = MgmtAbsentRed,
                            label = stringResource(R.string.dashboard_mgmt_absent),
                            usersText = stringResource(
                                R.string.dashboard_mgmt_users_format,
                                data.absentCount
                            ),
                            percentText = "$absentPct%",
                            percentColor = MgmtAbsentRed
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MiniStatPill(
    modifier: Modifier = Modifier,
    label: String,
    value: Int,
    tint: Color,
    iconTint: Color,
    background: Brush,
    isPresent: Boolean = true
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(background)
            .padding(horizontal = 8.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = if(isPresent) ImageVector.vectorResource(R.drawable.icon_person_check) else ImageVector.vectorResource(R.drawable.icon_person_ignore),
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(12.dp)
            )
            Text(
                text = label,
                style = TextStyle(
                    fontFamily = InterFontFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 9.sp,
                    color = DashboardFigmaMuted
                )
            )
        }
        Text(
            text = value.toString(),
            style = TextStyle(
                fontFamily = InterFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = tint
            )
        )
    }
}

@Composable
private fun AttendanceLegendRow(
    dotColor: Color,
    label: String,
    usersText: String,
    percentText: String,
    percentColor: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(Color.White)
            .padding(horizontal = 8.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(dotColor)
            )
            Column {
                Text(
                    text = label,
                    style = TextStyle(
                        fontFamily = InterFontFamily,
                        fontWeight = FontWeight.Normal,
                        fontSize = 11.sp,
                        color = DashboardFigmaMuted
                    )
                )
                Text(
                    text = usersText,
                    style = TextStyle(
                        fontFamily = InterFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp,
                        color = DashboardFigmaInk
                    )
                )
            }
        }
        Text(
            text = percentText,
            style = TextStyle(
                fontFamily = InterFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 11.sp,
                color = percentColor
            )
        )
    }
}

@Composable
private fun ManagementSecurityCard(
    data: ManagementSecurityUiModel,
    onChevronClick: () -> Unit
) {
    val total = (data.submitted + data.pending + data.notSubmitted).coerceAtLeast(1)
    val fSub = data.submitted.toFloat() / total
    val fPen = data.pending.toFloat() / total
    val fNsub = data.notSubmitted.toFloat() / total

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.6f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
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
                                Brush.linearGradient(
                                    listOf(Color(0xFF00D3F3), DashboardTabActiveBlue)
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.ic_security),
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Column {
                        Text(
                            text = stringResource(R.string.dashboard_mgmt_security_title),
                            style = TextStyle(
                                fontFamily = InterFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = DashboardFigmaInk,
                                letterSpacing = (-0.31).sp
                            )
                        )
                        Text(
                            text = stringResource(R.string.dashboard_mgmt_security_subtitle),
                            style = TextStyle(
                                fontFamily = InterFontFamily,
                                fontWeight = FontWeight.Normal,
                                fontSize = 11.sp,
                                color = DashboardFigmaMuted
                            )
                        )
                    }
                }
                IconButton(onClick = onChevronClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = stringResource(R.string.dashboard_cd_navigate_forward),
                        tint = DashboardFigmaMuted,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                SummaryPill(
                    modifier = Modifier.weight(1f),
                    background = Brush.linearGradient(
                        listOf(Color(0xFFEFF6FF), Color(0xFFE8F4FF))
                    ),
                    icon = {
                        Icon(
                            ImageVector.vectorResource(R.drawable.ic_list_person),
                            null,
                            tint = Primary400,
                            modifier = Modifier.size(14.dp)
                        )
                    },
                    label = stringResource(R.string.dashboard_mgmt_total_users),
                    valueText = data.totalUsers.toString(),
                    valueBrush = Brush.linearGradient(listOf(DashboardFigmaInk, DashboardFigmaInk))
                )
                SummaryPill(
                    modifier = Modifier.weight(1f),
                    background = Brush.linearGradient(
                        listOf(Color(0xFFF0FDF4), Color(0xFFECFDF5))
                    ),
                    icon = {
                        Icon(
                            ImageVector.vectorResource(R.drawable.icon_trend_arrow),
                            null,
                            tint = MgmtPresentGreen,
                            modifier = Modifier.size(14.dp)
                        )
                    },
                    label = stringResource(R.string.dashboard_mgmt_submissions),
                    valueText = data.submissionsCount.toString(),
                    valueBrush = Brush.linearGradient(listOf(DashboardFigmaInk, DashboardFigmaInk))
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(Color(0xFFFAFAFA), Color(0xFFF5F5F5))
                        )
                    )
                    .padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column {
                        Text(
                            text = stringResource(R.string.dashboard_mgmt_submission_status),
                            style = TextStyle(
                                fontFamily = InterFontFamily,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 13.sp,
                                color = DashboardFigmaInk
                            )
                        )
                        Text(
                            text = stringResource(R.string.dashboard_mgmt_this_month),
                            style = TextStyle(
                                fontFamily = InterFontFamily,
                                fontWeight = FontWeight.Normal,
                                fontSize = 11.sp,
                                color = DashboardFigmaMuted
                            )
                        )
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = stringResource(R.string.dashboard_mgmt_total_users_header),
                            style = TextStyle(
                                fontFamily = InterFontFamily,
                                fontWeight = FontWeight.Normal,
                                fontSize = 10.sp,
                                color = DashboardFigmaMuted,
                                textAlign = TextAlign.End
                            )
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF00D3F3))
                            )
                            Text(
                                text = stringResource(
                                    R.string.dashboard_mgmt_total_users_count,
                                    data.totalUsers
                                ),
                                style = TextStyle(
                                    fontFamily = InterFontFamily,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 11.sp,
                                    color = DashboardFigmaInk
                                )
                            )
                        }
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp),
                    contentAlignment = Alignment.Center
                ) {
                    DonutChartTriple(
                        modifier = Modifier.size(160.dp),
                        fractions = listOf(fSub, fPen, fNsub),
                        colors = listOf(
                            MgmtSecuritySliceDark,
                            MgmtSecuritySliceMid,
                            MgmtSecuritySliceLight
                        ),
                        strokeWidth = 18.dp
                    )
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "${data.totalUsers}",
                            style = TextStyle(
                                fontFamily = InterFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 24.sp,
                                color = DashboardFigmaInk
                            )
                        )
                        Text(
                            text = stringResource(R.string.dashboard_mgmt_users_format),
                            style = TextStyle(
                                fontFamily = InterFontFamily,
                                fontWeight = FontWeight.Normal,
                                fontSize = 10.sp,
                                color = DashboardFigmaMuted,
                                textAlign = TextAlign.Center
                            )
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    SecurityLegendLine(
                        color = MgmtSecuritySliceDark,
                        title = R.string.dashboard_mgmt_legend_submitted,
                        text = data.submitted.toString()
                    )
                    SecurityLegendLine(
                        color = MgmtSecuritySliceMid,
                        title = R.string.dashboard_mgmt_legend_pending,
                        text = data.pending.toString()
                    )
                    SecurityLegendLine(
                        color = MgmtSecuritySliceLight,
                        title = R.string.dashboard_mgmt_legend_not_submitted,
                        text = data.notSubmitted.toString()
                    )
                }
            }
        }
    }
}

@Composable
private fun SummaryPill(
    modifier: Modifier = Modifier,
    background: Brush,
    icon: @Composable () -> Unit,
    label: String,
    valueText: String,
    valueBrush: Brush
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(background)
            .padding(horizontal = 12.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            icon()
            Text(
                text = label,
                style = TextStyle(
                    fontFamily = InterFontFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 11.sp,
                    color = DashboardFigmaMuted
                )
            )
        }
        Text(
            text = valueText,
            style = TextStyle(
                fontFamily = InterFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                brush = valueBrush
            )
        )
    }
}

@Composable
private fun SecurityLegendLine(color: Color, text: String, title: Int) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(color)
        )
        Column {
            Text(
                text = stringResource(id = title),
                style = TextStyle(
                    fontFamily = InterFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 9.sp,
                    color = DashboardFigmaMuted
                )
            )

            Text(
                text = text,
                style = TextStyle(
                    fontFamily = InterFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                    color = DashboardFigmaInk
                )
            )
        }
    }
}

/**
 * Three-segment donut matching Figma PieChart (871:35217): white gaps between arcs,
 * flat stroke caps, ring inset inside the canvas (6.67% vertical, 17.5% horizontal).
 */
@Composable
private fun DonutChartTriple(
    modifier: Modifier = Modifier,
    fractions: List<Float>,
    colors: List<Color>,
    strokeWidth: Dp
) {
    Canvas(modifier = modifier) {
        val n = fractions.size
        if (n == 0 || colors.size != n) return@Canvas

        val sum = fractions.sum()
        if (sum <= 0f) return@Canvas
        val normalized = fractions.map { it / sum }

        // Figma: thin gaps between segments (background shows through).
        val gapDegrees = 3.2f
        val totalGaps = n * gapDegrees
        val sweepBudget = (360f - totalGaps).coerceAtLeast(0f)
        if (sweepBudget <= 0f) return@Canvas

        // Figma Surface inset on the pie: ~6.67% vertical, ~17.5% horizontal.
        val padH = size.width * 0.175f
        val padV = size.height * 0.0667f
        val innerW = (size.width - 2f * padH).coerceAtLeast(0f)
        val innerH = (size.height - 2f * padV).coerceAtLeast(0f)
        val diameter = min(innerW, innerH)
        if (diameter <= 0f) return@Canvas

        val left = padH + (innerW - diameter) / 2f
        val top = padV + (innerH - diameter) / 2f
        val topLeft = Offset(left, top)
        val arcSize = Size(diameter, diameter)

        val strokePx = strokeWidth.toPx()
        val strokeStyle = Stroke(width = strokePx, cap = StrokeCap.Butt)

        var startAngle = -90f
        normalized.zip(colors).forEach { (fraction, color) ->
            val sweep = fraction * sweepBudget
            if (sweep > 0.05f) {
                drawArc(
                    color = color,
                    startAngle = startAngle,
                    sweepAngle = sweep,
                    useCenter = false,
                    topLeft = topLeft,
                    size = arcSize,
                    style = strokeStyle
                )
            }
            startAngle += sweep + gapDegrees
        }
    }
}
