package com.example.ihrm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.ihrm.core.errorHandler.GlobalErrorHandler
import com.example.ihrm.ui.common.ErrorAlert
import com.example.ihrm.ui.common.LoadingWidget
import com.example.ihrm.ui.components.ComingSoonDialog
import com.example.ihrm.ui.components.DrawerMenu
import com.example.ihrm.ui.components.LogoutConfirmDialog
import com.example.ihrm.ui.navigation.NavGraph
import com.example.ihrm.ui.navigation.Screen
import com.example.ihrm.ui.theme.IHRMTheme
import com.example.ihrm.util.AuthManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            IHRMTheme {
                HRMApp()
            }
        }
    }
}

@Composable
fun HRMApp() {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: Screen.Splash.route

    // Show drawer only for authenticated screens (not splash/signup/login_test)
    val showDrawer = currentRoute != Screen.Splash.route &&
            currentRoute != Screen.Login.route

    // listen the global error event
    val globalMsg = remember { mutableStateOf<String>("") }
    LaunchedEffect(Unit) {
        GlobalErrorHandler.globalError.collect { msg ->
            globalMsg.value = msg
        }
    }

    Box {
        // page flow
        if (showDrawer) {
            DrawerContent(currentRoute, drawerState, scope, navController)
        } else {
            NavGraph(
                navController = navController,
                drawerState = null,
                scope = scope
            )
        }

        // global error popup
        ErrorAlert()

        // loading
//        LoadingWidget()
    }
}

@Composable
private fun DrawerContent(
    currentRoute: String,
    drawerState: DrawerState,
    scope: CoroutineScope,
    navController: NavHostController
) {
    var showLogoutDialog by remember { mutableStateOf(false) }
    var comingSoonFeatureName by remember { mutableStateOf<String?>(null) }
    val gesturesEnabled = currentRoute == Screen.Dashboard.route
    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = gesturesEnabled,
        drawerContent = {
            DrawerMenu(
                drawerState = drawerState,
                scope = scope,
                currentRoute = currentRoute,
                onItemClick = { route ->
                    when (route) {
                        Screen.Dashboard.route -> {
                            navController.navigate(route) {
                                popUpTo(Screen.Dashboard.route) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }

                        Screen.EmployeeList.route -> {
                            navController.navigate(route) {
                                popUpTo(Screen.Dashboard.route) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    }
                },
                onLogoutClick = { showLogoutDialog = true },
                onShowComingSoon = { featureName -> comingSoonFeatureName = featureName }
            )
        }
    ) {
        NavGraph(
            navController = navController,
            drawerState = drawerState,
            scope = scope
        )
    }
    if (showLogoutDialog) {
        LogoutConfirmDialog(
            onConfirm = {
                showLogoutDialog = false
                scope.launch { drawerState.close() }
                AuthManager.clearTokens()
                navController.navigate(Screen.Login.route) {
                    popUpTo(0) { inclusive = true }
                }
            },
            onDismiss = { showLogoutDialog = false }
        )
    }
    if (comingSoonFeatureName != null) {
        ComingSoonDialog(
            featureName = comingSoonFeatureName!!,
            onDismiss = { comingSoonFeatureName = null }
        )
    }
}