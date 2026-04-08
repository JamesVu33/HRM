package com.example.ihrm.ui.security.checks

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ihrm.R
import com.example.ihrm.ui.common.header.BaseHeader
import com.example.ihrm.ui.components.ButtonSize
import com.example.ihrm.ui.components.CustomButton
import com.example.ihrm.util.DashboardBrush
import com.example.ihrm.ui.localization.tr

private data class CreateChecklistQuestionUi(
    val content: String,
    val translation: String? = null,
    val selected: Boolean = false,
    val expanded: Boolean = false
)

private fun initialCreateChecklistQuestions(): List<CreateChecklistQuestionUi> = listOf(
    CreateChecklistQuestionUi(
        content = "There are no information (ID, PW, etc.) leakage written on post-it notes and attached to open places (like monitors, desks)?",
        translation = "Các thông tin (ID, PW, v.v.) có nguy cơ rò rỉ không được viết trên các ghi chú và dán ở những nơi mở (màn hình, bàn làm việc) đúng không?"
    ),
    CreateChecklistQuestionUi(
        content = "Are information (ID, PW, etc.) leakage safely managed without being stored on the PC or department's web hard?",
        translation = "Thong tin (ID, PW, v.v.) co nguy co ro ri co duoc quan ly an toan ma khong can luu tru tren PC hoac web cua bo phan khong?",
    ),
    CreateChecklistQuestionUi(
        content = "Are confidential documents (including personal information) or print-outs containing work-related information stored in lockable drawers or cabinets?",
        translation = "Các tài liệu bảo mật (bao gồm thông tin cá nhân) hoặc bản in chứa thông tin liên quan đến công việc có được lưu trữ trong ngăn kéo hoặc tủ có khóa không?",
    ),
    CreateChecklistQuestionUi(
        content = "Are confidential information prohibited to print-outs?",
        translation = "Thông tin bảo mật có bị cấm in ra không?"
    ),
    CreateChecklistQuestionUi(
        content = "Is the screen locked when leaving the seat?",
        translation = "Màn hình có được khóa khi rời khỏi chỗ ngồi không?"
    )
)

@Composable
fun CreateSecurityChecklistScreen(
    onBackClick: () -> Unit,
    onSubmitClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
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
                enabled = true,
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
            QuestionsCard()
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
private fun QuestionsCard() {
    var questions by remember { mutableStateOf(initialCreateChecklistQuestions()) }

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
                    text = tr(R.string.create_security_checklist_items_count),
                    color = Color(0xFF6A7282),
                    fontSize = 14.sp
                )
            }
            LazyColumn(contentPadding = PaddingValues(bottom = 4.dp)) {
                itemsIndexed(questions, key = { _, q -> q.content }) { index, question ->
                    QuestionRow(
                        item = question,
                        isLast = index == questions.lastIndex,
                        onExpandedToggle = {
                            questions = questions.mapIndexed { i, q ->
                                if (i == index) q.copy(expanded = !q.expanded) else q
                            }
                        },
                        onSelectedToggle = {
                            questions = questions.mapIndexed { i, q ->
                                if (i == index) q.copy(selected = !q.selected) else q
                            }
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
    val hasTranslation = !item.translation.isNullOrBlank()
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
                    text = item.content,
                    color = if (item.selected) Color(0xFF6A7282) else Color(0xFF0A0A0A),
                    fontSize = 14.sp,
                    lineHeight = 23.sp,
                    fontWeight = FontWeight.Medium
                )
                if (item.expanded && hasTranslation) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = item.translation.orEmpty(),
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
