package com.example.ihrm.ui.dashboard.personal

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ihrm.R
import com.example.ihrm.ui.dashboard.DashboardMockData
import com.example.ihrm.ui.dashboard.DashboardProfileModel
import com.example.ihrm.ui.dashboard.buildPersonalRoleUi
import com.example.ihrm.ui.dashboard.dashboardSections.ProfileInfoItemSection
import com.example.ihrm.ui.dashboard.extra.DashboardProfileCardManagement
import com.example.ihrm.ui.theme.DashboardFigmaMuted
import com.example.ihrm.ui.theme.DashboardProfileTitleBlue
import com.example.ihrm.ui.theme.DashboardTabActiveBlue
import com.example.ihrm.ui.theme.InterFontFamily
import com.example.ihrm.ui.theme.PrimaryTint
import com.example.ihrm.util.Avatar

@Composable
fun DashboardProfileCardPersonal(
    profile: DashboardProfileModel,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.6f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.2f),
                            Color.Transparent
                        )
                    )
                )
                .padding(24.dp),
        ) {

            Column (
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally

            ) {
                Avatar(
                    size = 120.dp,
                    imageUrl = profile.avatarUrl,
                    initials = profile.avatarInitials,
                    isOnline = true,
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = profile.displayName,
                    style = TextStyle(
                        fontFamily = InterFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        lineHeight = 24.sp,
                        color = DashboardProfileTitleBlue,
                        letterSpacing = (-0.31).sp
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = stringResource(R.string.dashboard_id_format, profile.employeeId),
                    style = TextStyle(
                        fontFamily = InterFontFamily,
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp,
                        lineHeight = 18.sp,
                        color = DashboardFigmaMuted
                    )
                )
                Spacer(modifier = Modifier.height(13.dp))
                ProfileInfoItemSection(
                    modifierContent = Modifier.padding(vertical = 12.dp),
                    icon = {
                        Icon(
                            Icons.Default.Email,
                            contentDescription = null,
                            tint = DashboardTabActiveBlue,
                            modifier = Modifier.size(14.dp)
                        )
                    },
                    label = stringResource(R.string.dashboard_profile_email_label),
                    value = profile.email,
                    valueSemiBold = true,
                )
                Spacer(modifier = Modifier.height(12.dp))
                ProfileInfoItemSection(
                    modifierContent = Modifier.padding(vertical = 12.dp),
                    icon = {
                        Icon(
                            Icons.Default.Phone,
                            contentDescription = null,
                            tint = DashboardTabActiveBlue,
                            modifier = Modifier.size(14.dp)
                        )
                    },
                    label = stringResource(R.string.dashboard_profile_phone_label),
                    value = profile.phone,
                    valueSemiBold = true,
                )
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ProfileInfoItemSection(
                        modifierContent = Modifier.padding(vertical = 12.dp),
                        icon = {
                            Icon(
                                Icons.Default.Edit,
                                contentDescription = null,
                                tint = DashboardTabActiveBlue,
                                modifier = Modifier.size(14.dp)
                            )
                        },
                        label = stringResource(R.string.dashboard_profile_joined_label),
                        value = profile.joined,
                        valueSemiBold = true,
                        modifier = Modifier.weight(1f)
                    )
                    ProfileInfoItemSection(
                        modifierContent = Modifier.padding(vertical = 12.dp),
                        icon = {
                            Icon(
                                Icons.Default.Home,
                                contentDescription = null,
                                tint = DashboardTabActiveBlue,
                                modifier = Modifier.size(14.dp)
                            )
                        },
                        label = stringResource(R.string.dashboard_profile_department_label),
                        value = profile.departmentDetail,
                        valueSemiBold = true,
                        modifier = Modifier.weight(1f)
                    )
                }
            }


        }
    }
}

@Preview
@Composable
fun DashboardProfileCardPersonal_Preview() {
    val mock = DashboardMockData.rememberHomeModel()
    val personalUi = remember(mock) { buildPersonalRoleUi(mock) }
    DashboardProfileCardPersonal(profile = personalUi.profile)
}