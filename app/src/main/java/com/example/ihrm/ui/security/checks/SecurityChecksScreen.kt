package com.example.ihrm.ui.security.checks

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ihrm.R
import com.example.ihrm.ui.common.header.BaseHeader
import com.example.ihrm.ui.theme.InterFontFamily
import com.example.ihrm.util.DashboardBrush
import com.example.ihrm.util.LabelTextStyle13RegularGrey
import com.example.ihrm.util.LabelTextStyle13RegularWhite
import com.example.ihrm.util.txtInterBold22White
import com.example.ihrm.util.txtInterMedium15

enum class SecurityCheckStatus {
    APPROVED, SUBMITTED, REJECTED
}
private data class SecurityCheckItemUi(
    val teamIndex: Int,
    val teamName: String,
    val department: String,
    val statusLabel: String,
    val statusColor: Color,
    val statusBg: Color,
    /** When non-null, shows grey label + name; when null, single line for [approverName]. */
    @StringRes val approvedByLabelRes: Int?,
    val approverName: String,
    val submittedDate: String,
    val approvedDate: String,
    val statusUseApprovedChip: SecurityCheckStatus = SecurityCheckStatus.APPROVED
)

private val figmaApprovedGreen = Color(0xFF34C759)
private val figmaApprovedGreenBg = Color(0x1F34C759)
private val figmaRejectedGreen = Color(0xFFF44336)
private val figmaRejectedGreenBg = Color(0x21F44336)
private val figmaSubmittedBlue = Color(0xFF007AFF)
private val figmaSubmittedBlueBg = Color(0x1F007AFF)

private val demoChecks = listOf(
    SecurityCheckItemUi(
        teamIndex = 1,
        teamName = "GCD",
        department = "Department",
        statusLabel = "Approved",
        statusColor = figmaApprovedGreen,
        statusBg = figmaApprovedGreenBg,
        approvedByLabelRes = R.string.security_checks_approved_by,
        approverName = "Nguyen Van A",
        submittedDate = "01/01/2000",
        approvedDate = "01/01/2000",
        statusUseApprovedChip = SecurityCheckStatus.APPROVED
    ),
    SecurityCheckItemUi(
        teamIndex = 1,
        teamName = "GCD",
        department = "Department",
        statusLabel = "Approved",
        statusColor = figmaApprovedGreen,
        statusBg = figmaApprovedGreenBg,
        approvedByLabelRes = R.string.security_checks_approved_by,
        approverName = "Nguyen Van A",
        submittedDate = "01/01/2000",
        approvedDate = "01/01/2000",
        statusUseApprovedChip = SecurityCheckStatus.APPROVED
    ),
    SecurityCheckItemUi(
        teamIndex = 1,
        teamName = "GCD",
        department = "Department",
        statusLabel = "Rejected",
        statusColor = figmaRejectedGreen,
        statusBg = figmaRejectedGreenBg,
        approvedByLabelRes = R.string.security_checks_rejected_by,
        approverName = "Nguyen Van A",
        submittedDate = "01/01/2000",
        approvedDate = "01/01/2000",
        statusUseApprovedChip = SecurityCheckStatus.REJECTED
    ),
    SecurityCheckItemUi(
        teamIndex = 8,
        teamName = "GCD",
        department = "Department",
        statusLabel = "Submitted",
        statusColor = figmaSubmittedBlue,
        statusBg = figmaSubmittedBlueBg,
        approvedByLabelRes = R.string.security_checks_wait_for_approve_by,
        approverName = "Nguyen Van A",
        submittedDate = "01/01/2000",
        approvedDate = "-",
        statusUseApprovedChip = SecurityCheckStatus.SUBMITTED
    )
)

@Composable
fun SecurityChecksScreen(
    onBackClick: () -> Unit,
    onSeeChartClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .navigationBarsPadding(),
        containerColor = Color.Transparent,
        contentWindowInsets = WindowInsets(top = 0),
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(brush = DashboardBrush.BaseBackground)
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
                        modifier = Modifier,
                        title = stringResource(R.string.drawer_item_security_checks),
                        showNavigationIcon = true,
                        onNavigationClick = onBackClick,
                        containerColor = Color.Transparent,
                        navigationIcon = {
                            Icon(
                                modifier = Modifier.size(24.dp),
                                imageVector = Icons.Default.Menu,
                                contentDescription = null,
                                tint = White
                            )
                        }
                    )
                }
                item { Spacer(modifier = Modifier.height(12.dp)) }
                item {
                    SecurityFiltersRow()
                    Spacer(modifier = Modifier.height(20.dp))
                }
                item {
                    Spacer(modifier = Modifier.height(20.dp))
                    SecuritySummaryCards()
                }
                item {
                    Spacer(modifier = Modifier.height(12.dp))
                    SeeChartButton(onClick = onSeeChartClick)
                }
                item {
                    Spacer(modifier = Modifier.height(32.dp))
                    RecentChecksHeader()
                    Spacer(modifier = Modifier.height(12.dp))
                }
                items(demoChecks) { check ->
                    SecurityCheckCard(check)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
private fun SecurityFiltersRow() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 14.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Surface(
            modifier = Modifier
                .weight(1f)
                .height(44.dp),
            color = Color(0xFFF2F2F7),
            shape = RoundedCornerShape(14.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_my_leave),
                    contentDescription = null,
                    tint = Color(0xFF6B7280),
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = stringResource(R.string.security_checks_year),
                    color = Color.Black,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(start = 8.dp)
                )
                Box(modifier = Modifier.weight(1f))
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_down),
                    contentDescription = null,
                    tint = Color(0xFF9CA3AF),
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        Surface(
            modifier = Modifier.size(44.dp),
            color = Color(0xFFF2F2F7),
            shape = RoundedCornerShape(14.dp)
        ) {
            IconButton(onClick = { }) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_filter),
                    contentDescription = stringResource(R.string.security_checks_filter_cd),
                    tint = Color(0xFF6B7280),
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

