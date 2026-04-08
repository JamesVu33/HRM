package com.example.ihrm.ui.security.checks

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ihrm.ui.common.DonutChartTriple
import com.example.ihrm.ui.common.header.BaseHeader
import com.example.ihrm.R
import com.example.ihrm.ui.common.TopSummaryCard
import com.example.ihrm.ui.stats.SummaryCardData
import com.example.ihrm.ui.theme.DashboardFigmaInk
import com.example.ihrm.ui.theme.InterFontFamily
import com.example.ihrm.util.CaptionTextStyle13Regular9C9CA1
import com.example.ihrm.util.DashboardBrush
import com.example.ihrm.util.LabelTextStyle13Regular8E8E93
import com.example.ihrm.util.ValueTextStyle32BoldBlack
import com.example.ihrm.util.ValueTextStyle36BoldBlack
import com.example.ihrm.util.singleClick
import com.example.ihrm.ui.localization.tr

/** Static metrics aligned with [Figma 871:40126](https://www.figma.com/design/Q9qmml4Qj4FD73VnYshBsO/HRM-Mobile-App-Ver?node-id=871-40126). */
private object SecurityChecksAnalyticsStatic {
    const val totalSubmissions = 68
    const val activeUsers = 45
}

private data class LegendEntry(
    val label: String,
    val value: Int,
    val color: Color,
)

/** Dummy counts for status donut + legend — [Figma 871:40157](https://www.figma.com/design/Q9qmml4Qj4FD73VnYshBsO/HRM-Mobile-App-Ver?node-id=871-40157). */
private object SecurityChecksStatusDonutDummy {
    const val submitted = 20
    const val pending = 10
    const val rejected = 3
    const val notSubmitted = 12
    val fractions: List<Float>
        get() = listOf(submitted, pending, rejected, notSubmitted).map { it.toFloat() }
    val total: Int
        get() = submitted + pending + rejected + notSubmitted
}

private val StatusDonutSegmentColors = listOf(
    Color(0xFF007AFF),
    Color(0xFF5AC8FA),
    Color(0xFFFF3B30),
    Color(0xFF8E8E93),
)

private val AnalyticsCardBorder = Color(0xFFE5E5EA)

private val AnalyticsCurrentStatusTitleStyle = TextStyle(
    fontFamily = InterFontFamily,
    fontWeight = FontWeight.Bold,
    fontSize = 20.sp,
    color = DashboardFigmaInk,
    letterSpacing = (-0.45).sp,
    lineHeight = 30.sp,
)

private val AnalyticsLegendValueStyle = TextStyle(
    fontFamily = InterFontFamily,
    fontWeight = FontWeight.SemiBold,
    fontSize = 17.sp,
    color = DashboardFigmaInk,
    letterSpacing = (-0.43).sp,
    lineHeight = 25.5.sp,
)

