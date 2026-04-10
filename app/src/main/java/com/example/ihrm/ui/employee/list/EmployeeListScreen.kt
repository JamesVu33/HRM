package com.example.ihrm.ui.employee.list

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.ihrm.R
import com.example.ihrm.domain.model.EmployeeListSampleData
import com.example.ihrm.domain.model.EmployeeUiModel
import com.example.ihrm.ui.common.BaseHRMCompose
import com.example.ihrm.ui.common.header.BaseHeader
import com.example.ihrm.ui.components.CustomButton
import com.example.ihrm.ui.components.EmployeeCard
import com.example.ihrm.ui.theme.EdittextBg
import com.example.ihrm.ui.theme.EdittextBg_50
import com.example.ihrm.ui.theme.EdittextBg_70
import com.example.ihrm.ui.theme.Error
import com.example.ihrm.ui.theme.FABGradientEnd
import com.example.ihrm.ui.theme.FABGradientStart
import com.example.ihrm.ui.theme.IHRMTheme
import com.example.ihrm.ui.theme.Neutral200
import com.example.ihrm.ui.theme.Neutral500
import com.example.ihrm.ui.theme.Neutral600
import com.example.ihrm.ui.theme.Neutral700
import com.example.ihrm.ui.theme.Primary400
import com.example.ihrm.ui.theme.Primary50
import com.example.ihrm.ui.theme.StrokeColor
import com.example.ihrm.ui.theme.SuccessGreen
import com.example.ihrm.ui.theme.SurfaceBorder
import com.example.ihrm.util.DashboardBrush
import com.example.ihrm.ui.localization.tr

@Composable
fun EmployeeListScreen(
    onEmployeeClick: (String) -> Unit,
    onAddEmployeeClick: () -> Unit,
    onBackClick: (() -> Unit)? = null,
    onViewStats: () -> Unit = {},
    viewModel: EmployeeListViewModel = hiltViewModel()
) {
    BaseHRMCompose(
        content = {
            EmployeeListScreenContent(
                onEmployeeClick = onEmployeeClick,
                onAddEmployeeClick = onAddEmployeeClick,
                onBackClick = onBackClick,
                onViewStats = onViewStats,
                viewModel = viewModel
            )
        },
        viewmodel = viewModel,
        onErrorAlertClose = onBackClick
    )
}

