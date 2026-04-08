package com.example.ihrm.ui.dashboard.personal

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ihrm.R
import com.example.ihrm.ui.common.Avatar
import com.example.ihrm.ui.dashboard.DashboardProfileModel
import com.example.ihrm.ui.dashboard.SecurityMonthlyModel
import com.example.ihrm.ui.dashboard.dashboardSections.SecurityCheckThisMonthProgressSection
import com.example.ihrm.ui.theme.DashboardFigmaInk
import com.example.ihrm.ui.theme.DashboardFigmaMuted
import com.example.ihrm.ui.theme.DashboardShieldCyan
import com.example.ihrm.ui.theme.DashboardTabActiveBlue
import com.example.ihrm.ui.theme.InterFontFamily
import com.example.ihrm.util.txtInterBold16
import com.example.ihrm.util.txtInterRegular12
import com.example.ihrm.ui.localization.tr

@Composable
fun DashboardSecurityCardPersonal(
    monthly: SecurityMonthlyModel,
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
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .shadow(6.dp, RoundedCornerShape(14.dp))
                        .clip(RoundedCornerShape(14.dp))
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(DashboardShieldCyan, DashboardTabActiveBlue)
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Column {
                    Text(
                        text = tr(R.string.dashboard_security_title),
                        style = TextStyle(
                            fontFamily = InterFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            lineHeight = 21.sp,
                            color = DashboardFigmaInk,
                            letterSpacing = (-0.31).sp
                        )
                    )
                    Text(
                        text = tr(R.string.dashboard_security_subtitle),
                        style = TextStyle(
                            fontFamily = InterFontFamily,
                            fontWeight = FontWeight.Normal,
                            fontSize = 11.sp,
                            lineHeight = 16.5.sp,
                            color = DashboardFigmaMuted
                        )
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(
                    modifier = Modifier
                        .clip(RoundedCornerShape(14.dp))
                        .border(1.dp, Color(0xFFDBEAFE), RoundedCornerShape(14.dp))
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    Color(0xFFEFF6FF),
                                    Color(0xFFECFEFF),
                                )
                            )
                        )
                        .padding(12.dp)
                ) {
                    Text(
                        text = tr(R.string.dashboard_security_in_review_personal),
                        style = txtInterBold16
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = tr(R.string.dashboard_security_in_review_body_personal),
                        style = txtInterRegular12
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally

            ) {
                Avatar(
                    imageUrl = profile.avatarSecurity,
                    initials = profile.avatarInitials,
                    isSecurity = true,
                    placeholderImage = painterResource(R.drawable.ic_avatar_security),
                    errorImage = painterResource(R.drawable.ic_avatar_security),
                    size = 112.dp
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            SecurityCheckThisMonthProgressSection(monthly)
        }
    }
}