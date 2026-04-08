package com.example.ihrm.ui.security.checks

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.ihrm.R
import com.example.ihrm.ui.common.BaseHRMCompose
import com.example.ihrm.ui.common.bottomsheet.SecurityChecksFilterBottomSheet
import com.example.ihrm.ui.common.header.BaseHeader
import com.example.ihrm.ui.theme.InterFontFamily
import com.example.ihrm.util.DashboardBrush
import com.example.ihrm.util.LabelTextStyle13Regular
import com.example.ihrm.util.LabelTextStyle13RegularGrey
import com.example.ihrm.util.LabelTextStyle13RegularWhite
import com.example.ihrm.util.formatVNPhoneNumber
import com.example.ihrm.util.singleClick
import com.example.ihrm.util.txtInterMedium15
import com.example.ihrm.ui.localization.tr

@Composable
fun SecurityChecksScreen(
    onBackClick: () -> Unit,
    onSeeChartClick: () -> Unit,
    onSecurityCheckClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SecurityChecksViewModel = hiltViewModel(),
) {
    BaseHRMCompose(
        content = {
            SecurityChecksScreenContent(
                onBackClick = onBackClick,
                onSeeChartClick = onSeeChartClick,
                onSecurityCheckClick = onSecurityCheckClick,
                modifier = modifier,
                viewModel = viewModel
            )
        },
        viewmodel = viewModel,
        onErrorAlertClose = onBackClick,
    )
}

enum class SecuritySummaryFilter {
    NOT_SUBMITTED,
    APPROVED,
    SUBMITTED,
    REJECTED,
}

@Composable
fun SecurityChecksScreenContent(
    onBackClick: () -> Unit,
    onSeeChartClick: () -> Unit,
    onSecurityCheckClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SecurityChecksViewModel,
) {
    val submissionsState by viewModel.uiState.collectAsState()
    val securityGroupsState by viewModel.uiStateSecurityGroups.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    var showFilterSheet by remember { mutableStateOf(false) }
    var appliedFilters by remember { mutableStateOf(SecurityChecksActiveFilters.Default) }
    var draftFilters by remember { mutableStateOf(SecurityChecksActiveFilters.Default) }
    var selectedSummaryFilter by remember { mutableStateOf(SecuritySummaryFilter.NOT_SUBMITTED) }

    LaunchedEffect(showFilterSheet) {
        if (showFilterSheet) {
            draftFilters = appliedFilters
        }
    }

    val groupOptions = remember(securityGroupsState) {
        securityGroupsState.securityGroups
    }

    val labelApproved = tr(R.string.security_checks_approved)
    val labelSubmitted = tr(R.string.security_checks_submitted)
    val labelRejected = tr(R.string.security_checks_status_rejected)
    val labelNotSubmitted = tr(R.string.security_checks_status_not_submitted)
    val dash = tr(R.string.security_checks_dash)

//    val filteredSubmissions = remember(
//        submissionsState.submissions,
//        searchQuery,
//        appliedFilters,
//    ) {
//        submissionsState.submissions
//            .filterByDateRange(appliedFilters.dateFromMillis, appliedFilters.dateToMillis)
//            .filterByGroup(appliedFilters.groupId)
//            .filteredBySearchQuery(searchQuery, appliedFilters.searchBy)
//    }

    LaunchedEffect(selectedSummaryFilter) {
        viewModel.loadSecurityCheck(selectedSummaryFilter)
    }

    val uiItems = remember(
        submissionsState,
        labelApproved,
        labelSubmitted,
        labelRejected,
        labelNotSubmitted,
        dash,
    ) {
        Log.d("Vinh", "selectedSummaryFilter: $selectedSummaryFilter")
        if (selectedSummaryFilter == SecuritySummaryFilter.NOT_SUBMITTED) {
            submissionsState.notSubmission
                .toSecurityCheckItemUiList(
                    labelApproved = labelApproved,
                    labelSubmitted = labelSubmitted,
                    labelRejected = labelRejected,
                    labelNotSubmitted = labelNotSubmitted,
                    dash = dash,
                )
        } else {
            submissionsState.submissions
                .toSecurityCheckItemUiList(
                    labelApproved = labelApproved,
                    labelSubmitted = labelSubmitted,
                    labelRejected = labelRejected,
                    labelNotSubmitted = labelNotSubmitted,
                    dash = dash,
                )
        }
    }



    val notSubmittedCount = submissionsState.notSubmission.size
    val approvedCount = submissionsState.approvedCount
    val submittedCount = submissionsState.submittedCount
    val rejectedCount = submissionsState.rejectedCount
    val showInitialLoading = submissionsState.isLoading && submissionsState.submissions.isEmpty()
    val filterIndicatorActive = appliedFilters.hasActiveFilters()

    SecurityChecksFilterBottomSheet(
        visible = showFilterSheet,
        draft = draftFilters,
        onDraftChange = { draftFilters = it },
        groupOptions = groupOptions,
        onDismiss = { showFilterSheet = false },
        onClearAll = { draftFilters = SecurityChecksActiveFilters.Default },
        onApply = {
            appliedFilters = draftFilters
            showFilterSheet = false
        },
    )

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .navigationBarsPadding(),
        containerColor = Color.Transparent,
        contentWindowInsets = WindowInsets(top = 0),
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(brush = DashboardBrush.BaseBackground)
        ) {
            BaseHeader(
                modifier = Modifier
                    .padding(paddingValues)
                    .statusBarsPadding(),
                title = tr(R.string.drawer_item_security_checks),
                showNavigationIcon = true,
                onNavigationClick = onBackClick.singleClick(),
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
            Spacer(modifier = Modifier.height(12.dp))
            SecurityFiltersRow(
                searchQuery = searchQuery,
                onSearchQueryChange = { searchQuery = it },
                filterActive = filterIndicatorActive,
                onFilterClick = { showFilterSheet = true },
            )
            Spacer(modifier = Modifier.height(20.dp))
            Spacer(modifier = Modifier.height(20.dp))
            SecuritySummaryCards(
                total = notSubmittedCount.toString(),
                approved = approvedCount.toString(),
                submitted = submittedCount.toString(),
                rejected = rejectedCount.toString(),
                selectedFilter = selectedSummaryFilter,
                onFilterSelected = { selectedSummaryFilter = it },
            )
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                ) {
                    if (!showInitialLoading && uiItems.isEmpty() && submissionsState.errorMessage == null) {
                        item {
                            Text(
                                text = tr(R.string.security_checks_empty),
                                style = LabelTextStyle13RegularWhite,
                                modifier = Modifier.padding(vertical = 24.dp)
                            )
                        }
                    }
                    items(
                        items = uiItems,
                        key = { it.submissionId },
                    ) { check ->
                        Spacer(modifier = Modifier.height(12.dp))
                        if (selectedSummaryFilter == SecuritySummaryFilter.NOT_SUBMITTED) {
                            SecurityCheckNotSubmittedCard(item = check)
                        } else {
                            SecurityCheckCard(
                                item = check,
                                onClick = {
                                    onSecurityCheckClick(check.submissionId.toString())
                                }.singleClick()
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    item { Spacer(modifier = Modifier.height(8.dp)) }
                }
            }
        }
    }
}

