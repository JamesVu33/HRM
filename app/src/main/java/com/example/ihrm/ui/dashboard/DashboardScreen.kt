package com.example.ihrm.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ihrm.R
import com.example.ihrm.ui.common.BaseHRMCompose
import com.example.ihrm.ui.common.header.DashboardHomeTab
import com.example.ihrm.ui.common.header.DashboardHomeTabSubtitle
import com.example.ihrm.ui.common.header.DashboardHomeTabSwitcher
import com.example.ihrm.ui.common.header.DashboardHomeTopBar
import com.example.ihrm.ui.dashboard.dashboardSections.DashboardLeaveSection
import com.example.ihrm.ui.dashboard.extra.DashboardManagementTabContent
import com.example.ihrm.ui.dashboard.extra.DashboardProfileCardManagement
import com.example.ihrm.ui.dashboard.extra.DashboardSecurityCardManagement
import com.example.ihrm.ui.dashboard.personal.DashboardProfileCardPersonal
import com.example.ihrm.ui.dashboard.personal.DashboardSecurityCardPersonal
import com.example.ihrm.ui.theme.DashboardGradientMid
import com.example.ihrm.ui.theme.DashboardGradientSoft
import com.example.ihrm.ui.theme.DashboardGradientTop
import com.example.ihrm.ui.theme.IHRMTheme
import com.example.ihrm.util.AuthManager
import com.example.ihrm.ui.localization.tr

@Composable
fun DashboardScreen(
    onMenuClick: () -> Unit,
    onProfileClick: () -> Unit,
    onBellClick: () -> Unit = onProfileClick,
    onCalendarManagement: () -> Unit,
    onViewStats: () -> Unit,
    viewmodel: DashboardViewModel = hiltViewModel()
) {
    BaseHRMCompose(
        content = {
            DashboardScreenContent(
                onMenuClick = onMenuClick,
                onProfileClick = onProfileClick,
                onBellClick = onBellClick,
                onCalendarManagement = onCalendarManagement,
                onViewStats = onViewStats,
                viewmodel = viewmodel
            )
        },
        onErrorAlertClose = onMenuClick,
        viewmodel = viewmodel
    )
}

@Composable
fun DashboardScreenContent(
    viewmodel: DashboardViewModel,
    onMenuClick: () -> Unit,
    onProfileClick: () -> Unit,
    onBellClick: () -> Unit = onProfileClick,
    onCalendarManagement: () -> Unit,
    onViewStats: () -> Unit
) {
    val mock = DashboardMockData.rememberHomeModel()
    val meEmployeeInfo by viewmodel.meEmployeeInfo.collectAsStateWithLifecycle()
    val homeModel = remember(mock, meEmployeeInfo) {
        val me = meEmployeeInfo
        if (me == null) {
            mock
        } else {
            mock.copy(
                profile = mapMeEmployeeToDashboardProfile(
                    me,
                    mock.profile,
                    AuthManager.getUserFullName(),
                    AuthManager.getUserEmail(),
                    AuthManager.getUserEmployeeId(),
                    AuthManager.getUserPhone()
                )
            )
        }
    }
    val accountType = AuthManager.getAccountType()
    val role = remember(accountType) { accountType.toDashboardRole() }
    val securityCard by viewmodel.dashboardSecurityCardState.collectAsStateWithLifecycle()
    val managementSecurity by viewmodel.managementSecurityState.collectAsStateWithLifecycle()
    val personalUi = remember(homeModel, securityCard) {
        buildPersonalRoleUi(homeModel).copy(
            securityMonthly = securityCard.monthly,
            securityBanner = securityCard.banner
        )
    }
    val extraUi = remember(homeModel, securityCard, managementSecurity) {
        buildExtraRoleUi(homeModel).copy(
            securityMonthly = securityCard.monthly,
            securityBanner = securityCard.banner,
            management = homeModel.management.copy(security = managementSecurity)
        )
    }
    var selectedTab by remember { mutableStateOf(DashboardHomeTab.Personal) }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding(),
        containerColor = Color.Transparent,
        topBar = {
            Column (
                modifier = Modifier.fillMaxWidth().statusBarsPadding()
            ){
                DashboardHomeTopBar(
                    greeting = tr(R.string.dashboard_good_morning),
                    dateText = tr(R.string.dashboard_mock_date),
                    onMenuClick = onMenuClick,
                    onBellClick = onBellClick,
                    showBellBadge = true
                )
                if (role == DashboardRole.Extra) {
                    Spacer(modifier = Modifier.height(8.dp))
                    DashboardHomeTabSwitcher(
                        selected = selectedTab,
                        onSelect = { selectedTab = it }
                    )
                    DashboardHomeTabSubtitle(tab = selectedTab)
                }
            }
        }
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
                    .padding(paddingValues)
            ) {
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
                                        securityMonthly = extraUi.securityMonthly,
                                        securityBanner = extraUi.securityBanner
                                    )
                                )
                            }
                        }

                        DashboardHomeTab.Management -> {
                            item {
                                DashboardManagementTabContent(
                                    ui = extraUi.management,
                                    onCalendarNavigate = onCalendarManagement,
                                    onSecurityNavigate = onViewStats
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
        DashboardSecurityCardManagement(
            monthly = personalUi.securityMonthly,
            profile = personalUi.profile,
            banner = personalUi.securityBanner
        )
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
        DashboardSecurityCardPersonal(
            monthly = personalUi.securityMonthly,
            profile = personalUi.profile,
            banner = personalUi.securityBanner
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DashboardScreenPreview() {
    IHRMTheme {
        DashboardScreen(
            onMenuClick = {},
            onProfileClick = {},
            onCalendarManagement = {},
            onViewStats = {}
        )
    }
}
