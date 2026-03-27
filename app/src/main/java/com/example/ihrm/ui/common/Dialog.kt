package com.example.ihrm.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ihrm.R
import com.example.ihrm.ui.components.ButtonSize
import com.example.ihrm.ui.components.ButtonVariant
import com.example.ihrm.ui.components.CustomButton
import com.example.ihrm.util.dropShadow

@Composable
fun ChecklistConfirmDialog(
    type: ChecklistActionType,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    val ui = remember(type) { dialogUi(type) }
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 16.dp),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFF3F4F6))
                        .clickable(onClick = onDismiss),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = null,
                        tint = Color(0xFF9CA3AF),
                        modifier = Modifier.size(20.dp)
                    )
                }

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(ui.iconBackground),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(ui.iconRes),
                            contentDescription = null,
                            tint = ui.iconTint,
                            modifier = Modifier.size(48.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = ui.title,
                        color = Color(0xFF101828),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = ui.message,
                        color = Color(0xFF6A7282),
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    PendingActionsRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 6.dp),
                        onLeftClick = onDismiss,
                        onRightClick = onConfirm,
                        textButtonLeft = stringResource(R.string.logout_cancel_button),
                        textButtonRight = ui.confirmText,
                        variantButtonLeft = ButtonVariant.Neutral,
                        variantButtonRight = ui.confirmVariant,
                    )
                }
            }
        }
    }
}

@Composable
fun PendingActionsRow(
    modifier: Modifier = Modifier,
    textButtonLeft: String = "Reject",
    textButtonRight: String = "Approve",
    variantButtonLeft: ButtonVariant = ButtonVariant.Danger,
    variantButtonRight: ButtonVariant = ButtonVariant.Primary,
    onLeftClick: () -> Unit,
    onRightClick: () -> Unit,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        CustomButton(
            modifier = Modifier
                .dropShadow(
                    offsetX = 0.dp,
                    offsetY = 4.dp,
                    blur = 6.dp,
                    shape = RoundedCornerShape(14.dp),
                    color = Color(0x00000000).copy(0.1f),
                    spread = (-1).dp
                )
                .weight(1f),
            size = ButtonSize.Large,
            variant = variantButtonLeft,
            text = textButtonLeft,
            onClick = onLeftClick,
        )
        CustomButton(
            modifier = Modifier
                .weight(1f)
                .dropShadow(
                    offsetX = 0.dp,
                    offsetY = 4.dp,
                    blur = 6.dp,
                    shape = RoundedCornerShape(14.dp),
                    color = Color(0x00000000).copy(0.1f),
                    spread = (-1).dp
                ),
            size = ButtonSize.Large,
            variant = variantButtonRight,
            text = textButtonRight,
            onClick = onRightClick,
        )
    }
}

private fun dialogUi(type: ChecklistActionType): ChecklistDialogUi {
    return when (type) {
        ChecklistActionType.APPROVE -> ChecklistDialogUi(
            title = "Approve Checklist?",
            message = "Are you sure you want to approve this security checklist?",
            iconRes = R.drawable.ic_approve,
            iconTint = Color(0xFF1A53B8),
            iconBackground = Color(0xFFB3D4FF),
            confirmText = "Approve",
            confirmVariant = ButtonVariant.Primary,
        )

        ChecklistActionType.REJECT -> ChecklistDialogUi(
            title = "Reject Checklist?",
            message = "Are you sure you want to reject this security checklist?",
            iconRes = R.drawable.ic_rejected,
            iconTint = Color(0xFFF44336),
            iconBackground = Color(0xFFFEE2E2),
            confirmText = "Reject",
            confirmVariant = ButtonVariant.Danger,
        )
    }
}

enum class ChecklistActionType {
    APPROVE,
    REJECT
}

data class ChecklistDialogUi(
    val title: String,
    val message: String,
    val iconRes: Int,
    val iconTint: Color,
    val iconBackground: Color,
    val confirmText: String,
    val confirmVariant: ButtonVariant,
)