@Composable
private fun SecurityFiltersRow(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    filterActive: Boolean,
    onFilterClick: () -> Unit,
) {
    val textStyle = TextStyle(
        color = Color.Black,
        fontSize = 15.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 18.sp,
        platformStyle = PlatformTextStyle(
            includeFontPadding = false
        )
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(top = 14.dp)
            .height(44.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(Color(0xFFF2F2F7))
            .padding(horizontal = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_search),
            contentDescription = null,
            tint = Color(0xFF6B7280),
            modifier = Modifier.size(16.dp)
        )
        BasicTextField(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            modifier = Modifier.weight(1f),
            singleLine = true,
            textStyle = textStyle,
            decorationBox = { innerTextField ->
                Box(
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (searchQuery.isEmpty()) {
                        Text(
                            text = tr(R.string.security_checks_year),
                            style = textStyle,
                            color = Color(0xFF9CA3AF)
                        )
                    }
                    innerTextField()
                }
            }
        )
        if (searchQuery.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF969BA4))
                    .clickable { onSearchQueryChange("") },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = null,
                    tint = White,
                    modifier = Modifier.size(10.dp)
                )
            }
        }
        VerticalDivider(
            modifier = Modifier.height(20.dp),
            thickness = 1.dp,
            color = Color(0xFFE5E7EB)
        )
        Box(modifier = Modifier.size(28.dp), contentAlignment = Alignment.Center) {
            IconButton(
                onClick = onFilterClick.singleClick(),
                modifier = Modifier.size(28.dp)
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_filter),
                    contentDescription = tr(R.string.security_checks_filter_cd),
                    tint = Color(0xFF6B7280),
                    modifier = Modifier.size(16.dp)
                )
            }
            if (filterActive) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 4.dp, end = 2.dp)
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF2563EB))
                )
            }
        }
    }
}