@Composable
fun EmployeeListScreenContent(
    onEmployeeClick: (String) -> Unit,
    onAddEmployeeClick: () -> Unit,
    onBackClick: (() -> Unit)? = null,
    onViewStats: () -> Unit = {},
    viewModel: EmployeeListViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    var searchQuery by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.refreshEmployees()
    }

    val baseModels = remember(uiState.employeeUiModels, uiState.isLoading, uiState.error) {
        when {
            uiState.employeeUiModels.isNotEmpty() -> uiState.employeeUiModels
            uiState.isLoading -> emptyList()
            uiState.error != null -> emptyList()
            else -> EmployeeListSampleData.uiModels
        }
    }

    val filteredModels = remember(baseModels, searchQuery) {
        val q = searchQuery.trim().lowercase()
        if (q.isEmpty()) baseModels
        else {
            baseModels.filter { m ->
                val e = m.employee
                e.name.lowercase().contains(q) || e.position.lowercase()
                    .contains(q) || e.email.lowercase().contains(q)
            }
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding(),
        containerColor = Color.Transparent,
        contentWindowInsets = WindowInsets(top = 0),
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddEmployeeClick,
                modifier = Modifier
                    .size(56.dp)
                    .shadow(8.dp, CircleShape),
                shape = CircleShape,
                containerColor = Color.Transparent,
                contentColor = White,
                elevation = FloatingActionButtonDefaults.elevation(8.dp, 8.dp),
                content = {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                brush = Brush.linearGradient(
                                    colors = listOf(FABGradientStart, FABGradientEnd)
                                ),
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = tr(R.string.dashboard_fab_add)
                        )
                    }
                }
            )
        },
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(brush = DashboardBrush.BaseBackground)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {

                if (uiState.error != null) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Error.copy(alpha = 0.15f))
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = uiState.error!!,
                            modifier = Modifier.weight(1f),
                            fontSize = 13.sp,
                            color = Error
                        )
                        TextButton(onClick = { viewModel.clearError() }) {
                            Text(
                                text = tr(R.string.dashboard_error_dismiss),
                                color = Primary400,
                                fontSize = 13.sp
                            )
                        }
                    }
                }

                when {

                    uiState.error != null && baseModels.isEmpty() -> {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = tr(R.string.employee_list_load_error),
                                color = Neutral600,
                                fontSize = 15.sp
                            )
                        }
                    }

                    else -> {
                        EmployeeListScrollContent(
                            baseModels = baseModels,
                            filteredModels = filteredModels,
                            searchQuery = searchQuery,
                            onSearchQueryChange = { searchQuery = it },
                            onViewStats = onViewStats,
                            onEmployeeClick = onEmployeeClick,
                            onBackClick = onBackClick,
                            paddingValues = paddingValues
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun EmployeeListScrollContent(
    baseModels: List<EmployeeUiModel>,
    filteredModels: List<EmployeeUiModel>,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onViewStats: () -> Unit,
    onEmployeeClick: (String) -> Unit,
    onBackClick: (() -> Unit)? = null,
    paddingValues: PaddingValues = PaddingValues()
) {
    val totalEmployees = baseModels.size
    val activeToday = derivedActiveToday(baseModels)
    Column(modifier = Modifier.fillMaxSize().statusBarsPadding()) {
        BaseHeader(
            modifier = Modifier,
            title = tr(R.string.employee_list_title),
            showNavigationIcon = onBackClick != null,
            onNavigationClick = onBackClick,
            navigationIcon = {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                    tint = White
                )
            },
            containerColor = Color.Transparent
        )
        Spacer(modifier = Modifier.height(12.dp))
        EmployeeListSearchBar(
            query = searchQuery,
            onQueryChange = onSearchQueryChange,
            placeholder = tr(R.string.dashboard_search_placeholder)
        )
        Spacer(modifier = Modifier.height(16.dp))
        ManageTeamCard(onViewStats = onViewStats)
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(
                bottom = paddingValues.calculateBottomPadding()
            ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                StatsRow(
                    totalEmployees = totalEmployees,
                    activeToday = activeToday
                )
            }
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = tr(R.string.dashboard_team_members),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Neutral700
                )
            }
            item { Spacer(modifier = Modifier.height(12.dp)) }
            if (filteredModels.isEmpty()) {
                item {
                    Text(
                        text = tr(R.string.employee_list_no_match),
                        fontSize = 14.sp,
                        color = Neutral600,
                        modifier = Modifier.padding(vertical = 24.dp)
                    )
                }
            } else {
                items(
                    items = filteredModels,
                    key = { it.employee.id }
                ) { model ->
                    EmployeeCard(
                        employee = model.employee,
                        levelCode = model.employee.level,
                        onViewDetails = { onEmployeeClick(model.employee.id) },
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
            item { Spacer(modifier = Modifier.height(80.dp)) }
        }
    }
}

/** Tỷ lệ gần với mẫu Figma (68 tổng / 57 active). */
private fun derivedActiveToday(total: List<EmployeeUiModel>): Int {
    return total.count {
        it.employee.statusWorking.uppercase() == "WORKING"
    }
}

private val DeleteRed = Color(0xFFdc2626)
private val DeleteIconBg = Color(0xFFfee2e2)

@Composable
private fun DeleteConfirmDialog(
    employeeName: String,
    onDismiss: () -> Unit,
    onConfirmDelete: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(SurfaceBorder)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = null,
                            tint = Neutral600,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(DeleteIconBg),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = null,
                        tint = DeleteRed,
                        modifier = Modifier.size(32.dp)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = tr(R.string.dashboard_delete_dialog_title),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF101828)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = tr(R.string.dashboard_delete_dialog_message, employeeName),
                    fontSize = 14.sp,
                    color = Neutral500,
                    lineHeight = 20.sp
                )
                Spacer(modifier = Modifier.height(24.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SurfaceBorder,
                            contentColor = Neutral600
                        ),
                        elevation = ButtonDefaults.buttonElevation(0.dp)
                    ) {
                        Text(
                            text = tr(R.string.dashboard_delete_dialog_cancel),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    Button(
                        onClick = onConfirmDelete,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = DeleteRed,
                            contentColor = White
                        ),
                        elevation = ButtonDefaults.buttonElevation(4.dp)
                    ) {
                        Text(
                            text = tr(R.string.dashboard_delete_dialog_confirm),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun EmployeeListSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    placeholder: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(44.dp)
            .border(
                width = 1.dp,
                color = StrokeColor,
                shape = RoundedCornerShape(14.dp)
            )
            .clip(RoundedCornerShape(14.dp))
            .background(EdittextBg_70)
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = White,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            BasicTextField(
                value = query,
                onValueChange = onQueryChange,
                modifier = Modifier.weight(1f),
                singleLine = true,
                textStyle = TextStyle(
                    color = White,
                    fontSize = 15.sp
                ),
                cursorBrush = SolidColor(Neutral700),
                decorationBox = { inner ->
                    if (query.isEmpty()) {
                        Text(
                            text = placeholder,
                            color = White,
                            fontSize = 15.sp
                        )
                    }
                    inner()
                }
            )
        }
    }
}

