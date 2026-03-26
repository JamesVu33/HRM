package com.example.ihrm.ui.security.checks

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ihrm.R

private data class CreateChecklistQuestionUi(
    val content: String,
    val translation: String? = null,
    val selected: Boolean = false,
    val expanded: Boolean = false
)

private val createChecklistQuestions = listOf(
    CreateChecklistQuestionUi(
        content = "There are no information (ID, PW, etc.) leakage written on post-it notes and attached to open places (like monitors, desks)?"
    ),
    CreateChecklistQuestionUi(
        content = "Are information (ID, PW, etc.) leakage safely managed without being stored on the PC or department's web hard?",
        translation = "Thong tin (ID, PW, v.v.) co nguy co ro ri co duoc quan ly an toan ma khong can luu tru tren PC hoac web cua bo phan khong?",
        selected = true,
        expanded = true
    ),
    CreateChecklistQuestionUi(
        content = "Are confidential documents (including personal information) or print-outs containing work-related information stored in lockable drawers or cabinets?",
        selected = true
    ),
    CreateChecklistQuestionUi(content = "Are confidential information prohibited to print-outs?"),
    CreateChecklistQuestionUi(content = "Is the screen locked when leaving the seat?")
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
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 12.dp)
            ) {
                Button(
                    onClick = onSubmitClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0747A6))
                ) {
                    Text(
                        text = stringResource(R.string.create_security_checklist_submit),
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colorStops = arrayOf(
                            0.0f to Color(0xFF0A53BE),
                            0.50f to Color(0xFF5A93E5),
                            0.75f to Color(0xFF9CBFF2),
                            1.0f to Color(0xFFF3F4F6)
                        )
                    )
                )
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
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                    tint = Color.White
                )
            }
            Text(
                text = stringResource(R.string.create_security_checklist_title),
                color = Color.White,
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Text(
            text = stringResource(R.string.create_security_checklist_subtitle),
            color = Color.White,
            fontSize = 14.sp,
            modifier = Modifier.padding(start = 8.dp, top = 4.dp)
        )
    }
}

@Composable
private fun QuestionsCard() {
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
                    text = stringResource(R.string.create_security_checklist_items_title),
                    color = Color(0xFF091E42),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = stringResource(R.string.create_security_checklist_items_count),
                    color = Color(0xFF6A7282),
                    fontSize = 14.sp
                )
            }
            LazyColumn(contentPadding = PaddingValues(bottom = 4.dp)) {
                itemsIndexed(createChecklistQuestions) { index, question ->
                    QuestionRow(
                        item = question,
                        isLast = index == createChecklistQuestions.lastIndex
                    )
                }
            }
        }
    }
}

@Composable
private fun QuestionRow(item: CreateChecklistQuestionUi, isLast: Boolean) {
    val bg = if (item.selected) Color(0xFFD3DDFF) else Color.White
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
                    .padding(top = 2.dp)
                    .size(20.dp)
                    .clip(CircleShape)
                    .border(2.dp, if (item.selected) Color(0xFF12B76A) else Color(0xFFD1D5DB), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                if (item.selected) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = Color(0xFF12B76A),
                        modifier = Modifier.size(14.dp)
                    )
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
                if (item.expanded && item.translation != null) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = item.translation,
                        color = Color(0xFF6A7282),
                        fontSize = 12.sp,
                        lineHeight = 20.sp
                    )
                }
            }
            Icon(
                imageVector = if (item.expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                contentDescription = null,
                tint = Color(0xFF6A7282)
            )
        }
    }
}
