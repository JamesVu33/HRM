package com.example.ihrm.ui.employee.list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.ihrm.R
import com.example.ihrm.ui.theme.Primary200
import com.example.ihrm.ui.theme.Primary400
import com.example.ihrm.ui.theme.Primary50
import com.example.ihrm.ui.theme.Primary500
import com.example.ihrm.ui.common.header.BaseHeader

@Composable
fun EmployeeListScreen(
    onEmployeeClick: (String) -> Unit,
    onAddEmployeeClick: () -> Unit,
    onBackClick: (() -> Unit)? = null,
    viewModel: EmployeeListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        // Screen initialization if needed
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            BaseHeader(
                title = stringResource(R.string.employee_list_title),
                showNavigationIcon = onBackClick != null,
                onNavigationClick = onBackClick,
                navigationIcon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null,
                        tint = Color.White
                    )
                },
                containerBrush = Brush.linearGradient(
                    colors = listOf(
                        Primary500,
                        Primary400,
                        Primary200,
                        Primary50
                    )
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator()
                }

                uiState.error != null -> {
                    Text(text = "Error: ${uiState.error}")
                }

                uiState.employeeUiModels.isEmpty() -> {
                    Text(text = "No employees found")
                }

                else -> {
                    // UI model đã gộp Employee + Level (levelCode dùng cho badge, v.v.)
                    Text(text = "Employee List Screen\n${uiState.employeeUiModels.size} employees")
                }
            }
        }
    }
}