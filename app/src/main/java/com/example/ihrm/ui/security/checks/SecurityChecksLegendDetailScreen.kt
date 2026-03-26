package com.example.ihrm.ui.security.checks

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ihrm.R
import com.example.ihrm.ui.common.header.BaseHeader
import com.example.ihrm.ui.components.ButtonSize
import com.example.ihrm.ui.components.ButtonVariant
import com.example.ihrm.ui.components.CustomButton
import com.example.ihrm.util.DashboardBrush
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

private data class SecurityLegendMeta(
    val key: String,
    val label: String,
    val chipText: String,
    val chipTextColor: Color,
    val chipBackground: Color,
    val highlightedQuestionIndices: Set<Int> = emptySet(),
    val outlinedQuestionIndices: Set<Int> = emptySet(),
    val showPendingActions: Boolean = false,
)

private enum class ChecklistActionType {
    APPROVE,
    REJECT
}

private data class ChecklistDialogUi(
    val title: String,
    val message: String,
    val iconRes: Int,
    val iconTint: Color,
    val iconBackground: Color,
    val confirmText: String,
    val confirmVariant: ButtonVariant,
)

private val SecurityDetailHeaderBrush = DashboardBrush.BaseBackground
private val AccordionCardBg = Color(0xFFABF5D1)
private val AccordionRejectedBg = Color(0xFFFFBDAD)
private val AccordionIndexBg = Color(0xFFDEEBFF)
private val AccordionTextColor = Color(0xFF091E42)
private val AccordionPendingOutline = Color(0xFF98E8E6)

@Composable
fun SecurityChecksLegendDetailScreen(
    legendKey: String,
    onBackClick: () -> Unit,
) {
    val legend = remember(legendKey) { legendByKey(legendKey) }
    val questions = remember(legendKey) { securityQuestionsByLegend(legend.label) }
    var activeDialogType by remember { mutableStateOf<ChecklistActionType?>(null) }
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding(),
        containerColor = Color.Transparent,
        contentWindowInsets = WindowInsets(top = 0),
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(brush = DashboardBrush.BaseBackground)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(
                    top = paddingValues.calculateTopPadding() + WindowInsets.statusBars
                        .asPaddingValues()
                        .calculateTopPadding(),
                    bottom = paddingValues.calculateBottomPadding()
                ),
            ) {
                item {
                    BaseHeader(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        title = stringResource(R.string.security_checks_title),
                        showNavigationIcon = true,
                        onNavigationClick = onBackClick,
                        navigationIcon = {
                            Icon(
                                modifier = Modifier.size(24.dp),
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = null,
                                tint = Color.White
                            )
                        },
                        containerColor = Color.Transparent
                    )
                }
                item {
                    CardSecurityDetail(legend)
                }
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFDEEBFF))
                            .padding(vertical = 10.dp, horizontal = 16.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.security_checks_detail_section_title),
                            color = Color(0xFF0747A6),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
                items(questions.size) { idx ->
                    SecurityQuestionAccordionCard(
                        question = questions[idx],
                        backgroundColor = if (idx in legend.highlightedQuestionIndices) {
                            AccordionRejectedBg
                        } else {
                            AccordionCardBg
                        },
                        borderColor = if (idx in legend.outlinedQuestionIndices) {
                            AccordionPendingOutline
                        } else {
                            Color(0xFFE5E7EB)
                        },
                        borderWidth = if (idx in legend.outlinedQuestionIndices) 2.dp else 1.dp,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }
                if (legend.showPendingActions) {
                    item {
                        PendingActionsRow(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 6.dp),
                            onLeftClick = { activeDialogType = ChecklistActionType.REJECT },
                            onRightClick = { activeDialogType = ChecklistActionType.APPROVE },
                        )
                    }
                }
            }
        }
    }

    if (activeDialogType != null) {
        ChecklistConfirmDialog(
            type = activeDialogType!!,
            onDismiss = { activeDialogType = null },
            onConfirm = { activeDialogType = null }
        )
    }
}

@Composable
private fun CardSecurityDetail(legend: SecurityLegendMeta) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.24f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "A",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "#12381208",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "Nguyen Van A",
                    color = Color.White,
                    fontSize = 14.sp,
                    lineHeight = 21.sp
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "temp-v5",
                        color = Color(0xFF155DFC),
                        fontSize = 12.sp,
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color(0xFFEFF6FF))
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                    Text(text = "•", color = Color.White, fontSize = 12.sp)
                    Text(text = "GDC", color = Color.White, fontSize = 12.sp)
                    Text(text = "•", color = Color.White, fontSize = 12.sp)
                    Text(text = "Developer", color = Color.White, fontSize = 12.sp)
                }
            }
            Text(
                text = legend.chipText,
                color = legend.chipTextColor,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .clip(RoundedCornerShape(999.dp))
                    .background(legend.chipBackground)
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Color(0xFFF4F5F7))
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = stringResource(R.string.security_checks_submitted_date),
            color = Color.White,
            fontSize = 12.sp
        )
        Text(
            text = "29/12/2025",
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

private fun legendByKey(key: String): SecurityLegendMeta {
    return when (key) {
        "approved" -> SecurityLegendMeta(
            key = key,
            label = "Approved",
            chipText = "APPROVED",
            chipTextColor = Color(0xFF008236),
            chipBackground = Color(0xFFDCFCE7),
        )

        "rejected" -> SecurityLegendMeta(
            key = key,
            label = "Rejected",
            chipText = "REJECT",
            chipTextColor = Color(0xFFF10C00),
            chipBackground = Color(0xFFFFC1C2),
            highlightedQuestionIndices = setOf(1),
        )

        "submitted" -> SecurityLegendMeta(
            key = key,
            label = "Submitted",
            chipText = "SUBMITTED",
            chipTextColor = Color(0xFF007AFF),
            chipBackground = Color(0xFFDDEBFF),
            showPendingActions = true,
        )

        "pending" -> SecurityLegendMeta(
            key = key,
            label = "Pending",
            chipText = "PENDING",
            chipTextColor = Color(0xFFB35A00),
            chipBackground = Color(0xFFF9D9A7),
            outlinedQuestionIndices = setOf(3),
            showPendingActions = true,
        )

        else -> SecurityLegendMeta(
            key = key,
            label = "Not Submitted",
            chipText = "NOT SUBMITTED",
            chipTextColor = Color(0xFF4B5563),
            chipBackground = Color(0xFFE5E7EB),
        )
    }
}

@Composable
private fun SecurityQuestionAccordionCard(
    question: SecurityQuestionItem,
    backgroundColor: Color,
    borderColor: Color,
    borderWidth: androidx.compose.ui.unit.Dp,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        border = BorderStroke(borderWidth, borderColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(AccordionIndexBg, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = question.id.toString(),
                    color = Color(0xFF0747A6),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Text(
                text = question.content,
                modifier = Modifier.weight(1f),
                color = AccordionTextColor,
                fontSize = 14.sp,
                lineHeight = 22.sp,
                fontWeight = FontWeight.Medium
            )
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = null,
                tint = Color(0xFF6A7282),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
private fun ChecklistConfirmDialog(
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
            modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
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
private fun PendingActionsRow(
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
            modifier = Modifier.weight(1f),
            size = ButtonSize.Large,
            variant = variantButtonLeft,
            text = textButtonLeft,
            onClick = onLeftClick,
        )
        CustomButton(
            modifier = Modifier.weight(1f),
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

