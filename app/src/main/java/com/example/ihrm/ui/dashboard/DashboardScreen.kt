package com.example.ihrm.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.ihrm.R
import com.example.ihrm.domain.model.Employee
import com.example.ihrm.ui.components.CustomButton
import com.example.ihrm.ui.components.EmployeeCard
import com.example.ihrm.ui.theme.AppBackground
import com.example.ihrm.ui.theme.ColorBgSuccessGradientBottom
import com.example.ihrm.ui.theme.ColorBgSuccessGradientMiddle
import com.example.ihrm.ui.theme.ColorBgSuccessGradientTop
import com.example.ihrm.ui.theme.DrawerOrangeBorder
import com.example.ihrm.ui.theme.EdittextBg
import com.example.ihrm.ui.theme.EdittextBg_50
import com.example.ihrm.ui.theme.EdittextBg_70
import com.example.ihrm.ui.theme.FABGradientEnd
import com.example.ihrm.ui.theme.FABGradientStart
import com.example.ihrm.ui.theme.IHRMTheme
import com.example.ihrm.ui.theme.Neutral400
import com.example.ihrm.ui.theme.Neutral500
import com.example.ihrm.ui.theme.Neutral200
import com.example.ihrm.ui.theme.Neutral600
import com.example.ihrm.ui.theme.Neutral700
import com.example.ihrm.ui.theme.Primary200
import com.example.ihrm.ui.theme.Primary50
import com.example.ihrm.ui.theme.Primary400
import com.example.ihrm.ui.theme.Primary500
import com.example.ihrm.ui.theme.StrokeColor
import com.example.ihrm.ui.theme.SuccessGreen
import com.example.ihrm.ui.theme.SurfaceBorder

@Composable
fun DashboardScreen(
    onMenuClick: () -> Unit,
    onProfileClick: () -> Unit,
    onViewDetails: (String) -> Unit,
    onAddEmployee: () -> Unit,
    onViewStats: () -> Unit,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var employeeToDelete by remember { mutableStateOf<Employee?>(null) }

    // Reload list from DB when Dashboard is shown (e.g. after back from Employee Detail)
    LaunchedEffect(Unit) {
        viewModel.refreshEmployees()
    }

    if (employeeToDelete != null) {
        DeleteConfirmDialog(
            employeeName = employeeToDelete!!.name,
            onDismiss = { employeeToDelete = null },
            onConfirmDelete = {
                viewModel.deleteEmployee(employeeToDelete!!.id)
                employeeToDelete = null
            }
        )
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = AppBackground,
        contentWindowInsets = WindowInsets(top = 0),
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddEmployee,
                modifier = Modifier
                    .navigationBarsPadding()
                    .size(56.dp)
                    .shadow(8.dp, CircleShape),
                shape = CircleShape,
                containerColor = Color.Transparent,
                contentColor = Color.White,
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
                            contentDescription = stringResource(R.string.dashboard_fab_add)
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Primary500,
                                Primary400,
                                Primary200,
                                Primary50,
                                Primary50,
                                Primary50
                            )
                        )
                    )
            )

            // Content layer
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .padding(paddingValues)
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    item {
                        DashboardHeader(
                            greeting = uiState.greeting,
                            dateText = uiState.dateText,
                            onMenuClick = onMenuClick,
                            onProfileClick = onProfileClick
                        )
                    }
                    item {
                        Spacer(modifier = Modifier.height(12.dp))
                        DashboardSearchBar(
                            query = uiState.searchQuery,
                            onQueryChange = viewModel::updateSearchQuery,
                            placeholder = stringResource(R.string.dashboard_search_placeholder)
                        )
                    }
                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                        ManageTeamCard(onViewStats = onViewStats)
                    }
                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                        StatsRow(
                            totalEmployees = uiState.totalEmployees,
                            activeToday = uiState.activeToday
                        )
                    }
                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = stringResource(R.string.dashboard_team_members),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Neutral700
                        )
                    }
                    item { Spacer(modifier = Modifier.height(12.dp)) }
                    items(
                        items = uiState.filteredEmployees,
                        key = { it.id }
                    ) { employee ->
                        EmployeeCard(
                            employee = employee,
                            onViewDetails = { onViewDetails(employee.id) },
                            onDelete = { employeeToDelete = employee }
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                    item { Spacer(modifier = Modifier.height(80.dp)) }
                }
            }
        }
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
            colors = CardDefaults.cardColors(containerColor = Color.White),
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
                    text = stringResource(R.string.dashboard_delete_dialog_title),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = androidx.compose.ui.graphics.Color(0xFF101828)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(R.string.dashboard_delete_dialog_message, employeeName),
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
                            text = stringResource(R.string.dashboard_delete_dialog_cancel),
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
                            contentColor = Color.White
                        ),
                        elevation = ButtonDefaults.buttonElevation(4.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.dashboard_delete_dialog_confirm),
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
private fun DashboardHeader(
    greeting: String,
    dateText: String,
    onMenuClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onMenuClick,
            modifier = Modifier.size(36.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = null,
                tint = White
            )
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp)
        ) {
            Text(
                text = greeting,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = White
            )
            Text(
                text = dateText,
                fontSize = 14.sp,
                color = StrokeColor
            )
        }
        IconButton(onClick = onProfileClick) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .border(1.5.dp, DrawerOrangeBorder, CircleShape)
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = Neutral500,
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    }
}

@Composable
private fun DashboardSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    placeholder: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
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
                modifier = Modifier
                    .weight(1f),
                singleLine = true,
                textStyle = androidx.compose.ui.text.TextStyle(
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
            label = stringResource(R.string.dashboard_total_employees),
            valueColor = Primary400
        )
        StatCard(
            modifier = Modifier.weight(1f),
            value = activeToday.toString(),
            label = stringResource(R.string.dashboard_active_today),
            valueColor = SuccessGreen
        )
    }
}

@Composable
private fun StatCard(
    modifier: Modifier = Modifier,
    value: String,
    label: String,
    valueColor: androidx.compose.ui.graphics.Color
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = androidx.compose.ui.graphics.Color.White),
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
        modifier = Modifier.fillMaxWidth(),
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
                    text = stringResource(R.string.dashboard_manage_team_title),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = stringResource(R.string.dashboard_manage_team_subtitle),
                    fontSize = 14.sp,
                    color = androidx.compose.ui.graphics.Color.Black
                )
                Spacer(modifier = Modifier.height(11.dp))
                CustomButton(
                    text = stringResource(R.string.dashboard_view_stats),
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
private fun DashboardScreenPreview() {
    IHRMTheme {
        DashboardScreen(
            onMenuClick = {},
            onProfileClick = {},
            onViewDetails = {},
            onAddEmployee = {},
            onViewStats = {}
        )
    }
}