@Composable
private fun StatsRow(
    totalEmployees: Int,
    activeToday: Int
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        StatCard(
            modifier = Modifier.weight(1f),
            value = totalEmployees.toString(),
            label = tr(R.string.dashboard_total_employees),
            valueColor = Primary400
        )
        StatCard(
            modifier = Modifier.weight(1f),
            value = activeToday.toString(),
            label = tr(R.string.dashboard_active_today),
            valueColor = SuccessGreen
        )
    }
}

@Composable
private fun StatCard(
    modifier: Modifier = Modifier,
    value: String,
    label: String,
    valueColor: Color
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = value,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = valueColor
            )
            Text(
                text = label,
                fontSize = 12.sp,
                color = Neutral500
            )
        }
    }
}

@Composable
private fun ManageTeamCard(onViewStats: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            EdittextBg,
                            EdittextBg_50,
                            Primary50,
                            Primary50,
                            White
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Text(
                    text = tr(R.string.dashboard_manage_team_title),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = tr(R.string.dashboard_manage_team_subtitle),
                    fontSize = 14.sp,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(11.dp))
                CustomButton(
                    text = tr(R.string.dashboard_view_stats),
                    onClick = onViewStats,
                    modifier = Modifier,
                    isPill = true
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun EmployeeListScrollContentPreview() {
    IHRMTheme {
        Box(Modifier.fillMaxSize()) {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = { "onAddEmployeeClick" },
                        modifier = Modifier
                            .size(56.dp)
                            .shadow(8.dp, CircleShape),
                        shape = CircleShape,
                        containerColor = Color.Transparent,
                        contentColor = White,
                        elevation = FloatingActionButtonDefaults.elevation(8.dp, 8.dp),
                        content = {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        brush = Brush.linearGradient(
                                            colors = listOf(FABGradientStart, FABGradientEnd)
                                        ),
                                        shape = CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = tr(R.string.dashboard_fab_add)
                                )
                            }
                        }
                    )
                },
            ) { paddingValues ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(brush = DashboardBrush.BaseBackground)
                        .padding(paddingValues)
                ) {
                    EmployeeListScrollContent(
                        baseModels = EmployeeListSampleData.uiModels,
                        filteredModels = EmployeeListSampleData.uiModels,
                        searchQuery = "",
                        onSearchQueryChange = {},
                        onViewStats = {},
                        onEmployeeClick = {},
                    )
                }
            }
        }
    }
}
