package com.example.ihrm.ui.calendar.management

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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ihrm.R
import com.example.ihrm.ui.common.header.BaseHeader
import com.example.ihrm.ui.components.ButtonSize
import com.example.ihrm.ui.components.ButtonVariant
import com.example.ihrm.ui.components.CustomButton
import com.example.ihrm.ui.theme.InterFontFamily
import com.example.ihrm.ui.theme.Neutral500
import com.example.ihrm.ui.theme.Neutral700
import com.example.ihrm.ui.theme.Primary500
import com.example.ihrm.util.DashboardBrush
import com.example.ihrm.util.singleClick
import com.example.ihrm.util.txtInterBold24
import com.example.ihrm.util.txtInterMedium14
import com.example.ihrm.util.txtInterRegular14
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Locale

private val StatYellow = Color(0xFFF0B100)
private val StatBlue = Color(0xFF2B7FFF)
private val StatOrange = Color(0xFFFF6900)
private val StatGreen = Color(0xFF00C950)
private val CardBorder = Color(0xFFF3F4F6)
private val MutedCellBg = Color(0xFFF9FAFB)
private val FilterRowBg = Color(0xFFF9FAFB)
private val ApprovedBg = Color(0xFFDCFCE7)
private val ApprovedText = Color(0xFF008236)
private val PendingBg = Color(0xFFFFEDD4)
private val PendingText = Color(0xFFCA3500)

private data class DemoLeaveRow(
    val initials: String,
    val nameRes: Int,
    val detailRes: Int,
    val approved: Boolean
)

@Composable
fun CalendarManagementScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var yearMonth by remember { mutableStateOf(YearMonth.of(2026, 3)) }
    var selectedDay by remember { mutableIntStateOf(11) }
    var showFilterPopup by remember { mutableStateOf(false) }
    var appliedLeaveFilters by remember { mutableStateOf(LeaveFilterType.All) }
    var pendingLeaveFilters by remember { mutableStateOf(LeaveFilterType.All) }

    val leaveTypesByDay = remember(yearMonth) {
        if (yearMonth == YearMonth.of(2026, 3)) {
            CalendarManagementDemoData.march2026LeaveTypesByDay
        } else {
            emptyMap()
        }
    }

    val filteredDotMap = remember(yearMonth, appliedLeaveFilters, leaveTypesByDay) {
        buildFilteredDotMapByDay(leaveTypesByDay, appliedLeaveFilters)
    }

    val legendLeaveTypes = remember(appliedLeaveFilters) {
        LeaveFilterType.entries.filter { it in appliedLeaveFilters }
    }

    val filterHintText = when {
        appliedLeaveFilters.isEmpty() ->
            stringResource(R.string.calendar_mgmt_filter_calendar_hint_empty)

        appliedLeaveFilters.size < LeaveFilterType.entries.size ->
            stringResource(
                R.string.calendar_mgmt_filter_calendar_hint_partial,
                appliedLeaveFilters.size,
                LeaveFilterType.entries.size
            )

        else -> null
    }

    val monthTitle = remember(yearMonth) {
        val formatter = DateTimeFormatter.ofPattern("MMMM yyyy", Locale.US)
        yearMonth.atDay(1).format(formatter)
    }

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
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    BaseHeader(
                        modifier = Modifier,
                        title = stringResource(R.string.calendar_mgmt_title),
                        showNavigationIcon = true,
                        onNavigationClick = onBackClick.singleClick(),
                        containerColor = Color.Transparent
                    )
                }
                item { Spacer(modifier = Modifier.height(24.dp)) }

                item {
                    StatsGrid(
                        statValue = CalendarManagementDemoData.DEMO_STAT_VALUE
                    )
                }
                item { Spacer(modifier = Modifier.height(12.dp)) }
                item {
                    CalendarCard(
                        monthTitle = monthTitle,
                        onPrevMonth = {
                            yearMonth = yearMonth.minusMonths(1)
                            selectedDay = 1
                        },
                        onNextMonth = {
                            yearMonth = yearMonth.plusMonths(1)
                            selectedDay = 1
                        },
                        yearMonth = yearMonth,
                        selectedDay = selectedDay,
                        onSelectDay = { selectedDay = it },
                        onFilterClick = {
                            pendingLeaveFilters = appliedLeaveFilters
                            showFilterPopup = true
                        },
                        dotArgbByDay = filteredDotMap,
                        filterHintText = filterHintText,
                        legendLeaveTypes = legendLeaveTypes
                    )
                }

                item {
                    UpcomingLeavesCard()
                }

                item { Spacer(modifier = Modifier.height(24.dp)) }
            }

            if (showFilterPopup) {
                FilterLeaveTypePopup(
                    pendingSelection = pendingLeaveFilters,
                    onPendingChange = { pendingLeaveFilters = it },
                    onDismiss = { showFilterPopup = false },
                    onApply = {
                        appliedLeaveFilters = pendingLeaveFilters
                        showFilterPopup = false
                    }
                )
            }
        }
    }
}