@Composable
private fun SecuritySummaryCards() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        SecurityStatCard(
            modifier = Modifier.weight(1f),
            value = "68",
            label = stringResource(R.string.security_checks_total),
            valueColor = Color(0xFF111827)
        )
        SecurityStatCard(
            modifier = Modifier.weight(1f),
            value = "2",
            label = stringResource(R.string.security_checks_approved),
            valueColor = Color(0xFF16A34A)
        )
        SecurityStatCard(
            modifier = Modifier.weight(1f),
            value = "6",
            label = stringResource(R.string.security_checks_submitted),
            valueColor = Color(0xFF2563EB)
        )
    }
}

@Composable
private fun SecurityStatCard(
    modifier: Modifier = Modifier,
    value: String,
    label: String,
    valueColor: Color
) {
    Surface(
        modifier = modifier,
        color = Color(0xFFF3F4F6),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(all = 18.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = value,
                color = valueColor,
                fontFamily = InterFontFamily,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1
            )
            Text(
                text = label,
                style = LabelTextStyle13RegularGrey
            )
        }
    }
}

@Composable
private fun SeeChartButton(onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        color = Color(0xFFF9FAFB),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_chart),
                contentDescription = null,
                tint = Color(0xFF101828),
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(R.string.security_checks_see_chart),
                color = Color(0xFF101828),
                fontSize = 17.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun RecentChecksHeader() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.security_checks_recent_checks),
            style = txtInterBold22White
        )
        Text(
            text = stringResource(R.string.security_checks_page_info),
            style = LabelTextStyle13RegularWhite
        )
    }
}

private val SecurityCheckCardMuted = Color(0xFF8E8E93)
private val SecurityCheckCardBorder = Color(0xFFE5E5EA)
private val SecurityCheckCardDivider = Color(0xFFF2F2F7)
private val SecurityCheckBadgeGradient = Brush.verticalGradient(
    colors = listOf(Color(0xFF5856D6), Color(0xFF007AFF))
)

@Composable
private fun SecurityCheckCard(item: SecurityCheckItemUi) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = White,
        shape = RoundedCornerShape(16.dp),
        tonalElevation = 0.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, SecurityCheckCardBorder, RoundedCornerShape(16.dp))
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            CardHeader(item)
            CardApprovedBy(item)
            HorizontalDivider(thickness = 1.dp, color = SecurityCheckCardDivider)
            CardDates(item)
        }
    }
}

@Composable
private fun CardHeader(item: SecurityCheckItemUi) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        TeamBadge(index = item.teamIndex)
        TeamInfo(
            modifier = Modifier.weight(1f),
            name = item.teamName,
            department = item.department
        )
        StatusChip(item)
    }
}

@Composable
private fun TeamBadge(index: Int) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(SecurityCheckBadgeGradient),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = index.toString(),
            color = White,
            fontFamily = InterFontFamily,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun TeamInfo(modifier: Modifier = Modifier, name: String, department: String) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Text(
            text = name,
            color = Color.Black,
            fontFamily = InterFontFamily,
            fontSize = 17.sp,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = department,
            color = SecurityCheckCardMuted,
            fontFamily = InterFontFamily,
            fontSize = 13.sp,
            fontWeight = FontWeight.Normal,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}



@Composable
private fun StatusChip(item: SecurityCheckItemUi) {
    val icon = when (item.statusUseApprovedChip) {
        SecurityCheckStatus.APPROVED  -> R.drawable.ic_approved
        SecurityCheckStatus.SUBMITTED -> R.drawable.ic_submitted
        else ->  R.drawable.ic_rejected
    }
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(999.dp))
            .background(item.statusBg)
            .padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(icon),
            contentDescription = null,
            tint = item.statusColor,
            modifier = Modifier.size(14.dp)
        )
        Text(
            text = item.statusLabel,
            color = item.statusColor,
            fontFamily = InterFontFamily,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1
        )
    }
}

@Composable
private fun CardApprovedBy(item: SecurityCheckItemUi) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(SecurityCheckCardDivider),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(R.drawable.shinhan_logo),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
            )
        }
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
            if (item.approvedByLabelRes != null) {
                Text(
                    text = stringResource(item.approvedByLabelRes),
                    color = SecurityCheckCardMuted,
                    fontFamily = InterFontFamily,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Normal,
                    maxLines = 1
                )
            }
            Text(
                text = item.approverName,
                style = txtInterMedium15,
                maxLines = if (item.approvedByLabelRes != null) 2 else 3,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun CardDates(item: SecurityCheckItemUi) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        val label = when (item.statusUseApprovedChip) {
            SecurityCheckStatus.REJECTED  -> stringResource(R.string.security_checks_rejected_date)
            else -> stringResource(R.string.security_checks_approved_date)
        }
        DateColumn(
            modifier = Modifier.weight(1f),
            label = stringResource(R.string.security_checks_submitted_date),
            value = item.submittedDate
        )
        DateColumn(
            modifier = Modifier.weight(1f),
            label = label,
            value = item.approvedDate
        )
    }
}

@Composable
private fun DateColumn(modifier: Modifier = Modifier, label: String, value: String) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = label,
            style = LabelTextStyle13RegularGrey
        )
        Text(
            text = value,
            style = txtInterMedium15
        )
    }
}
