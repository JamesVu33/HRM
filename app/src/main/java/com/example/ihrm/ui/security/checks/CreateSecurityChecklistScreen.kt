package com.example.ihrm.ui.security.checks

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.ihrm.R
import com.example.ihrm.data.remote.securities.SecurityAnswerRequest
import com.example.ihrm.data.remote.securities.SecuritySubmissionRequest
import com.example.ihrm.ui.common.BaseHRMCompose
import com.example.ihrm.ui.common.header.BaseHeader
import com.example.ihrm.ui.components.ButtonSize
import com.example.ihrm.ui.components.CustomButton
import com.example.ihrm.util.DashboardBrush
import com.example.ihrm.ui.localization.tr

// 1. Cập nhật Model UI để khớp với dữ liệu Template
private data class CreateChecklistQuestionUi(
    val id: Int,
    val key: String,
    val titleEn: String,
    val titleVi: String? = null,
    val selected: Boolean = false,
    val expanded: Boolean = false
)

@Composable
fun CreateSecurityChecklistScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SecurityChecksViewModel = hiltViewModel()
) {
    // 2. Lấy dữ liệu từ ViewModel
    val templateUiState by viewModel.templateUiState.collectAsState()
    val isPostSuccess by viewModel.isPostSubmissionSuccess.collectAsState()

    // Gọi API khi màn hình khởi tạo
    LaunchedEffect(Unit) {
        viewModel.getCurrentSecurityTemplate()
    }

    // Tự động quay lại khi post thành công
    LaunchedEffect(isPostSuccess) {
        if (isPostSuccess) {
            onBackClick()
            viewModel.resetPostSubmissionSuccess()
        }
    }

    BaseHRMCompose(
        viewmodel = viewModel,
        content = {
            CreateSecurityChecklistScreenContent(
                onBackClick = onBackClick,
                modifier = modifier,
                templateUiState = templateUiState,
                viewModel = viewModel
            )
        }
    )
}

@Composable
private fun CreateSecurityChecklistScreenContent(
    onBackClick: () -> Unit,
    modifier: Modifier,
    templateUiState: SecurityTemplateUiState,
    viewModel: SecurityChecksViewModel
) {
    // 3. Mapping dữ liệu từ API sang UI State và sử dụng mutableStateOf để theo dõi thay đổi
    var questions by remember(templateUiState.template) {
        mutableStateOf<List<CreateChecklistQuestionUi>>(
            templateUiState.template?.items?.map { item ->
                CreateChecklistQuestionUi(
                    id = item.id,
                    key = item.key,
                    titleEn = item.titleEn,
                    titleVi = item.titleVi,
                    selected = false,
                    expanded = false
                )
            } ?: emptyList()
        )
    }

    val onSubmitClick: () -> Unit = {
        val answers = questions.map { q ->
            SecurityAnswerRequest(
                key = q.key,
                value = q.selected,
                remark = ""
            )
        }
        viewModel.postSubmission(SecuritySubmissionRequest(answers))
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .navigationBarsPadding(),
        containerColor = Color.Transparent,
        contentWindowInsets = WindowInsets(top = 0),
        bottomBar = {
            CustomButton(
                modifier = Modifier.padding(horizontal = 14.dp).padding(top = 30.dp, bottom = 8.dp),
                text = tr(R.string.create_security_checklist_submit),
                size = ButtonSize.Large,
                fullWidth = true,
                // Enable button chỉ khi list không trống và tất cả đã được selected
                enabled = questions.isNotEmpty() && questions.all { it.selected },
                onClick = onSubmitClick
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(brush = DashboardBrush.BaseBackground)
                .padding(paddingValues)
                .padding(horizontal = 14.dp)
        ) {
            val topInset = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
            HeaderSection(
                modifier = Modifier.padding(top = topInset + 8.dp),
                onBackClick = onBackClick
            )
            Spacer(modifier = Modifier.height(14.dp))

            QuestionsCard(
                questions = questions,
                onQuestionsChange = { questions = it }
            )
        }
    }
}

@Composable
private fun HeaderSection(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit
) {
    Column(modifier = modifier.fillMaxWidth()) {
        BaseHeader(
            title = tr(R.string.create_security_checklist_title),
            showNavigationIcon = true,
            onNavigationClick = onBackClick,
            navigationIcon = {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                    tint = Color.White
                )
            },
            containerColor = Color.Transparent
        )
        Text(
            text = tr(R.string.create_security_checklist_subtitle),
            color = Color.White,
            fontSize = 14.sp,
            modifier = Modifier.padding(start = 8.dp, top = 4.dp)
        )
    }
}

@Composable
private fun QuestionsCard(
    questions: List<CreateChecklistQuestionUi>,
    onQuestionsChange: (List<CreateChecklistQuestionUi>) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = Color.White
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF9FAFB))
                    .border(1.dp, Color(0xFFE5E7EB))
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = tr(R.string.create_security_checklist_items_title),
                    color = Color(0xFF091E42),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "${questions.count { it.selected }}/${questions.size}",
                    color = Color(0xFF6A7282),
                    fontSize = 14.sp
                )
            }
            LazyColumn(contentPadding = PaddingValues(bottom = 4.dp)) {
                itemsIndexed(questions, key = { _, q -> q.id }) { index, question ->
                    QuestionRow(
                        item = question,
                        isLast = index == questions.lastIndex,
                        onExpandedToggle = {
                            onQuestionsChange(questions.mapIndexed { i, q ->
                                if (i == index) q.copy(expanded = !q.expanded) else q
                            })
                        },
                        onSelectedToggle = {
                            onQuestionsChange(questions.mapIndexed { i, q ->
                                if (i == index) q.copy(selected = !q.selected) else q
                            })
                        },
                    )
                }
            }
        }
    }
}

@Composable
private fun QuestionRow(
    item: CreateChecklistQuestionUi,
    isLast: Boolean,
    onExpandedToggle: () -> Unit,
    onSelectedToggle: () -> Unit,
) {
    val bg = if (item.selected) Color(0xFFD3DDFF) else Color.White
    val hasTranslation = !item.titleVi.isNullOrBlank()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(bg)
            .let {
                if (!isLast) it.border(width = 1.dp, color = Color(0xFFE5E7EB)) else it
            }
            .padding(horizontal = 16.dp, vertical = 14.dp)
    ) {
        Row(verticalAlignment = Alignment.Top) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clickable(
                        onClick = onSelectedToggle,
                        role = Role.RadioButton,
                    ),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape)
                        .border(
                            2.dp,
                            if (item.selected) Color(0xFF12B76A) else Color(0xFFD1D5DB),
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (item.selected) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = tr(R.string.create_security_checklist_select_item_cd),
                            tint = Color(0xFF12B76A),
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.size(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.titleEn,
                    color = if (item.selected) Color(0xFF6A7282) else Color(0xFF0A0A0A),
                    fontSize = 14.sp,
                    lineHeight = 23.sp,
                    fontWeight = FontWeight.Medium
                )
                if (item.expanded && hasTranslation) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = item.titleVi ?: "",
                        color = Color(0xFF6A7282),
                        fontSize = 12.sp,
                        lineHeight = 20.sp
                    )
                }
            }
            if (hasTranslation) {
                IconButton(onClick = onExpandedToggle) {
                    Icon(
                        imageVector = if (item.expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = tr(R.string.create_security_checklist_expand_translation_cd),
                        tint = Color(0xFF6A7282),
                    )
                }
            }
        }
    }
}
