package com.example.ihrm.ui.stats

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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ihrm.R
import com.example.ihrm.ui.common.DonutChartTriple
import com.example.ihrm.ui.common.TopSummaryCard
import com.example.ihrm.ui.common.header.BaseHeader
import com.example.ihrm.ui.theme.CardBorder
import com.example.ihrm.ui.theme.DashboardFigmaInk
import com.example.ihrm.ui.theme.DashboardFigmaMuted
import com.example.ihrm.ui.theme.IHRMTheme
import com.example.ihrm.ui.theme.InterFontFamily
import com.example.ihrm.util.CaptionTextStyle12NormalGrey
import com.example.ihrm.util.DashboardBrush
import com.example.ihrm.util.LabelTextStyle13MediumGrey
import com.example.ihrm.util.ValueTextStyle32BoldBlack
import com.example.ihrm.ui.localization.tr

/** Static snapshot values aligned with [Figma 871:34408](https://www.figma.com/design/Q9qmml4Qj4FD73VnYshBsO/HRM-Mobile-App-Ver?node-id=871-34408). */
private object TeamStatisticsStatic {
    const val totalCompanyEmployees = 445
    const val activeNow = 432
    val departmentCounts = listOf(10, 32, 3, 5, 15)
    val statusCounts = listOf(32, 2, 3, 12, 16)
}

private val CardBorder = Color(0xFFF0F0F0)
private val StatIconBgBlue = Color(0xFFDEEBFF)
private val StatIconBgGreen = Color(0xFFE3FCEF)
private val StatIconTintBlue = Color(0xFF0747A6)
private val StatIconTintGreen = Color(0xFF36B37E)

private val DeptColors = listOf(
    Color(0xFF0747A6),
    Color(0xFF2684FF),
    Color(0xFF4C9AFF),
    Color(0xFF889CFF),
    Color(0xFFDEEBFF)
)

private val StatusColors = listOf(
    Color(0xFF36B37E),
    Color(0xFF6E83FF),
    Color(0xFF0747A6),
    Color(0xFF2684FF),
    Color(0xFFDEEBFF)
)

@Composable
fun TeamStatisticsScreen(
    onBackClick: () -> Unit
) {
    val deptLabels = listOf(
        tr(R.string.team_statistics_dept_gdc),
        tr(R.string.team_statistics_dept_bank_biz),
        tr(R.string.team_statistics_dept_operation),
        tr(R.string.team_statistics_dept_it),
        tr(R.string.team_statistics_dept_other)
    )
    val statusLabels = listOf(
        tr(R.string.team_statistics_status_working),
        tr(R.string.team_statistics_status_resigned),
        tr(R.string.team_statistics_status_suspended),
        tr(R.string.team_statistics_status_probation),
        tr(R.string.team_statistics_status_intern)
    )

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
                        title = tr(R.string.team_statistics_title),
                        showNavigationIcon = true,
                        onNavigationClick = onBackClick,
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
                            label = tr(R.string.dashboard_total_employees),
                            value = TeamStatisticsStatic.totalCompanyEmployees.toString(),
                            caption = tr(R.string.team_statistics_active_employees_hint),
                            iconBackground = StatIconBgBlue,
                            iconRes = R.drawable.ic_list_person,
                            iconTint = StatIconTintBlue
                        ),
                        SummaryCardData(
                            label = tr(R.string.team_statistics_active_now),
                            value = TeamStatisticsStatic.activeNow.toString(),
                            caption = tr(R.string.team_statistics_working_today),
                            iconBackground = StatIconBgGreen,
                            iconRes = R.drawable.icon_person_check,
                            iconTint = StatIconTintGreen
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
                                data = card
                            )
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(16.dp)) }
                item {
                    DistributionDonutCard(
                        title = tr(R.string.team_statistics_dept_title),
                        subtitle = tr(R.string.team_statistics_dept_subtitle),
                        centerTop = tr(R.string.team_statistics_center_total),
                        centerBottom = tr(R.string.team_statistics_center_employees),
                        counts = TeamStatisticsStatic.departmentCounts,
                        colors = DeptColors,
                        legendLabels = deptLabels
                    )
                }
                item { Spacer(modifier = Modifier.height(16.dp)) }
                item {
                    DistributionDonutCard(
                        title = tr(R.string.team_statistics_status_title),
                        subtitle = tr(R.string.team_statistics_status_subtitle),
                        centerTop = tr(R.string.team_statistics_center_total),
                        centerBottom = tr(R.string.team_statistics_center_status_label),
                        counts = TeamStatisticsStatic.statusCounts,
                        colors = StatusColors,
                        legendLabels = statusLabels
                    )
                }
                item { Spacer(modifier = Modifier.height(24.dp)) }
            }
        }
    }
}


@Composable
private fun TopSummaryCard(modifier: Modifier = Modifier, data: SummaryCardData) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, CardBorder)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(17.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = data.label, style = LabelTextStyle13MediumGrey)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = data.value, style = ValueTextStyle32BoldBlack)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = data.caption, style = CaptionTextStyle12NormalGrey)
            }
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(data.iconBackground),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(data.iconRes),
                    contentDescription = null,
                    tint = data.iconTint,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}


@Composable
private fun DistributionDonutCard(
    title: String,
    subtitle: String,
    centerTop: String,
    centerBottom: String,
    counts: List<Int>,
    colors: List<Color>,
    legendLabels: List<String>
) {
    val total = counts.sum()
    val fractions = counts.map { it.toFloat() }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, CardBorder)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                text = title,
                style = TextStyle(
                    fontFamily = InterFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = DashboardFigmaInk,
                    letterSpacing = (-0.3125).sp
                )
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = subtitle,
                style = TextStyle(
                    fontFamily = InterFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp,
                    color = DashboardFigmaMuted
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp),
                contentAlignment = Alignment.Center
            ) {
                DonutChartTriple(
                    modifier = Modifier.size(220.dp),
                    fractions = fractions,
                    colors = colors,
                    strokeWidth = 18.dp
                )
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = centerTop,
                        style = TextStyle(
                            fontFamily = InterFontFamily,
                            fontWeight = FontWeight.Medium,
                            fontSize = 11.sp,
                            color = DashboardFigmaMuted,
                            textAlign = TextAlign.Center
                        )
                    )
                    Text(
                        text = total.toString(),
                        style = TextStyle(
                            fontFamily = InterFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp,
                            color = DashboardFigmaInk,
                            textAlign = TextAlign.Center
                        )
                    )
                    Text(
                        text = centerBottom,
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
            Spacer(modifier = Modifier.height(16.dp))
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                counts.forEachIndexed { i, count ->
                    LegendRow(
                        label = legendLabels[i],
                        count = count,
                        dotColor = colors[i]
                    )
                }
            }
        }
    }
}

@Composable
private fun LegendRow(
    label: String,
    count: Int,
    dotColor: Color
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
                    .size(12.dp)
                    .clip(CircleShape)
                    .background(dotColor)
            )
            Text(
                text = label,
                style = TextStyle(
                    fontFamily = InterFontFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 13.sp,
                    color = DashboardFigmaInk,
                    letterSpacing = (-0.0762).sp
                )
            )
        }
        Text(
            text = count.toString(),
            style = TextStyle(
                fontFamily = InterFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 13.sp,
                color = DashboardFigmaInk,
                letterSpacing = (-0.0762).sp
            )
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun TeamStatisticsScreenPreview() {
    IHRMTheme {
        TeamStatisticsScreen(onBackClick = {})
    }
}
