package com.example.ihrm.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.ihrm.util.AuthManager
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ihrm.R
import com.example.ihrm.ui.theme.DrawerSectionLabel
import com.example.ihrm.ui.theme.DrawerItemSelected
import com.example.ihrm.ui.theme.FABGradientStart
import com.example.ihrm.ui.theme.LogoutBorder
import com.example.ihrm.ui.theme.LogoutIconBgEnd
import com.example.ihrm.ui.theme.LogoutIconBgStart
import com.example.ihrm.ui.theme.LogoutRed
import com.example.ihrm.ui.theme.Neutral50
import com.example.ihrm.ui.theme.Neutral600
import com.example.ihrm.ui.theme.Neutral200
import com.example.ihrm.ui.theme.PrimaryTint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/** Routes that have a real screen. Others show Coming Soon. */
private val IMPLEMENTED_ROUTES = setOf("dashboard", "employee_list")

data class DrawerMenuItem(
    val route: String,
    val labelResId: Int,
    val icon: ImageVector
)

data class DrawerMenuSection(
    val titleResId: Int,
    val items: List<DrawerMenuItem>
)

private val DRAWER_SECTIONS: List<DrawerMenuSection> = listOf(
    DrawerMenuSection(
        titleResId = R.string.drawer_section_dashboard,
        items = listOf(
            DrawerMenuItem("dashboard", R.string.drawer_item_dashboard, Icons.Default.Home)
        )
    ),
    DrawerMenuSection(
        titleResId = R.string.drawer_section_hr_info,
        items = listOf(
            DrawerMenuItem("employee_list", R.string.drawer_item_employees, Icons.Default.Person),
            DrawerMenuItem("attendance_management", R.string.drawer_item_attendance, Icons.Default.Info)
        )
    ),
    DrawerMenuSection(
        titleResId = R.string.drawer_section_security,
        items = listOf(
            DrawerMenuItem("security_check", R.string.drawer_item_security_check, Icons.Default.Lock),
            DrawerMenuItem("templates", R.string.drawer_item_templates, Icons.Default.Info)
        )
    ),
    DrawerMenuSection(
        titleResId = R.string.drawer_section_organization,
        items = listOf(
            DrawerMenuItem("structure", R.string.drawer_item_structure, Icons.Default.Info)
        )
    ),
    DrawerMenuSection(
        titleResId = R.string.drawer_section_admin,
        items = listOf(
            DrawerMenuItem("permission", R.string.drawer_item_permission, Icons.Default.Lock),
            DrawerMenuItem("role", R.string.drawer_item_role, Icons.Default.Settings),
            DrawerMenuItem("team", R.string.drawer_item_team, Icons.Default.Person),
            DrawerMenuItem("translation_keys", R.string.drawer_item_translation_keys, Icons.Default.Settings)
        )
    )
)

@Composable
fun DrawerMenu(
    drawerState: DrawerState,
    scope: CoroutineScope,
    currentRoute: String,
    onItemClick: (String) -> Unit,
    onLogoutClick: () -> Unit = {},
    onShowComingSoon: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    ModalDrawerSheet(
        modifier = modifier,
        drawerContainerColor = androidx.compose.ui.graphics.Color.White
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            DrawerHeader(scope = scope, drawerState = drawerState)
            DrawerUserCard(
                fullName = AuthManager.getUserFullName(),
                email = AuthManager.getUserEmail()
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(0.dp)
                ) {
                    DRAWER_SECTIONS.forEach { section ->
                        item(key = "header_${section.titleResId}") {
                            Text(
                                text = stringResource(section.titleResId).uppercase(),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = DrawerSectionLabel,
                                modifier = Modifier.padding(start = 8.dp, top = 16.dp, bottom = 8.dp)
                            )
                        }
                        items(
                            items = section.items,
                            key = { "${section.titleResId}_${it.route}" }
                        ) { item ->
                            val label = stringResource(item.labelResId)
                            val selected = when (item.route) {
                                "employee_list" -> currentRoute == "employee_list" || currentRoute.startsWith("employee_detail")
                                else -> currentRoute == item.route
                            }
                            DrawerItem(
                                icon = item.icon,
                                label = label,
                                selected = selected,
                                onClick = {
                                    if (item.route in IMPLEMENTED_ROUTES) {
                                        scope.launch { drawerState.close() }
                                        onItemClick(item.route)
                                    } else {
                                        onShowComingSoon(label)
                                    }
                                }
                            )
                        }
                    }
                }
            }
            DrawerLogoutButton(onClick = onLogoutClick)
        }
    }
}

@Composable
private fun DrawerHeader(scope: CoroutineScope, drawerState: DrawerState) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.size(32.dp), contentAlignment = Alignment.Center) {
            androidx.compose.foundation.Image(
                painter = painterResource(R.drawable.shinhan_logo),
                contentDescription = null,
                modifier = Modifier.size(32.dp)
            )
        }
        Spacer(modifier = Modifier.size(8.dp))
        Text(
            text = stringResource(R.string.drawer_brand),
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = androidx.compose.ui.graphics.Color(0xFF0a0a0a)
        )
        Spacer(modifier = Modifier.weight(1f))
        IconButton(onClick = { scope.launch { drawerState.close() } }, modifier = Modifier.size(32.dp)) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = stringResource(R.string.drawer_close),
                tint = Neutral600
            )
        }
    }
}

