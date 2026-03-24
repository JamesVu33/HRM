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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ihrm.R
import com.example.ihrm.ui.dashboard.dashboardSections.DashboardLeaveSection
import com.example.ihrm.ui.dashboard.extra.DashboardSecurityCardManagement
import com.example.ihrm.ui.dashboard.extra.DashboardManagementTabContent
import com.example.ihrm.ui.dashboard.extra.DashboardProfileCardManagement
import com.example.ihrm.ui.dashboard.personal.DashboardProfileCardPersonal
import com.example.ihrm.ui.dashboard.personal.DashboardSecurityCardPersonal
import com.example.ihrm.ui.theme.DashboardGradientMid
import com.example.ihrm.ui.theme.DashboardGradientSoft
import com.example.ihrm.ui.theme.DashboardGradientTop
import com.example.ihrm.ui.theme.IHRMTheme
import com.example.ihrm.ui.common.header.DashboardHomeTab
import com.example.ihrm.ui.common.header.DashboardHomeTabSubtitle
import com.example.ihrm.ui.common.header.DashboardHomeTabSwitcher
import com.example.ihrm.ui.common.header.DashboardHomeTopBar
import com.example.ihrm.util.AuthManager

@Composable
fun DashboardScreen(
    onMenuClick: () -> Unit,
    onProfileClick: () -> Unit,
    onBellClick: () -> Unit = onProfileClick,
    onViewStats: () -> Unit
) {
    val mock = DashboardMockData.rememberHomeModel()
    val role = remember(
        AuthManager.getUserEmail(),
        AuthManager.getUserFullName()
    ) {
        resolveDashboardRoleAfterLogin(
            email = AuthManager.getUserEmail(),
            fullName = AuthManager.getUserFullName()
        )
    }
    val personalUi = remember(mock) { buildPersonalRoleUi(mock) }
    val extraUi = remember(mock) { buildExtraRoleUi(mock) }
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

                if (role == DashboardRole.Extra) {
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
                }

                item { Spacer(modifier = Modifier.height(16.dp)) }

                if (role == DashboardRole.Personal) {
                    item {
                        DashboardPersonalRoleSection(personalUi = personalUi)
                    }
                    item { Spacer(modifier = Modifier.height(32.dp)) }
                } else {
                    when (selectedTab) {
                        DashboardHomeTab.Personal -> {
                            item {
                                DashboardExtraRoleSection(
                                    personalUi = DashboardPersonalRoleUi(
                                        profile = extraUi.profile,
                                        leaveStats = extraUi.leaveStats,
                                        securityMonthly = extraUi.securityMonthly
                                    )
                                )
                            }
                        }

                        DashboardHomeTab.Management -> {
                            item {
                                DashboardManagementTabContent(
                                    ui = extraUi.management,
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
}

@Composable
private fun DashboardExtraRoleSection(
    personalUi: DashboardPersonalRoleUi
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Personal role dashboard UI (Figma 871:30135).
        DashboardProfileCardManagement(profile = personalUi.profile)
        DashboardLeaveSection(stats = personalUi.leaveStats)
        DashboardSecurityCardManagement(monthly = personalUi.securityMonthly, profile = personalUi.profile)
    }
}

@Composable
private fun DashboardPersonalRoleSection(
    personalUi: DashboardPersonalRoleUi
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Personal role dashboard UI (Figma 871:30135).
        DashboardProfileCardPersonal(profile = personalUi.profile)
        DashboardLeaveSection(stats = personalUi.leaveStats)
        DashboardSecurityCardPersonal(monthly = personalUi.securityMonthly, profile = personalUi.profile)
    }
}

@Preview(showBackground = true)
@Composable
private fun DashboardScreenPreview() {
    IHRMTheme {
        DashboardScreen(
            onMenuClick = {},
            onProfileClick = {},
            onViewStats = {}
        )
    }
}
