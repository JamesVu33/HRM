package com.example.ihrm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.ihrm.ui.components.DrawerMenu
import com.example.ihrm.ui.navigation.NavGraph
import com.example.ihrm.ui.navigation.Screen
import com.example.ihrm.ui.theme.IHRMTheme
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

//comment something
@Composable
fun HRMApp() {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: Screen.Splash.route
    
    // Show drawer only for authenticated screens (not splash/login/signup)
    val showDrawer = currentRoute != Screen.Splash.route && 
                     currentRoute != Screen.Login.route &&
                     currentRoute != Screen.SignUp.route

    if (showDrawer) {
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                DrawerMenu(
                    drawerState = drawerState,
                    scope = scope,
                    currentRoute = currentRoute,
                    onItemClick = { route ->
                        when (route) {
                            Screen.EmployeeList.route -> {
                                navController.navigate(route) {
                                    popUpTo(Screen.EmployeeList.route) {
                                        inclusive = true
                                    }
                                }
                            }
                        }
                    }
                )
            }
        ) {
            NavGraph(
                navController = navController,
                drawerState = drawerState,
                scope = scope
            )
        }
    } else {
        NavGraph(
            navController = navController,
            drawerState = null,
            scope = scope
        )
    }
}