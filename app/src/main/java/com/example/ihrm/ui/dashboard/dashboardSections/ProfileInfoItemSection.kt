package com.example.ihrm.ui.dashboard.dashboardSections

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ihrm.ui.theme.DashboardFigmaInk
import com.example.ihrm.ui.theme.DashboardFigmaMuted
import com.example.ihrm.ui.theme.DashboardProfileCellBg
import com.example.ihrm.ui.theme.InterFontFamily
import com.example.ihrm.ui.theme.PrimaryTint

@Composable
fun ProfileInfoItemSection(
    icon: @Composable () -> Unit,
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    modifierContent: Modifier = Modifier,
    valueSemiBold: Boolean = false
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(DashboardProfileCellBg)
            .padding(horizontal = 8.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = modifierContent
                .size(28.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(PrimaryTint),
            contentAlignment = Alignment.Center
        ) {
            icon()
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                style = TextStyle(
                    fontFamily = InterFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp,
                    color = DashboardFigmaMuted
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = value,
                style = TextStyle(
                    fontFamily = InterFontFamily,
                    fontWeight = if (valueSemiBold) FontWeight.SemiBold else FontWeight.Medium,
                    fontSize = 14.sp,
                    color = DashboardFigmaInk
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
