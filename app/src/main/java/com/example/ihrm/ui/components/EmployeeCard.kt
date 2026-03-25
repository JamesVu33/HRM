package com.example.ihrm.ui.components

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
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ihrm.R
import com.example.ihrm.domain.model.Employee
import com.example.ihrm.ui.theme.AvatarBlueBg
import com.example.ihrm.ui.theme.BadgeOrangeBg
import com.example.ihrm.ui.theme.BadgePurpleBg
import com.example.ihrm.ui.theme.Error
import com.example.ihrm.ui.theme.ErrorLight
import com.example.ihrm.ui.theme.IHRMTheme
import com.example.ihrm.ui.theme.Neutral400
import com.example.ihrm.ui.theme.Neutral500
import com.example.ihrm.ui.theme.Neutral700
import com.example.ihrm.ui.theme.Primary400
import com.example.ihrm.ui.theme.PrimaryTint
import com.example.ihrm.ui.theme.SurfaceBorder
import com.example.ihrm.ui.theme.TextSecondary

/**
 * Employee card matching [Figma HRM EmployeeCard](https://www.figma.com/design/Q9qmml4Qj4FD73VnYshBsO/HRM-Mobile-App-Ver?node-id=255-11482).
 * Shows avatar (initials), name, position, badge, ID, email, phone, and View Details / Delete actions.
 */
@Composable
fun EmployeeCard(
    employee: Employee,
    levelCode: String,
    onViewDetails: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val avatarBg = when (employee.id.hashCode() % 3) {
        0 -> BadgePurpleBg
        1 -> BadgeOrangeBg
        else -> AvatarBlueBg
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = androidx.compose.ui.graphics.Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(avatarBg),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = employee.name.take(3).uppercase().filter { it.isLetter() }.ifEmpty { "?" },
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Neutral700
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = employee.name,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Neutral700,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = employee.position ?: "—",
                        fontSize = 12.sp,
                        color = Neutral500
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    EmployeeCardBadge(text = levelCode)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = stringResource(R.string.dashboard_id_format, employee.id),
                        fontSize = 12.sp,
                        color = Neutral400
                    )
                }
            }
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 12.dp),
                color = SurfaceBorder,
                thickness = 1.dp
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = TextSecondary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = employee.email,
                        fontSize = 12.sp,
                        color = TextSecondary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.Phone,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = TextSecondary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = employee.phone,
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                FlatButton(
                    text = stringResource(R.string.dashboard_view_details),
                    textColor = Primary400,
                    backgroundColor = PrimaryTint,
                    onClick = onViewDetails,
                    modifier = Modifier.weight(1f)
                )
                FlatButton(
                    text = stringResource(R.string.dashboard_delete),
                    textColor = Error,
                    backgroundColor = ErrorLight,
                    onClick = onDelete,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun EmployeeCardBadge(text: String) {
    Box(
        modifier = Modifier.width(65.dp)
            .height(22.dp)
            .background(color = Color(0xFFF3E8FF), shape = RoundedCornerShape(size = 36369700.dp))
            .padding(start = 8.dp, top = 2.dp, end = 8.dp, bottom = 2.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = TextStyle(
                fontSize = 12.sp,
                lineHeight = 16.sp,
                fontWeight = FontWeight(500),
                color = Color(0xFF8200DB),
            ),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun EmployeeCardPreview() {
    IHRMTheme {
        EmployeeCard(
            employee = Employee(
                id = "emp_002",
                name = "Nguyen Van A",
                email = "example@gmail.com",
                phone = "0123456789",
                department = null,
                position = "Developer",
                hireDate = null,
                salary = null,
                address = null
            ),
            levelCode = "S1",
            onViewDetails = {},
            onDelete = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}