@Composable
private fun DrawerUserCard(
    fullName: String?,
    email: String?
) {
    val displayName = fullName?.takeIf { it.isNotBlank() } ?: stringResource(R.string.drawer_user_name)
    val displayEmail = email?.takeIf { it.isNotBlank() } ?: stringResource(R.string.drawer_user_email)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .heightIn(min = 56.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(Neutral50)
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(Neutral600.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Default.Person,
                contentDescription = null,
                tint = Neutral600,
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = displayName,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF0a0a0a)
            )
            Text(
                text = displayEmail,
                fontSize = 12.sp,
                color = Neutral600
            )
        }

        IconButton(
            onClick = {},
            modifier = Modifier.size(32.dp)
        ) {
            Icon(
                Icons.Default.Edit,
                contentDescription = stringResource(R.string.drawer_edit_profile),
                tint = Neutral600,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Composable
private fun DrawerLogoutButton(onClick: () -> Unit) {
    Box(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .border(1.5.dp, LogoutBorder, RoundedCornerShape(8.dp))
                .background(androidx.compose.ui.graphics.Color.White, RoundedCornerShape(8.dp))
                .clickable(onClick = onClick)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = LogoutRed, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.size(8.dp))
            Text(text = stringResource(R.string.drawer_logout), fontSize = 14.sp, fontWeight = FontWeight.Medium, color = LogoutRed)
        }
    }
}

@Composable
private fun DrawerItem(
    icon: ImageVector,
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .then(if (selected) Modifier.background(PrimaryTint) else Modifier)
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (selected) DrawerItemSelected else Neutral600,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.size(12.dp))
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = if (selected) DrawerItemSelected else Neutral600
        )
    }
}

@Composable
fun ComingSoonDialog(
    featureName: String,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(androidx.compose.ui.graphics.Color.White, RoundedCornerShape(24.dp))
                .padding(top = 32.dp, bottom = 24.dp, start = 24.dp, end = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(brush = Brush.linearGradient(colors = listOf(PrimaryTint, androidx.compose.ui.graphics.Color(0xFFdbeafe)))),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = DrawerItemSelected,
                    modifier = Modifier.size(32.dp)
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = stringResource(R.string.coming_soon_title),
                fontSize = 22.sp,
                fontWeight = FontWeight.SemiBold,
                color = androidx.compose.ui.graphics.Color(0xFF0a0a0a),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = stringResource(R.string.coming_soon_message, featureName),
                fontSize = 15.sp,
                color = Neutral600,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(24.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .drawBehind {
                        drawLine(
                            color = androidx.compose.ui.graphics.Color.Black.copy(alpha = 0.08f),
                            start = Offset(0f, 0f),
                            end = Offset(size.width, 0f),
                            strokeWidth = 1.5f
                        )
                    }
                    .padding(top = 16.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Button(
                        onClick = onDismiss,
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = FABGradientStart),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp, pressedElevation = 2.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.coming_soon_got_it),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    Button(
                        onClick = onDismiss,
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = androidx.compose.ui.graphics.Color.White),
                        border = androidx.compose.foundation.BorderStroke(1.5.dp, Neutral200),
                        elevation = ButtonDefaults.buttonElevation(0.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.coming_soon_close),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = Neutral600
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun LogoutConfirmDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(androidx.compose.ui.graphics.Color.White, RoundedCornerShape(20.dp))
                .padding(top = 32.dp, bottom = 24.dp, start = 24.dp, end = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(brush = Brush.linearGradient(colors = listOf(LogoutIconBgStart, LogoutIconBgEnd))),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = LogoutRed, modifier = Modifier.size(28.dp))
            }
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = stringResource(R.string.logout_dialog_title),
                fontSize = 22.sp,
                fontWeight = FontWeight.SemiBold,
                color = androidx.compose.ui.graphics.Color(0xFF0a0a0a),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = stringResource(R.string.logout_dialog_message),
                fontSize = 15.sp,
                color = Neutral600,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(24.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .drawBehind {
                        drawLine(
                            color = androidx.compose.ui.graphics.Color.Black.copy(alpha = 0.08f),
                            start = Offset(0f, 0f),
                            end = Offset(size.width, 0f),
                            strokeWidth = 1.5f
                        )
                    }
                    .padding(top = 16.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Button(
                        onClick = onConfirm,
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = LogoutRed),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp, pressedElevation = 2.dp),
                        contentPadding = ButtonDefaults.ContentPadding
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = androidx.compose.ui.graphics.Color.White, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.size(8.dp))
                        Text(text = stringResource(R.string.logout_yes_button), fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
                    }
                    Button(
                        onClick = onDismiss,
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = androidx.compose.ui.graphics.Color.White),
                        border = androidx.compose.foundation.BorderStroke(1.5.dp, Neutral200),
                        elevation = ButtonDefaults.buttonElevation(0.dp)
                    ) {
                        Text(text = stringResource(R.string.logout_cancel_button), fontSize = 15.sp, fontWeight = FontWeight.Medium, color = Neutral600)
                    }
                }
            }
        }
    }
}
