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
import com.example.ihrm.ui.login.LoginScreen
import com.example.ihrm.util.AuthManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

private const val EMPLOYEE_ID_PARAM = "employeeId"

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