@Composable
private fun StatsGrid(statValue: Int) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatMiniCard(
                modifier = Modifier.weight(1f),
                value = statValue,
                label = stringResource(R.string.calendar_mgmt_stat_leaves_today),
                valueColor = StatYellow,
                circleColor = StatYellow,
                icon = {
                    Icon(
                        Icons.Filled.Person,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            )
            StatMiniCard(
                modifier = Modifier.weight(1f),
                value = statValue,
                label = stringResource(R.string.calendar_mgmt_stat_total_requested),
                valueColor = StatBlue,
                circleColor = StatBlue,
                icon = {
                    Icon(
                        Icons.Filled.DateRange,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatMiniCard(
                modifier = Modifier.weight(1f),
                value = statValue,
                label = stringResource(R.string.calendar_mgmt_stat_pending),
                valueColor = StatOrange,
                circleColor = StatOrange,
                icon = {
                    Icon(
                        ImageVector.vectorResource(R.drawable.ic_pending),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            )
            StatMiniCard(
                modifier = Modifier.weight(1f),
                value = statValue,
                label = stringResource(R.string.calendar_mgmt_stat_approved),
                valueColor = StatGreen,
                circleColor = StatGreen,
                icon = {
                    Icon(
                        Icons.Filled.CheckCircle,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            )
        }
    }
}

@Composable
private fun StatMiniCard(
    modifier: Modifier = Modifier,
    value: Int,
    label: String,
    valueColor: Color,
    circleColor: Color,
    icon: @Composable () -> Unit
) {
    Card(
        modifier = modifier.height(155.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(12.dp)
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(circleColor),
                contentAlignment = Alignment.Center
            ) {
                icon()
            }
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
            ) {
                Text(
                    text = value.toString(),
                    fontFamily = InterFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 40.sp,
                    color = valueColor,
                    letterSpacing = 0.38.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = label,
                    fontFamily = InterFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 11.sp,
                    lineHeight = 13.75.sp,
                    color = Neutral500
                )
            }
        }
    }
}

@Composable
private fun CalendarCard(
    monthTitle: String,
    yearMonth: YearMonth,
    selectedDay: Int,
    onSelectDay: (Int) -> Unit,
    onPrevMonth: () -> Unit,
    onNextMonth: () -> Unit,
    onFilterClick: () -> Unit,
    dotArgbByDay: Map<Int, List<Long>>,
    filterHintText: String?,
    legendLeaveTypes: List<LeaveFilterType>
) {
    val grid = remember(yearMonth, selectedDay, dotArgbByDay) {
        buildCalendarGrid42(
            year = yearMonth.year,
            month = yearMonth.monthValue,
            selectedDayOfMonth = selectedDay,
            dotArgbByDay = dotArgbByDay
        )
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, CardBorder)
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = onPrevMonth.singleClick()) {
                    Icon(
                        Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = stringResource(R.string.calendar_mgmt_cd_prev_month),
                        tint = Neutral700
                    )
                }
                Text(
                    text = monthTitle,
                    fontFamily = InterFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 17.sp,
                    color = Neutral700,
                    letterSpacing = (-0.43).sp
                )
                IconButton(onClick = onNextMonth.singleClick()) {
                    Icon(
                        Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = stringResource(R.string.calendar_mgmt_cd_next_month),
                        tint = Neutral700
                    )
                }
            }

            Column(
                modifier = Modifier
                    .border(1.dp, CardBorder)
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(FilterRowBg)
                        .clickable(onClick = onFilterClick.singleClick())
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.List,
                            contentDescription = null,
                            tint = Neutral700,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = stringResource(R.string.calendar_mgmt_filter_leave_types),
                            fontFamily = InterFontFamily,
                            fontWeight = FontWeight.Medium,
                            fontSize = 15.sp,
                            color = Neutral700
                        )
                    }
                    Icon(
                        Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = null,
                        tint = Neutral500,
                        modifier = Modifier.size(18.dp)
                    )
                }
                if (filterHintText != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = filterHintText,
                        fontFamily = InterFontFamily,
                        fontWeight = FontWeight.Normal,
                        fontSize = 11.sp,
                        lineHeight = 14.sp,
                        color = Neutral500,
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )
                }
            }

            Column(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (legendLeaveTypes.isNotEmpty()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        legendLeaveTypes.forEach { type ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .clip(CircleShape)
                                        .background(Color((type.dotArgb and 0xFFFFFFFFL).toInt()))
                                )
                                Text(
                                    text = stringResource(type.labelRes),
                                    fontFamily = InterFontFamily,
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 10.sp,
                                    color = Neutral500,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    val labels = listOf(
                        R.string.calendar_weekday_sun,
                        R.string.calendar_weekday_mon,
                        R.string.calendar_weekday_tue,
                        R.string.calendar_weekday_wed,
                        R.string.calendar_weekday_thu,
                        R.string.calendar_weekday_fri,
                        R.string.calendar_weekday_sat,
                    )
                    labels.forEach { id ->
                        Text(
                            modifier = Modifier.weight(1f),
                            text = stringResource(id),
                            fontFamily = InterFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 11.sp,
                            color = Neutral500,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                grid.chunked(7).forEach { week ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        week.forEach { slot ->
                            Box(
                                modifier = Modifier.weight(1f),
                                contentAlignment = Alignment.Center
                            ) {
                                when (slot) {
                                    CalendarGridSlot.Pad -> Spacer(modifier = Modifier.size(40.dp))
                                    is CalendarGridSlot.Day -> CalendarDayCell(
                                        slot = slot,
                                        onClick = { onSelectDay(slot.dayOfMonth) }.singleClick()
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CalendarDayCell(
    slot: CalendarGridSlot.Day,
    onClick: () -> Unit
) {
    val bg = when {
        slot.selected -> StatBlue
        slot.mutedBackground -> MutedCellBg
        else -> Color.Transparent
    }
    val textColor = if (slot.selected) Color.White else Neutral700

    Column(
        modifier = Modifier
            .size(width = 44.dp, height = 44.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(bg)
            .clickable(onClick = onClick.singleClick()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = slot.dayOfMonth.toString(),
            fontFamily = InterFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 13.sp,
            color = textColor
        )
        if (slot.dotArgb.isNotEmpty() && !slot.selected) {
            Spacer(modifier = Modifier.height(2.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                slot.dotArgb.take(3).forEach { argb ->
                    Box(
                        modifier = Modifier
                            .size(4.dp)
                            .clip(CircleShape)
                            .background(Color((argb or 0xFF000000L).toInt()))
                    )
                }
            }
        }
    }
}

@Composable
private fun UpcomingLeavesCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            Text(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                text = stringResource(R.string.calendar_mgmt_upcoming_leaves),
                fontFamily = InterFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 17.sp,
                color = Neutral700,
                letterSpacing = (-0.43).sp
            )
            HorizontalDivider(color = CardBorder)

            val rows = listOf(
                DemoLeaveRow(
                    "SJ",
                    R.string.calendar_mgmt_demo_sarah_name,
                    R.string.calendar_mgmt_demo_sarah_mar2,
                    true
                ),
                DemoLeaveRow(
                    "CB",
                    R.string.calendar_mgmt_demo_chris_name,
                    R.string.calendar_mgmt_demo_chris_dates,
                    true
                ),
                DemoLeaveRow(
                    "RT",
                    R.string.calendar_mgmt_demo_ricardo_name,
                    R.string.calendar_mgmt_demo_ricardo_dates,
                    true
                ),
                DemoLeaveRow(
                    "SM",
                    R.string.calendar_mgmt_demo_sophia_name,
                    R.string.calendar_mgmt_demo_sophia_dates,
                    true
                ),
                DemoLeaveRow(
                    "SJ",
                    R.string.calendar_mgmt_demo_sarah_name,
                    R.string.calendar_mgmt_demo_sarah_sick_detail,
                    false
                ),
            )

            rows.forEachIndexed { index, row ->
                UpcomingLeaveRow(
                    initials = row.initials,
                    name = stringResource(row.nameRes),
                    detail = stringResource(row.detailRes),
                    approved = row.approved
                )
                if (index < rows.lastIndex) {
                    HorizontalDivider(
                        color = CardBorder,
                        modifier = Modifier.padding(start = 68.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun UpcomingLeaveRow(
    initials: String,
    name: String,
    detail: String,
    approved: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        val bg = Color(CalendarManagementDemoData.avatarColorArgb(initials))
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(bg),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = initials,
                fontFamily = InterFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp,
                color = Color.White
            )
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = name,
                fontFamily = InterFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 15.sp,
                color = Neutral700,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = detail,
                fontFamily = InterFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 13.sp,
                color = Neutral500,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
        val statusLabel = if (approved) {
            stringResource(R.string.calendar_mgmt_status_approved)
        } else {
            stringResource(R.string.calendar_mgmt_status_pending)
        }
        val statusBg = if (approved) ApprovedBg else PendingBg
        val statusFg = if (approved) ApprovedText else PendingText
        Text(
            modifier = Modifier
                .clip(RoundedCornerShape(50))
                .background(statusBg)
                .padding(horizontal = 10.dp, vertical = 8.dp),
            text = statusLabel,
            style = MaterialTheme.typography.labelSmall.copy(
                fontFamily = InterFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 11.sp,
                letterSpacing = 0.5.sp,
                color = statusFg
            ),
            maxLines = 1
        )
    }
}

@Composable
private fun FilterLeaveTypePopup(
    pendingSelection: Set<LeaveFilterType>,
    onPendingChange: (Set<LeaveFilterType>) -> Unit,
    onDismiss: () -> Unit,
    onApply: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.linearGradient(
                                colors = listOf(StatBlue, Primary500)
                            )
                        )
                        .padding(start = 24.dp, end = 24.dp, top = 20.dp, bottom = 24.dp)
                ) {
                    Column(
                        modifier = Modifier.align(Alignment.CenterStart)
                    ) {
                        Text(
                            text = stringResource(R.string.calendar_mgmt_filter_popup_title),
                            style = txtInterBold24
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = stringResource(R.string.calendar_mgmt_filter_popup_subtitle),
                            style = txtInterRegular14
                        )
                    }
                    IconButton(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.2f)),
                        onClick = onDismiss
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = stringResource(R.string.calendar_mgmt_close_popup),
                            tint = Color.White
                        )
                    }
                }

                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilterPopupActionButton(
                            modifier = Modifier.weight(1f),
                            text = stringResource(R.string.calendar_mgmt_select_all),
                            background = Color(0xFFE6F1FF),
                            textColor = StatBlue,
                            onClick = { onPendingChange(LeaveFilterType.All) }
                        )
                        FilterPopupActionButton(
                            modifier = Modifier.weight(1f),
                            text = stringResource(R.string.calendar_mgmt_clear_all),
                            background = Color(0xFFF3F4F6),
                            textColor = Neutral500,
                            onClick = { onPendingChange(emptySet()) }
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    LeaveFilterType.entries.forEach { type ->
                        FilterPopupCheckboxItem(
                            label = stringResource(type.labelRes),
                            checked = pendingSelection.contains(type),
                            onCheckedChange = { checked ->
                                onPendingChange(
                                    if (checked) pendingSelection + type else pendingSelection - type
                                )
                            },
                            colorBackgroundCheckBox = Color((type.accentArgb and 0xFFFFFFFFL).toInt()),
                            colorBackground = Color((type.softRowBackgroundArgb and 0xFFFFFFFFL).toInt())
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }


                CustomButton(
                    text = stringResource(R.string.calendar_mgmt_apply_filters),
                    onClick = onApply,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .padding(top = 12.dp, bottom = 24.dp),
                    size = ButtonSize.Large,
                    variant = ButtonVariant.Secondary
                )
            }
        }
    }
}

@Composable
private fun FilterPopupActionButton(
    text: String,
    background: Color,
    textColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(36.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(background)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = txtInterMedium14,
            color = textColor
        )
    }
}

@Composable
private fun FilterPopupCheckboxItem(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    colorBackgroundCheckBox: Color,
    colorBackground: Color
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(if (checked) colorBackground else Color(0xFFF3F4F6))
            .border(
                width = 1.dp,
                color = if (checked) colorBackgroundCheckBox else Color(0xFFE5E7EB),
                shape = RoundedCornerShape(16.dp)
            )
            .clickable { onCheckedChange(!checked) }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(RoundedCornerShape(10.dp))
                .border(
                    1.dp,
                    if (checked) colorBackgroundCheckBox else Color(0xFFD1D5DB),
                    RoundedCornerShape(10.dp)
                )
                .background(if (checked) colorBackgroundCheckBox else Color.Transparent)
        ) {
            if (checked) {
                Icon(
                    modifier = Modifier
                        .size(12.dp)
                        .align(Alignment.Center),
                    imageVector = Icons.Filled.Check,
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }
        Box(
            modifier = Modifier
                .size(12.dp)
                .clip(CircleShape)
                .background(colorBackgroundCheckBox)
        )
        Text(
            text = label,
            fontFamily = InterFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            color = Neutral500,
            letterSpacing = (-0.31).sp
        )
    }
}
