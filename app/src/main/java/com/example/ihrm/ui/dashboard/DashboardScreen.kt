package com.example.ihrm.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ihrm.R
import com.example.ihrm.ui.theme.DashboardGradientMid
import com.example.ihrm.ui.theme.DashboardGradientSoft
import com.example.ihrm.ui.theme.DashboardGradientTop
import com.example.ihrm.ui.theme.FABGradientEnd
import com.example.ihrm.ui.theme.FABGradientStart
import com.example.ihrm.ui.theme.IHRMTheme
import com.example.ihrm.util.base.header.DashboardHomeTab
import com.example.ihrm.util.base.header.DashboardHomeTabSubtitle
import com.example.ihrm.util.base.header.DashboardHomeTabSwitcher
import com.example.ihrm.util.base.header.DashboardHomeTopBar

@Composable
fun DashboardScreen(
    onMenuClick: () -> Unit,
    onProfileClick: () -> Unit,
    onBellClick: () -> Unit = onProfileClick,
    onViewDetails: (String) -> Unit,
    onAddEmployee: () -> Unit,
    onViewStats: () -> Unit
) {
    val mock = DashboardMockData.rememberHomeModel()
    var selectedTab by remember { mutableStateOf(DashboardHomeTab.Personal) }

    Scaffold(
        modifier = Modifier.fillMaxSize().navigationBarsPadding(),
        containerColor = Color.Transparent,
        contentWindowInsets = WindowInsets(top = 0),
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colorStops = arrayOf(
                            0f to DashboardGradientTop,
                            0.5f to DashboardGradientMid,
                            0.78f to DashboardGradientSoft,
                            1f to Color.White
                        )
                    )
                )
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .padding(paddingValues)
            ) {
                item {
                    DashboardHomeTopBar(
                        greeting = stringResource(R.string.dashboard_good_morning),
                        dateText = stringResource(R.string.dashboard_mock_date),
                        onMenuClick = onMenuClick,
                        onBellClick = onBellClick,
                        showBellBadge = true
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    DashboardHomeTabSwitcher(
                        selected = selectedTab,
                        onSelect = { selectedTab = it }
                    )
                }
                item {
                    DashboardHomeTabSubtitle(tab = selectedTab)
                }
                item { Spacer(modifier = Modifier.height(16.dp)) }

                when (selectedTab) {
                    DashboardHomeTab.Personal -> {
                        item {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp),
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                DashboardProfileCard(profile = mock.profile)
                                DashboardLeaveSection(stats = mock.leaveStats)
                                DashboardSecurityCard(monthly = mock.securityMonthly, profile = mock.profile)
                            }
                        }
                    }

                    DashboardHomeTab.Management -> {
                        item {
                            DashboardManagementTabContent(
                                ui = mock.management,
                                onNavigate = onViewStats
                            )
                        }
                    }
                }

                if (selectedTab == DashboardHomeTab.Personal) {
                    item { Spacer(modifier = Modifier.height(32.dp)) }
                }
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