@Composable
fun SecurityChecksAnalyticsScreen(
    onBackClick: () -> Unit,
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding(),
        containerColor = Color.Transparent,
        contentWindowInsets = WindowInsets(top = 0),
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(DashboardBrush.BaseBackground)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(
                    top = paddingValues.calculateTopPadding() + WindowInsets.statusBars
                        .asPaddingValues()
                        .calculateTopPadding(),
                    bottom = paddingValues.calculateBottomPadding()
                ),
            ) {
                item {
                    BaseHeader(
                        title = tr(R.string.security_checks_analytics_title),
                        showNavigationIcon = true,
                        onNavigationClick = onBackClick.singleClick(),
                        navigationIcon = {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = null,
                                tint = Color.White
                            )
                        },
                        containerColor = Color.Transparent
                    )
                }
                item { Spacer(modifier = Modifier.height(12.dp)) }
                item {
                    val summaryCards = listOf(
                        SummaryCardData(
                            label = tr(R.string.security_checks_analytics_total_submissions),
                            value = SecurityChecksAnalyticsStatic.totalSubmissions.toString(),
                            caption = tr(R.string.security_checks_analytics_this_month),
                            iconBackground = Color.Transparent,
                            iconRes = R.drawable.ic_templates,
                            iconTint = Color.Transparent,
                            plainHeaderIcon = true,
                            containerColor = Color.White,
                            borderColor = Color.Transparent,
                            textStyleLabel = LabelTextStyle13Regular8E8E93,
                            textStyleValue = ValueTextStyle36BoldBlack,
                            textStyleCaption = CaptionTextStyle13Regular9C9CA1,
                        ),
                        SummaryCardData(
                            label = tr(R.string.security_checks_analytics_active_users),
                            value = SecurityChecksAnalyticsStatic.activeUsers.toString(),
                            caption = "",
                            iconBackground = Color.Transparent,
                            iconRes = R.drawable.ic_employees,
                            iconTint = Color.Transparent,
                            plainHeaderIcon = true,
                            trendText = tr(R.string.security_checks_analytics_trend_percent),
                            containerColor = Color.White,
                            borderColor = AnalyticsCardBorder,
                            textStyleLabel = LabelTextStyle13Regular8E8E93,
                            textStyleValue = ValueTextStyle36BoldBlack,
                            textStyleCaption = CaptionTextStyle13Regular9C9CA1,
                        )
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(IntrinsicSize.Max),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        summaryCards.forEach { card ->
                            TopSummaryCard(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight(),
                                data = card,
                            )
                        }
                    }
                }
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        border = BorderStroke(1.dp, AnalyticsCardBorder),
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.Start,
                        ) {
                            Text(
                                text = tr(R.string.security_checks_analytics_current_status),
                                style = AnalyticsCurrentStatusTitleStyle
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = tr(R.string.security_checks_analytics_breakdown_by_submission_status),
                                style = LabelTextStyle13Regular8E8E93
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(280.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                DonutChartTriple(
                                    modifier = Modifier.size(280.dp),
                                    fractions = SecurityChecksStatusDonutDummy.fractions,
                                    colors = StatusDonutSegmentColors,
                                    strokeWidth = 26.dp
                                )
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = SecurityChecksStatusDonutDummy.total.toString(),
                                        style = ValueTextStyle32BoldBlack,
                                        textAlign = TextAlign.Center
                                    )
                                    Text(
                                        text = tr(R.string.security_checks_analytics_total_users_center),
                                        style = LabelTextStyle13Regular8E8E93,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            AnalyticsStatusLegendGrid(
                                entries = listOf(
                                    LegendEntry(tr(R.string.dashboard_mgmt_legend_submitted), SecurityChecksStatusDonutDummy.submitted, StatusDonutSegmentColors[0]),
                                    LegendEntry(tr(R.string.dashboard_mgmt_legend_pending), SecurityChecksStatusDonutDummy.pending, StatusDonutSegmentColors[1]),
                                    LegendEntry(tr(R.string.dashboard_security_rejected), SecurityChecksStatusDonutDummy.rejected, StatusDonutSegmentColors[2]),
                                    LegendEntry(tr(R.string.dashboard_mgmt_legend_not_submitted), SecurityChecksStatusDonutDummy.notSubmitted, StatusDonutSegmentColors[3]),
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AnalyticsStatusLegendGrid(
    entries: List<LegendEntry>,
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        entries.chunked(2).forEach { rowEntries ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                rowEntries.forEach { entry ->
                    AnalyticsStatusLegendTile(
                        modifier = Modifier.weight(1f),
                        entry = entry,
                    )
                }
                if (rowEntries.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun AnalyticsStatusLegendTile(
    entry: LegendEntry,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .background(entry.color, CircleShape)
        )
        Column {
            Text(text = entry.label, style = LabelTextStyle13Regular8E8E93)
            Text(text = entry.value.toString(), style = AnalyticsLegendValueStyle)
        }
    }
}