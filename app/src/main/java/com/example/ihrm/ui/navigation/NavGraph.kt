package com.example.ihrm.ui.navigation

import androidx.compose.material3.DrawerState
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.ihrm.ui.splash.SplashScreen
import com.example.ihrm.ui.calendar.management.CalendarManagementScreen
import com.example.ihrm.ui.dashboard.DashboardScreen
import com.example.ihrm.ui.employee.addedit.AddEditEmployeeScreen
import com.example.ihrm.ui.employee.detail.EmployeeDetailScreen
import com.example.ihrm.ui.employee.list.EmployeeListScreen
import com.example.ihrm.ui.security.checks.CreateSecurityChecklistScreen
import com.example.ihrm.ui.security.mysecurity.MySecurityCheckScreen
import com.example.ihrm.ui.security.checks.SecurityChecksAnalyticsScreen
import com.example.ihrm.ui.security.detail.SecurityChecksLegendDetailScreen
import com.example.ihrm.ui.security.checks.SecurityChecksScreen
import com.example.ihrm.ui.stats.TeamStatisticsScreen
import com.example.ihrm.ui.login.LoginScreen
import com.example.ihrm.ui.myinfo.MyInfoScreen
import com.example.ihrm.util.AuthManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

private const val EMPLOYEE_ID_PARAM = "employeeId"
private const val LEGEND_KEY_PARAM = "legendKey"

@Composable
fun NavGraph(
    navController: NavHostController,
    drawerState: DrawerState?,
    scope: CoroutineScope,
    startDestination: String = Screen.Splash.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                },
                onNavigateToHome = {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                },
                isLoggedIn = AuthManager.isUserLoggedIn() && !AuthManager.getAccessToken()
                    .isNullOrEmpty()
            )
        }

        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    AuthManager.setLoggedIn(true)
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
            )
        }

        composable(Screen.Dashboard.route) {
            DashboardScreen(
                onMenuClick = { scope.launch { drawerState?.open() } },
                onProfileClick = { },
                onCalendarManagement = {
                    navController.navigate(Screen.CalendarManagement.route)
                },
                onViewStats = {
                    navController.navigate(Screen.EmployeeList.route)
                }
            )
        }

        composable(Screen.MyInfo.route) {
            MyInfoScreen(
                onMenuClick = { scope.launch { drawerState?.open() } },
                onCancelClick = { navController.popBackStackIfPossible() },
            )
        }

        composable(Screen.MySecurityCheck.route) {
            MySecurityCheckScreen(
                onMenuClick = { scope.launch { drawerState?.open() } },
                onChecklistClick = { legendKey ->
                    navController.navigate(Screen.SecurityChecksLegendDetail.createRoute(legendKey))
                },
                onCreateChecklistClick = {
                    navController.navigate(Screen.CreateSecurityChecklist.route)
                },
            )
        }

        composable(Screen.CreateSecurityChecklist.route) {
            CreateSecurityChecklistScreen(
                onBackClick = { navController.popBackStackIfPossible() }
            )
        }

        composable(Screen.CalendarManagement.route) {
            CalendarManagementScreen(
                onBackClick = { navController.popBackStackIfPossible() }
            )
        }

        composable(Screen.EmployeeList.route) {
            EmployeeListScreen(
                onEmployeeClick = { employeeId ->
                    navController.navigate(Screen.EmployeeDetail.createRoute(employeeId))
                },
                onAddEmployeeClick = {
                    navController.navigate(Screen.AddEmployee.route)
                },
                onBackClick = { navController.popBackStackIfPossible() },
                onViewStats = { navController.navigate(Screen.TeamStatistics.route) }
            )
        }

        composable(Screen.TeamStatistics.route) {
            TeamStatisticsScreen(
                onBackClick = { navController.popBackStackIfPossible() }
            )
        }

        composable(Screen.SecurityChecks.route) {
            SecurityChecksScreen(
                onBackClick = { navController.popBackStackIfPossible() },
                onSeeChartClick = { navController.navigate(Screen.SecurityChecksAnalytics.route) },
                onSecurityCheckClick = { legendKey ->
                    navController.navigate(Screen.SecurityChecksLegendDetail.createRoute(legendKey))
                }
            )
        }

        composable(Screen.SecurityChecksAnalytics.route) {
            SecurityChecksAnalyticsScreen(
                onBackClick = { navController.popBackStackIfPossible() },
            )
        }

        composable(
            route = Screen.SecurityChecksLegendDetail.route,
            arguments = listOf(navArgument(LEGEND_KEY_PARAM) {})
        ) { backStackEntry ->
            val legendKey = backStackEntry.arguments?.getString(LEGEND_KEY_PARAM) ?: return@composable
            SecurityChecksLegendDetailScreen(
                legendKey = legendKey,
                onBackClick = { navController.popBackStackIfPossible() }
            )
        }

        composable(
            route = Screen.EmployeeDetail.route,
            arguments = listOf(navArgument(EMPLOYEE_ID_PARAM) {})
        ) { backStackEntry ->
            val employeeId =
                backStackEntry.arguments?.getString(EMPLOYEE_ID_PARAM) ?: return@composable
            EmployeeDetailScreen(
                employeeId = employeeId,
                onEditClick = { id ->
                    navController.navigate(Screen.EditEmployee.createRoute(id))
                },
                onDeleteClick = {
                    navController.popBackStackIfPossible()
                },
                onBackClick = { navController.popBackStackIfPossible() }
            )
        }

        composable(Screen.AddEmployee.route) {
            AddEditEmployeeScreen(
                employeeId = null,
                onSaveSuccess = { navController.popBackStackIfPossible() },
                onBack = { navController.popBackStackIfPossible() }
            )
        }

        composable(
            route = Screen.EditEmployee.route,
            arguments = listOf(navArgument(EMPLOYEE_ID_PARAM) {})
        ) { backStackEntry ->
            val employeeId =
                backStackEntry.arguments?.getString(EMPLOYEE_ID_PARAM) ?: return@composable
            AddEditEmployeeScreen(
                employeeId = employeeId,
                onSaveSuccess = { navController.popBackStackIfPossible() },
                onBack = { navController.popBackStackIfPossible() }
            )
        }
    }
}