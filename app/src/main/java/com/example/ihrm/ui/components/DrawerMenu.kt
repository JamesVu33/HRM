package com.example.ihrm.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

data class DrawerMenuItem(
    val title: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val route: String
)

@Composable
fun DrawerMenu(
    drawerState: DrawerState,
    scope: CoroutineScope,
    currentRoute: String,
    onItemClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val menuItems = listOf(
        DrawerMenuItem(
            title = "Dashboard",
            icon = Icons.Default.Home,
            route = "dashboard"
        ),
        DrawerMenuItem(
            title = "Employees",
            icon = Icons.Default.Person,
            route = "employee_list"
        )
    )

    ModalDrawerSheet(modifier = modifier) {
        menuItems.forEach { item ->
            NavigationDrawerItem(
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.title
                    )
                },
                label = { Text(item.title) },
                selected = currentRoute == item.route,
                onClick = {
                    scope.launch {
                        drawerState.close()
                    }
                    onItemClick(item.route)
                },
                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
            )
        }
    }
}