package com.example.ihrm.ui.security.detail

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.ihrm.R
import com.example.ihrm.domain.model.AppFeature
import com.example.ihrm.domain.usecase.securities.CheckItemUiModel
import com.example.ihrm.domain.usecase.securities.SecurityDetailUiModel
import com.example.ihrm.domain.usecase.securities.SecurityStatus
import com.example.ihrm.ui.common.BaseHRMCompose
import com.example.ihrm.ui.common.ChecklistActionType
import com.example.ihrm.ui.common.ChecklistConfirmDialog
import com.example.ihrm.ui.common.PendingActionsRow
import com.example.ihrm.ui.common.header.BaseHeader
import com.example.ihrm.util.AuthManager
import com.example.ihrm.util.DashboardBrush
import com.example.ihrm.util.LabelTextStyle13SemiBold

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
    viewModel: SecurityChecksDetailViewModel = hiltViewModel()
) {
    BaseHRMCompose(
        content = {
            SecurityChecksLegendDetailContent(
                legendKey = legendKey,
                onBackClick = onBackClick,
                viewModel = viewModel
            )
        },
        viewmodel = viewModel,
        onErrorAlertClose = onBackClick
    )
}

@Composable
fun SecurityChecksLegendDetailContent(
    legendKey: String,
    onBackClick: () -> Unit,
    viewModel: SecurityChecksDetailViewModel,
) {
    LaunchedEffect(Unit) {
        viewModel.getSecurityCheckDetail(legendKey)
    }

    val canReviewChecklist = AuthManager.canModify(AppFeature.SECURITY_CHECK)
    var activeDialogType by remember { mutableStateOf<ChecklistActionType?>(null) }

    val uiState by viewModel.uiState.collectAsState()

    val questions = remember(uiState.checkList) {
        uiState.checkList.ifEmpty {
            emptyList()
        }
    }

    val legend = remember(uiState.status) { legendByKey(uiState.status ?: "") }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding(),
        containerColor = Color.Transparent,
        contentWindowInsets = WindowInsets(top = 0),
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(brush = DashboardBrush.BaseBackground)
        ) {
            BaseHeader(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(paddingValues)
                    .statusBarsPadding(),
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
            if (legend.key.isNotEmpty() && !uiState.status.isNullOrEmpty()) {
                CardSecurityDetail(legend, uiState)
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(
                        bottom = paddingValues.calculateBottomPadding()
                    ),
                ) {
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
                    items(
                        items = questions,
                        key = { it.key }
                    ) { item ->
                        SecurityQuestionAccordionCard(
                            id = uiState.checkList.indexOf(item) + 1,
                            question = item,
                            backgroundColor = if (!item.isChecked) {
                                AccordionRejectedBg
                            } else {
                                AccordionCardBg
                            },
                            borderColor = if (item.isChecked) {
                                AccordionPendingOutline
                            } else {
                                Color(0xFFE5E7EB)
                            },
                            borderWidth = if (item.isChecked) 2.dp else 1.dp,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }
                    if (legend.showPendingActions && canReviewChecklist) {
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
                    item { Spacer(modifier = Modifier.height(8.dp)) }
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
private fun CardSecurityDetail(
    legend: SecurityLegendMeta,
    uiState: SecurityDetailUiModel
) {
    val status = SecurityStatus.fromKey(legend.key)
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
                    text = "${uiState.userName?.takeLast(2)}",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column{
                        Text(
                            text = "ID: ${uiState.employeeId}",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "${uiState.userName}",
                            color = Color.White,
                            fontSize = 14.sp,
                            lineHeight = 21.sp
                        )
                    }
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(999.dp))
                            .background(status.backgroundColor)
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            imageVector = ImageVector.vectorResource(status.iconRes),
                            contentDescription = null,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = legend.label,
                            color = legend.chipTextColor,
                            style = LabelTextStyle13SemiBold
                        )
                    }
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${uiState.templateName}",
                        color = Color(0xFF155DFC),
                        fontSize = 12.sp,
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color(0xFFEFF6FF))
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Color(0xFFF4F5F7))
        )
        Spacer(modifier = Modifier.height(10.dp))
        if (!uiState.approveBy.isNullOrEmpty()) {
            if (status != SecurityStatus.SUBMITTED) {
                Text(
                    text = stringResource(status.labelRes, uiState.approveBy),
                    color = Color.White,
                    fontSize = 12.sp
                )
            }
        }
        Text(
            text = stringResource(R.string.security_checks_submitted_date) + ": ${uiState.submittedAt}",
            color = Color.White,
            fontSize = 12.sp
        )
    }
}

private fun legendByKey(key: String): SecurityLegendMeta {
    return when (key.lowercase()) {
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
    question: CheckItemUiModel,
    backgroundColor: Color,
    borderColor: Color,
    borderWidth: Dp,
    modifier: Modifier = Modifier,
    id: Int,
) {
    Log.d("Vinh", "SecurityQuestionAccordionCard: $question")
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
                    text = id.toString(),
                    color = Color(0xFF0747A6),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = question.titleEn,
                    color = AccordionTextColor,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = question.titleVi,
                    color = AccordionTextColor,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal
                )
            }
        }
    }
}