@Composable
private fun SecuritySummaryCards(
    total: String,
    approved: String,
    submitted: String,
    rejected: String,
    selectedFilter: SecuritySummaryFilter,
    onFilterSelected: (SecuritySummaryFilter) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(IntrinsicSize.Min),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        SecurityStatCard(
            modifier = Modifier
                .weight(1f),
            value = approved,
            label = tr(R.string.security_checks_approved),
            valueColor = Color(0xFF34C759),
            selected = selectedFilter == SecuritySummaryFilter.APPROVED,
            selectedBackgroundColor = Color(0xFFD7F6D6),
            onClick = { onFilterSelected(SecuritySummaryFilter.APPROVED) },
        )
        SecurityStatCard(
            modifier = Modifier
                .weight(1f),
            value = submitted,
            label = tr(R.string.security_checks_submitted),
            valueColor = Color(0xFF1970F3),
            selected = selectedFilter == SecuritySummaryFilter.SUBMITTED,
            selectedBackgroundColor = Color(0xFFD2E6FF),
            onClick = { onFilterSelected(SecuritySummaryFilter.SUBMITTED) },
        )
        SecurityStatCard(
            modifier = Modifier
                .weight(1f),
            value = rejected,
            label = tr(R.string.security_checks_rejected),
            valueColor = Color(0xFFFF1515),
            selectedBackgroundColor = Color(0xFFFFD5D5),
            selected = selectedFilter == SecuritySummaryFilter.REJECTED,
            onClick = { onFilterSelected(SecuritySummaryFilter.REJECTED) },
        )
        SecurityStatCard(
            modifier = Modifier
                .weight(1f),
            value = total,
            label = tr(R.string.security_checks_not_submitted),
            valueColor = Color(0xFFF0B100),
            selected = selectedFilter == SecuritySummaryFilter.NOT_SUBMITTED,
            selectedBackgroundColor = Color(0xFFF0F5D7),
            onClick = { onFilterSelected(SecuritySummaryFilter.NOT_SUBMITTED) },
        )
    }
}

@Composable
private fun SecurityStatCard(
    modifier: Modifier = Modifier,
    value: String,
    label: String,
    valueColor: Color,
    selected: Boolean,
    onClick: () -> Unit,
    selectedBackgroundColor: Color,
) {
    Surface(
        modifier = modifier
            .fillMaxHeight()
            .border(
                width = 1.dp,
                color = if (selected) selectedBackgroundColor else Color(0xFFE5E5EA),
                shape = RoundedCornerShape(14.dp)
            ),
        color = if (selected) selectedBackgroundColor else White,
        shape = RoundedCornerShape(16.dp)
    ) {
        val interactionSource = remember { MutableInteractionSource() }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxSize()
                .padding(all = 8.dp)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClick = onClick.singleClick()
                ),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = value,
                color = valueColor,
                fontFamily = InterFontFamily,
                fontSize = 31.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                maxLines = 1
            )
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = label,
                color = valueColor,
                textAlign = TextAlign.Center,
                style = LabelTextStyle13Regular
            )
        }
    }
}

private val SecurityCheckCardMuted = Color(0xFF8E8E93)
private val SecurityCheckCardBorder = Color(0xFFE5E5EA)
private val SecurityCheckCardDivider = Color(0xFFF2F2F7)
private val SecurityCheckBadgeGradient = Brush.verticalGradient(
    colors = listOf(Color(0xFF5856D6), Color(0xFF007AFF))
)

@Composable
private fun SecurityCheckCard(
    item: SecurityCheckItemUi,
    onClick: () -> Unit,
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick),
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
private fun SecurityCheckNotSubmittedCard(
    item: SecurityCheckItemUi,
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp)),
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
            HorizontalDivider(thickness = 1.dp, color = SecurityCheckCardDivider)
            CardInfo(item)
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
        TeamBadge()
        TeamInfo(
            modifier = Modifier.weight(1f),
            name = item.teamName,
            employeeId = item.employeeId
        )
        StatusChip(item)
    }
}

@Composable
private fun TeamBadge() {
    Box(
        modifier = Modifier
            .size(45.dp)
            .clip(CircleShape)
            .border(
                width = 1.dp,
                color = SecurityCheckCardBorder,
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(R.drawable.shinhan_bear),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .size(30.dp)
                .align(Alignment.Center)
        )
    }
}

@Composable
private fun TeamInfo(modifier: Modifier = Modifier, name: String, employeeId: String) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Text(
            text = name,
            color = Color.Black,
            fontFamily = InterFontFamily,
            fontSize = 17.sp,
            fontWeight = FontWeight.SemiBold,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = "ID: $employeeId",
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
        SecurityCheckStatus.APPROVED -> R.drawable.ic_approved
        SecurityCheckStatus.NOT_SUBMITTED -> R.drawable.ic_not_submitted
        SecurityCheckStatus.SUBMITTED -> R.drawable.ic_submitted
        else -> R.drawable.ic_rejected
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
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
            if (item.approvedByLabelRes != null) {
                Text(
                    text = tr(item.approvedByLabelRes),
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
            SecurityCheckStatus.REJECTED -> tr(R.string.security_checks_rejected_date)
            else -> tr(R.string.security_checks_approved_date)
        }
        DateColumn(
            modifier = Modifier.weight(1f),
            label = tr(R.string.security_checks_submitted_date),
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
private fun CardInfo(item: SecurityCheckItemUi) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        RowInfo(text = item.email, icon = R.drawable.ic_email)
        RowInfo(text = item.phoneNumber.formatVNPhoneNumber(), icon = R.drawable.ic_cellphone)
    }
}

@Composable
private fun RowInfo(text: String, icon: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(icon),
            contentDescription = null,
            colorFilter = ColorFilter.tint(Color(0xFF99A1AF)),
            modifier = Modifier.size(14.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            style = LabelTextStyle13RegularGrey
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
