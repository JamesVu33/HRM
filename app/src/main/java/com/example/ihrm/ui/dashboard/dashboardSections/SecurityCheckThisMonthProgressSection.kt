package com.example.ihrm.ui.dashboard.dashboardSections

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.ihrm.R
import com.example.ihrm.ui.dashboard.SecurityMonthlyModel
import com.example.ihrm.ui.dashboard.calculateShare
import com.example.ihrm.ui.theme.DashboardSecurityTrack
import com.example.ihrm.util.DashboardBrush
import com.example.ihrm.util.statLabelStyle
import com.example.ihrm.util.statTitleStyle
import com.example.ihrm.util.statValueStyle
import com.example.ihrm.ui.localization.tr

data class SecurityStatItem(
    val label: String,
    val value: Int,
    val color: Brush? = null
)

@Composable
fun SecurityCheckThisMonthProgressSection(
    monthly: SecurityMonthlyModel
) {
    val values = listOf(monthly.approved, monthly.rechecking, monthly.rejected)
    val stats = listOf(
        SecurityStatItem("Approved", monthly.approved, color = DashboardBrush.Approved),
        SecurityStatItem("Re-checking", monthly.rechecking, color = DashboardBrush.Rechecking),
        SecurityStatItem("Rejected", monthly.rejected, color = DashboardBrush.Rejected)
    )

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = tr(R.string.dashboard_security_this_month),
            style = statTitleStyle
        )
        Spacer(modifier = Modifier.height(8.dp))
        stats.forEachIndexed { index, item ->
            SecurityStatRow(
                label = item.label,
                value = item.value.toString()
            )
            Spacer(modifier = Modifier.height(4.dp))
            val share = calculateShare(values[index], values)
            SecurityProgressBar(progress = share, color = item.color ?: DashboardBrush.Transparent)
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
        Text(text = label, style = statLabelStyle)
        Text(text = value, style = statValueStyle)
    }
}

@Composable
private fun SecurityProgressBar(progress: Float, color: Brush) {
    val safeProgress = progress.coerceIn(0f, 1f)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(6.dp)
            .clip(RoundedCornerShape(50))
            .background(DashboardSecurityTrack)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(safeProgress)
                .height(6.dp)
                .clip(RoundedCornerShape(50))
                .background(color)
        )
    }
}