package com.example.ihrm.ui.navigation

import androidx.compose.material3.DrawerState
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.ihrm.ui.auth.LoginScreen
import com.example.ihrm.ui.auth.SignUpScreen
import com.example.ihrm.ui.auth.SplashScreen
import com.example.ihrm.ui.employee.addedit.AddEditEmployeeScreen
import com.example.ihrm.ui.employee.detail.EmployeeDetailScreen
import com.example.ihrm.ui.dashboard.DashboardScreen
import com.example.ihrm.ui.employee.list.EmployeeListScreen
import com.example.ihrm.ui.loginTest.LoginTestScreen
import com.example.ihrm.util.AuthManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

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
                    navController.navigate(Screen.LoginTest.route) {
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
                    navController.navigate(Screen.EmployeeList.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToSignUp = {
                    navController.navigate(Screen.SignUp.route)
                }
            )
        }

        composable(Screen.LoginTest.route) {
            LoginTestScreen(
                onLoginSuccess = {
                    AuthManager.setLoggedIn(true)
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.LoginTest.route) { inclusive = true }
                    }
                },
                onNavigateToSignUp = {
                    navController.navigate(Screen.SignUp.route)
                }
            )
        }

        composable(Screen.SignUp.route) {
            SignUpScreen(
                onSignUpSuccess = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.SignUp.route) { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.Dashboard.route) {
            DashboardScreen(
                onMenuClick = { scope.launch { drawerState?.open() } },
                onProfileClick = { },
                onViewDetails = { employeeId ->
                    navController.navigate(Screen.EmployeeDetail.createRoute(employeeId))
                },
                onAddEmployee = {
                    navController.navigate(Screen.AddEmployee.route)
                },
                onViewStats = {
                    navController.navigate(Screen.EmployeeList.route)
                }
            )
        }

        composable(Screen.EmployeeList.route) {
            EmployeeListScreen(
                onEmployeeClick = { employeeId ->
                    navController.navigate(Screen.EmployeeDetail.createRoute(employeeId))
                },
                onAddEmployeeClick = {
                    navController.navigate(Screen.AddEmployee.route)
                }
            )
        }

        composable(
            route = Screen.EmployeeDetail.route,
            arguments = listOf(navArgument("employeeId") {})
        ) { backStackEntry ->
            val employeeId = backStackEntry.arguments?.getString("employeeId") ?: return@composable
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
                onSaveSuccess = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = Screen.EditEmployee.route,
            arguments = listOf(navArgument("employeeId") {})
        ) { backStackEntry ->
            val employeeId = backStackEntry.arguments?.getString("employeeId") ?: return@composable
            AddEditEmployeeScreen(
                employeeId = employeeId,
                onSaveSuccess = {
                    navController.popBackStack()
                }
            )
        }
    }
}