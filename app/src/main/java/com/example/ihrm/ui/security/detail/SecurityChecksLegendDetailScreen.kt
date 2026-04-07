package com.example.ihrm.ui.security.detail

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
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
import com.example.ihrm.ui.theme.InterFontFamily
import com.example.ihrm.util.AuthManager
import com.example.ihrm.util.DashboardBrush
import com.example.ihrm.util.LabelTextStyle13SemiBold
import com.example.ihrm.util.SecurityLegendMeta
import com.example.ihrm.util.legendByKey

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
                    Column {
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
                    text = buildAnnotatedString {
                        append(stringResource(status.labelRes))
                        append(" ")
                        withStyle(
                            style = SpanStyle(
                                color = Color.White,
                                fontFamily = InterFontFamily,
                                fontWeight = FontWeight.Normal,
                                fontSize = 12.sp
                            )
                        ) {
                            append(uiState.approveBy)
                        }
                    },
                    color = Color.White,
                    fontFamily = InterFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp
                )
            }
        }
        if (!uiState.reason.isNullOrEmpty() && status == SecurityStatus.REJECTED) {
            ExpandableText(uiState.reason)
        }
        Text(
            text = buildAnnotatedString {
                append(stringResource(R.string.security_checks_submitted_date))
                append(" ")
                withStyle(
                    style = SpanStyle(
                        color = Color.White,
                        fontFamily = InterFontFamily,
                        fontWeight = FontWeight.Normal,
                        fontSize = 12.sp
                    )
                ) {
                    append(uiState.submittedAt)
                }
            },
            color = Color.White,
            fontFamily = InterFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 12.sp
        )
    }
}

/**
 * Maps a line end offset in "prefix + body" to a safe end index within [reasonBody] only.
 */
internal fun clampReasonBodyEnd(
    lineEndExclusiveInFull: Int,
    reasonPrefixLength: Int,
    reasonBodyLength: Int,
): Int = (lineEndExclusiveInFull - reasonPrefixLength).coerceIn(0, reasonBodyLength)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ExpandableText(
    text: String,
    modifier: Modifier = Modifier,
    collapsedMaxLines: Int = 2,
    textStyle: TextStyle = TextStyle(fontSize = 12.sp, color = Color.White),
) {
    if (text.isEmpty()) return

    val reasonPrefix = stringResource(R.string.security_checks_rejection_reason_prefix)
    val seeMoreText = stringResource(R.string.security_checks_reason_see_more)
    val seeMoreWithLeadingSpace = " $seeMoreText"
    val reasonTitle = stringResource(R.string.security_checks_rejection_reason_title)

    var isOverflowing by remember(text) { mutableStateOf(false) }
    var lastCharIndex by remember(text) { mutableIntStateOf(0) }
    var layoutMeasured by remember(text) { mutableStateOf(false) }
    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val labelSpanStyle = SpanStyle(
        color = textStyle.color,
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = textStyle.fontSize,
    )
    val bodySpanStyle = SpanStyle(
        color = textStyle.color,
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = textStyle.fontSize,
    )

    val prefixLength = reasonPrefix.length

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = reasonPrefix,
            color = textStyle.color,
            fontFamily = InterFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = textStyle.fontSize,
        )
        Box(modifier = modifier) {

            val displayText = remember(
                text,
                isOverflowing,
                lastCharIndex,
                layoutMeasured,
                reasonPrefix,
                seeMoreWithLeadingSpace,
                labelSpanStyle,
                bodySpanStyle,
                ) {
                if (isOverflowing && layoutMeasured && lastCharIndex > prefixLength) {
                    val bodyEnd = clampReasonBodyEnd(lastCharIndex, 0, text.length)
                    val reserveChars = seeMoreWithLeadingSpace.length + 8
                    val visibleBody = if (bodyEnd > reserveChars) {
                        text.take(bodyEnd - reserveChars).trimEnd()
                    } else {
                        ""
                    }
                    buildAnnotatedString {
                        withStyle(bodySpanStyle) {
                            append(visibleBody)
                            append("...")
                        }
                        val link = LinkAnnotation.Clickable(
                            tag = "expand",
                            styles = TextLinkStyles(style = labelSpanStyle),
                            linkInteractionListener = { showBottomSheet = true }
                        )

                        withLink(link) {
                            append(seeMoreWithLeadingSpace)
                        }
                    }

                } else {
                    buildAnnotatedString {
                        withStyle(bodySpanStyle) { append(text) }
                    }
                }
            }

            Text(
                text = displayText,
                style = textStyle,
                maxLines = if (layoutMeasured) collapsedMaxLines else Int.MAX_VALUE,
                overflow = TextOverflow.Clip,
                onTextLayout = { result ->
                    if (layoutMeasured) return@Text

                    val overflowing = result.lineCount > collapsedMaxLines
                    if (overflowing) {
                        val lineEnd = result.getLineEnd(collapsedMaxLines - 1, visibleEnd = true)

                        isOverflowing = true
                        lastCharIndex = lineEnd
                    }
                    layoutMeasured = true
                },
                modifier = Modifier.fillMaxWidth()
            )
        }

        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { showBottomSheet = false },
                sheetState = sheetState,
                containerColor = Color.White,
                dragHandle = null
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = reasonTitle,
                            style = TextStyle(
                                color = Color.Black,
                                fontFamily = InterFontFamily,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 16.sp
                            )
                        )
                        IconButton(onClick = { showBottomSheet = false }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = stringResource(R.string.coming_soon_close),
                                tint = Color.Black
                            )
                        }
                    }

                    HorizontalDivider(color = Color.Black.copy(alpha = 0.1f))
                    Spacer(modifier.height(16.dp))
                    Text(
                        text = text,
                        style = TextStyle(
                            color = Color.Black,
                            fontFamily = InterFontFamily,
                            fontWeight = FontWeight.Normal,
                            fontSize = 14.sp,
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState())
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
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