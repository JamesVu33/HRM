package com.example.ihrm.ui.common.header

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ihrm.R
import com.example.ihrm.ui.theme.DashboardBellBadge
import com.example.ihrm.ui.theme.DashboardGlassFill
import com.example.ihrm.ui.theme.DashboardGlassStroke
import com.example.ihrm.ui.theme.DashboardTabActiveBlue
import com.example.ihrm.ui.theme.InterFontFamily
import com.example.ihrm.ui.common.rememberThrottledClick
import com.example.ihrm.ui.theme.Primary400
import com.example.ihrm.ui.localization.tr

/**
 * Home dashboard top row: menu, greeting + date (start-aligned in the flexible middle), bell.
 * Spacing aligns with [BaseHeader] (16.dp horizontal).
 */
@Composable
fun DashboardHomeTopBar(
    greeting: String,
    dateText: String,
    onMenuClick: () -> Unit,
    onBellClick: () -> Unit,
    showBellBadge: Boolean,
    modifier: Modifier = Modifier
) {
    val throttledMenu = rememberThrottledClick(onClick = onMenuClick)
    val throttledBell = rememberThrottledClick(onClick = onBellClick)
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Box(
            modifier = Modifier
                .size(50.dp)
                .clip(RoundedCornerShape(8.dp))
                .clickable(onClick = throttledMenu),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = tr(R.string.dashboard_cd_open_menu),
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = greeting,
                style = TextStyle(
                    fontFamily = InterFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    lineHeight = 32.sp,
                    color = Color.White,
                    letterSpacing = 0.07.sp
                )
            )
            Text(
                text = dateText,
                style = TextStyle(
                    fontFamily = InterFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    color = Color.White.copy(alpha = 0.86f),
                    letterSpacing = (-0.15).sp
                )
            )
        }

        DashboardBellIconButton(
            onClick = throttledBell,
            showBadge = showBellBadge
        )
    }
}

@Composable
fun DashboardBellIconButton(
    onClick: () -> Unit,
    showBadge: Boolean,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(48.dp)
            .shadow(10.dp, RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Notifications,
            contentDescription = tr(R.string.dashboard_cd_notifications),
            tint = Primary400,
            modifier = Modifier.size(20.dp)
        )
        if (showBadge) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 6.dp, end = 6.dp)
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(DashboardBellBadge)
                    .border(1.dp, Color.White, CircleShape)
            )
        }
    }
}

enum class DashboardHomeTab {
    Personal,
    Management
}

@Composable
fun DashboardHomeTabSwitcher(
    selected: DashboardHomeTab,
    onSelect: (DashboardHomeTab) -> Unit,
    modifier: Modifier = Modifier
) {
    val onPersonal = rememberThrottledClick { onSelect(DashboardHomeTab.Personal) }
    val onManagement = rememberThrottledClick { onSelect(DashboardHomeTab.Management) }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(52.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(DashboardGlassFill)
            .border(1.dp, DashboardGlassStroke, RoundedCornerShape(16.dp))
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        DashboardHomeTabChip(
            label = tr(R.string.dashboard_tab_personal),
            icon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = if (selected == DashboardHomeTab.Personal) DashboardTabActiveBlue else Color.White
                )
            },
            selected = selected == DashboardHomeTab.Personal,
            onClick = onPersonal,
            modifier = Modifier.weight(1f)
        )
        DashboardHomeTabChip(
            label = tr(R.string.dashboard_tab_management),
            icon = {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_list_person),
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = if (selected == DashboardHomeTab.Management) DashboardTabActiveBlue else Color.White
                )
            },
            selected = selected == DashboardHomeTab.Management,
            onClick = onManagement,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun DashboardHomeTabChip(
    label: String,
    icon: @Composable () -> Unit,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(12.dp)
    Box(
        modifier = modifier
            .height(44.dp)
            .clip(shape)
            .then(
                if (selected) {
                    Modifier
                        .shadow(4.dp, shape)
                        .background(Color.White, shape)
                } else {
                    Modifier
                }
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            icon()
            Text(
                text = label,
                style = TextStyle(
                    fontFamily = InterFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    lineHeight = 21.sp,
                    color = if (selected) DashboardTabActiveBlue else Color.White
                )
            )
        }
    }
}

@Composable
fun DashboardHomeTabSubtitle(
    tab: DashboardHomeTab,
    modifier: Modifier = Modifier
) {
    Text(
        text = when (tab) {
            DashboardHomeTab.Personal -> tr(R.string.dashboard_tab_subtitle_personal)
            DashboardHomeTab.Management -> tr(R.string.dashboard_tab_subtitle_management)
        },
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 8.dp),
        style = TextStyle(
            fontFamily = InterFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp,
            lineHeight = 18.sp,
            color = Color.White.copy(alpha = 0.9f),
            textAlign = TextAlign.Center
        )
    )
}
