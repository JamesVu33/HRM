package com.example.ihrm.ui.navigation

import androidx.compose.material3.DrawerState
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.ihrm.ui.auth.SplashScreen
import com.example.ihrm.ui.calendar.management.CalendarManagementScreen
import com.example.ihrm.ui.dashboard.DashboardScreen
import com.example.ihrm.ui.employee.addedit.AddEditEmployeeScreen
import com.example.ihrm.ui.employee.detail.EmployeeDetailScreen
import com.example.ihrm.ui.employee.list.EmployeeListScreen
import com.example.ihrm.ui.security.checks.SecurityChecksAnalyticsScreen
import com.example.ihrm.ui.security.checks.SecurityChecksLegendDetailScreen
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
                isLoggedIn = AuthManager.isUserLoggedIn()
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
                onCancelClick = { navController.popBackStack() },
            )
        }

        composable(Screen.CalendarManagement.route) {
            CalendarManagementScreen(
                onBackClick = { navController.popBackStack() }
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
                onBackClick = { navController.popBackStack() },
                onViewStats = { navController.navigate(Screen.TeamStatistics.route) }
            )
        }

        composable(Screen.TeamStatistics.route) {
            TeamStatisticsScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Screen.SecurityChecks.route) {
            SecurityChecksScreen(
                onBackClick = { navController.popBackStack() },
                onSeeChartClick = { navController.navigate(Screen.SecurityChecksAnalytics.route) },
                onSecurityCheckClick = { legendKey ->
                    navController.navigate(Screen.SecurityChecksLegendDetail.createRoute(legendKey))
                }
            )
        }

        composable(Screen.SecurityChecksAnalytics.route) {
            SecurityChecksAnalyticsScreen(
                onBackClick = { navController.popBackStack() },
            )
        }

        composable(
            route = Screen.SecurityChecksLegendDetail.route,
            arguments = listOf(navArgument(LEGEND_KEY_PARAM) {})
        ) { backStackEntry ->
            val legendKey = backStackEntry.arguments?.getString(LEGEND_KEY_PARAM) ?: return@composable
            SecurityChecksLegendDetailScreen(
                legendKey = legendKey,
                onBackClick = { navController.popBackStack() }
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
                    navController.popBackStack()
                },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Screen.AddEmployee.route) {
            AddEditEmployeeScreen(
                employeeId = null,
                onSaveSuccess = { navController.popBackStack() },
                onBack = { navController.popBackStack() }
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
                onSaveSuccess = { navController.popBackStack() },
                onBack = { navController.popBackStack() }
            )
        }
    }
}