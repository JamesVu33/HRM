package com.example.ihrm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.ihrm.ui.components.ChangeLanguageDialog
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
        //ErrorAlert()

        // loading
//        LoadingWidget()
    }
}

private val drawerGestureRoutes = setOf(
    Screen.Dashboard.route,
    Screen.EmployeeList.route,
    Screen.SecurityChecks.route,
    Screen.MyInfo.route,
    Screen.MySecurityCheck.route,
    Screen.Organization.route
)

private sealed class DrawerDialog {
    data object Logout : DrawerDialog()
    data class ComingSoon(val featureName: String) : DrawerDialog()
    data object ChangeLanguage : DrawerDialog()
}

@Composable
private fun DrawerContent(
    currentRoute: String,
    drawerState: DrawerState,
    scope: CoroutineScope,
    navController: NavHostController
) {
    var activeDialog by remember { mutableStateOf<DrawerDialog?>(null) }
    val gesturesEnabled = remember(currentRoute) { currentRoute in drawerGestureRoutes }

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = gesturesEnabled,
        drawerContent = {
            ModalDrawerSheet(drawerContainerColor = Color.White) {
                DrawerMenu(
                    drawerState = drawerState,
                    scope = scope,
                    currentRoute = currentRoute,
                    onItemClick = { route ->
                        scope.launch {
                            drawerState.close()
                            if (currentRoute != route) {
                                navigateWithStandardOptions(navController, route)
                            }}
                    },
                    onLogoutClick = { activeDialog = DrawerDialog.Logout },
                    onShowComingSoon = { activeDialog = DrawerDialog.ComingSoon(it) },
                    onTranslationKeysClick = { activeDialog = DrawerDialog.ChangeLanguage }
                )
            }
        }
    ) {
        NavGraph(
            navController = navController,
            drawerState = drawerState,
            scope = scope
        )
    }

    when (val dialog = activeDialog) {
        is DrawerDialog.Logout -> LogoutConfirmDialog(
            onConfirm = {
                activeDialog = null
                AuthManager.clearTokens()
                navController.navigate(Screen.Login.route) {
                    popUpTo(navController.graph.id) { inclusive = true }
                    launchSingleTop = true
                }
            },
            onDismiss = { activeDialog = null }
        )
        is DrawerDialog.ComingSoon -> ComingSoonDialog(
            featureName = dialog.featureName,
            onDismiss = { activeDialog = null }
        )
        DrawerDialog.ChangeLanguage -> ChangeLanguageDialog(
            onDismiss = { activeDialog = null },
            onSaveLanguage = { _ ->
                // TODO: call change-language API with selected code, then apply locale.
                activeDialog = null
            }
        )
        null -> Unit
    }
}
private fun navigateWithStandardOptions(navController: NavHostController, route: String) {
    navController.navigate(route) {
        popUpTo(Screen.Dashboard.route) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}