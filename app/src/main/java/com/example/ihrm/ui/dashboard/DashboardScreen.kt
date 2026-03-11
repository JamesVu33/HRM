package com.example.ihrm.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.ihrm.R
import com.example.ihrm.domain.model.Employee
import com.example.ihrm.ui.components.CustomButton
import com.example.ihrm.ui.components.EmployeeCard
import com.example.ihrm.ui.theme.IHRMTheme
import com.example.ihrm.ui.theme.Neutral400
import com.example.ihrm.ui.theme.Neutral500
import com.example.ihrm.ui.theme.Primary200
import com.example.ihrm.ui.theme.Primary400
import com.example.ihrm.ui.theme.Primary50
import com.example.ihrm.ui.theme.Primary500
import com.example.ihrm.ui.theme.SuccessGreen

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

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddEmployee,
                modifier = Modifier.size(56.dp),
                shape = CircleShape,
                containerColor = Primary500,
                contentColor = androidx.compose.ui.graphics.Color.White
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.dashboard_fab_add)
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Primary400,
                            Primary200,
                            Primary50,
                            androidx.compose.ui.graphics.Color.White
                        )
                    )
                )
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
                        color = androidx.compose.ui.graphics.Color.White
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
                        onDelete = { viewModel.deleteEmployee(employee.id) }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
                item { Spacer(modifier = Modifier.height(80.dp)) }
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
            .padding(top = 32.dp, bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onMenuClick) {
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = null,
                tint = androidx.compose.ui.graphics.Color.White
            )
        }
        Column(modifier = Modifier.weight(1f).padding(horizontal = 8.dp)) {
            Text(
                text = greeting,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = androidx.compose.ui.graphics.Color.White
            )
            Text(
                text = dateText,
                fontSize = 14.sp,
                color = Neutral400
            )
        }
        IconButton(onClick = onProfileClick) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(androidx.compose.ui.graphics.Color.White.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = androidx.compose.ui.graphics.Color.White,
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
            .clip(RoundedCornerShape(14.dp))
            .background(
                androidx.compose.ui.graphics.Color.White.copy(alpha = 0.36f)
            )
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = androidx.compose.ui.graphics.Color.White,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            BasicTextField(
                value = query,
                onValueChange = onQueryChange,
                modifier = Modifier.weight(1f),
                singleLine = true,
                textStyle = androidx.compose.ui.text.TextStyle(
                    color = androidx.compose.ui.graphics.Color.White,
                    fontSize = 15.sp
                ),
                cursorBrush = SolidColor(androidx.compose.ui.graphics.Color.White),
                decorationBox = { inner ->
                    if (query.isEmpty()) {
                        Text(
                            text = placeholder,
                            color = androidx.compose.ui.graphics.Color.White.copy(alpha = 0.8f),
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
            containerColor = Primary50
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = stringResource(R.string.dashboard_manage_team_title),
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = androidx.compose.ui.graphics.Color.Black
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
