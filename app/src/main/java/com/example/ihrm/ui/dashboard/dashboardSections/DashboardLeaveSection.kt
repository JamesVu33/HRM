package com.example.ihrm.ui.dashboard.dashboardSections

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ihrm.R
import com.example.ihrm.ui.dashboard.LeaveStatAccent
import com.example.ihrm.ui.dashboard.LeaveStatModel
import com.example.ihrm.ui.theme.DashboardFigmaMuted
import com.example.ihrm.ui.theme.DashboardLeaveAmberEnd
import com.example.ihrm.ui.theme.DashboardLeaveAmberStart
import com.example.ihrm.ui.theme.DashboardLeaveBlueEnd
import com.example.ihrm.ui.theme.DashboardLeaveBlueStart
import com.example.ihrm.ui.theme.DashboardLeaveGreenEnd
import com.example.ihrm.ui.theme.DashboardLeaveGreenStart
import com.example.ihrm.ui.theme.DashboardLeaveRedEnd
import com.example.ihrm.ui.theme.DashboardLeaveRedStart
import com.example.ihrm.ui.theme.InterFontFamily

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
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.6f))
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