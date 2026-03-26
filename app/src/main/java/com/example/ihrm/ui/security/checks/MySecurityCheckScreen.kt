package com.example.ihrm.ui.security.checks

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ihrm.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

private enum class MySecurityStatus { PENDING, APPROVED, REJECT }

private data class MySecurityChecklistUi(
    val title: String,
    val category: String,
    val name: String,
    val template: String,
    val code: String,
    val status: MySecurityStatus,
    val dateLabel: Int,
    val date: String,
    val approver: String? = null
)

private fun MySecurityStatus.toLegendKey(): String = when (this) {
    MySecurityStatus.PENDING -> "submitted"
    MySecurityStatus.APPROVED -> "approved"
    MySecurityStatus.REJECT -> "rejected"
}

private val mySecurityItems = listOf(
    MySecurityChecklistUi("#3", "GCD", "Nguyen Van A", "temp-v5", "2503001", MySecurityStatus.PENDING, R.string.my_security_check_submitted, "03/01/2025"),
    MySecurityChecklistUi("#4", "GCD", "Nguyen Van A", "temp-v5", "2503001", MySecurityStatus.PENDING, R.string.my_security_check_submitted, "03/01/2025"),
    MySecurityChecklistUi("#5", "GCD", "Nguyen Van A", "temp-v5", "2503001", MySecurityStatus.PENDING, R.string.my_security_check_submitted, "03/01/2025"),
    MySecurityChecklistUi("#1", "GCD", "Nguyen Van A", "temp-v5", "2503001", MySecurityStatus.APPROVED, R.string.my_security_check_updated, "12/01/2024", "Nguyen Van A"),
    MySecurityChecklistUi("#2", "GCD", "Nguyen Van A", "temp-v5", "2503001", MySecurityStatus.REJECT, R.string.my_security_check_updated, "12/01/2024", "Nguyen Van A")
)

@Composable
fun MySecurityCheckScreen(
    onMenuClick: () -> Unit,
    onChecklistClick: (String) -> Unit = {},
    onCreateChecklistClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var keyword by remember { mutableStateOf("") }
    var showCreatedInMonthDialog by remember { mutableStateOf(false) }
    val filtered = remember(keyword) {
        if (keyword.isBlank()) mySecurityItems
        else {
            val q = keyword.trim().lowercase()
            mySecurityItems.filter { item ->
                item.title.lowercase().contains(q) ||
                    item.name.lowercase().contains(q) ||
                    item.category.lowercase().contains(q) ||
                    item.template.lowercase().contains(q)
            }
        }
    }
    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .navigationBarsPadding(),
        containerColor = Color.Transparent,
        contentWindowInsets = WindowInsets(top = 0),
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (hasChecklistInCurrentMonth()) {
                        showCreatedInMonthDialog = true
                    } else {
                        onCreateChecklistClick()
                    }
                },
                modifier = Modifier
                    .size(56.dp)
                    .shadow(8.dp, CircleShape),
                containerColor = Color(0xFF0747A6),
                shape = CircleShape
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.my_security_check_add_cd),
                    tint = Color.White
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colorStops = arrayOf(
                            0.0f to Color(0xFF0A53BE),
                            0.48f to Color(0xFF5A93E5),
                            0.75f to Color(0xFF9CBFF2),
                            1.0f to Color(0xFFF3F4F6)
                        )
                    )
                )
                .padding(horizontal = 16.dp)
                .padding(paddingValues)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding() + 8.dp,
                    bottom = 80.dp
                ),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item {
                    Header(onMenuClick = onMenuClick)
                    Spacer(modifier = Modifier.height(12.dp))
                    SearchBar(
                        keyword = keyword,
                        onKeywordChange = { keyword = it }
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                        Text(
                            text = stringResource(R.string.my_security_check_items_count, filtered.size),
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
                items(filtered) { item ->
                    ChecklistCard(
                        item = item,
                        onClick = { onChecklistClick(item.status.toLegendKey()) }
                    )
                }
            }
        }
    }
    if (showCreatedInMonthDialog) {
        AlertDialog(
            onDismissRequest = { showCreatedInMonthDialog = false },
            confirmButton = {
                TextButton(onClick = { showCreatedInMonthDialog = false }) {
                    Text(text = stringResource(R.string.my_security_check_exists_dialog_ok))
                }
            },
            title = {
                Text(text = stringResource(R.string.my_security_check_exists_dialog_title))
            },
            text = {
                Text(text = stringResource(R.string.my_security_check_exists_dialog_message))
            }
        )
    }
}

private fun hasChecklistInCurrentMonth(): Boolean {
    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val now = Calendar.getInstance()
    val currentMonth = now.get(Calendar.MONTH)
    val currentYear = now.get(Calendar.YEAR)
    return mySecurityItems.any { item ->
        runCatching { formatter.parse(item.date) }.getOrNull()?.let { parsed ->
            Calendar.getInstance().apply { time = parsed }.let { created ->
                created.get(Calendar.MONTH) == currentMonth &&
                    created.get(Calendar.YEAR) == currentYear
            }
        } == true
    }
}

@Composable
private fun Header(onMenuClick: () -> Unit) {
    Box(modifier = Modifier.fillMaxWidth()) {
        IconButton(onClick = onMenuClick, modifier = Modifier.align(Alignment.CenterStart)) {
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = stringResource(R.string.dashboard_cd_open_menu),
                tint = Color.White
            )
        }
        Text(
            text = stringResource(R.string.drawer_item_security_check),
            color = Color.White,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
private fun SearchBar(keyword: String, onKeywordChange: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(44.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(Color(0xFFF9FAFB))
            .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(14.dp))
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = null,
            tint = Color(0xFF6A7282),
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        BasicTextField(
            value = keyword,
            onValueChange = onKeywordChange,
            modifier = Modifier.weight(1f),
            singleLine = true,
            textStyle = TextStyle(color = Color(0xFF364153), fontSize = 14.sp),
            cursorBrush = SolidColor(Color(0xFF155DFC)),
            decorationBox = { inner ->
                if (keyword.isBlank()) {
                    Text(
                        text = stringResource(R.string.my_security_check_search_placeholder),
                        color = Color(0xFF99A1AF),
                        fontSize = 14.sp
                    )
                }
                inner()
            }
        )
        Icon(
            painter = painterResource(R.drawable.ic_filter),
            contentDescription = stringResource(R.string.my_security_check_filter_cd),
            tint = Color(0xFF6A7282),
            modifier = Modifier.size(18.dp)
        )
    }
}

@Composable
private fun ChecklistCard(item: MySecurityChecklistUi, onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick),
        color = Color.White,
        shape = RoundedCornerShape(16.dp),
        shadowElevation = 2.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(16.dp))
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top) {
                Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = item.title,
                            color = Color(0xFF091E42),
                            fontSize = 22.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "•", color = Color(0xFF6A7282), fontSize = 16.sp)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = item.category, color = Color(0xFF6A7282), fontSize = 14.sp)
                    }
                    Text(
                        text = item.name,
                        color = Color(0xFF364153),
                        fontSize = 14.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    tint = Color(0xFF6A7282)
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFFEFF6FF))
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(text = item.template, color = Color(0xFF155DFC), fontSize = 12.sp, fontWeight = FontWeight.Medium)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "ID: ${item.code}",
                    color = Color(0xFF6A7282),
                    fontSize = 12.sp
                )
            }

            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                StatusChip(status = item.status)
                Spacer(modifier = Modifier.weight(1f))
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = stringResource(item.dateLabel),
                        color = Color(0xFF6A7282),
                        fontSize = 12.sp
                    )
                    Text(
                        text = item.date,
                        color = Color(0xFF364153),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            if (item.approver != null) {
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Color(0xFFE5E7EB))
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFF3F4F6)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.shinhan_logo),
                            contentDescription = null,
                            tint = Color.Unspecified,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = if(item.status == MySecurityStatus.REJECT) stringResource(R.string.my_security_check_rejecter) else stringResource(R.string.my_security_check_approver),
                            color = Color(0xFF6A7282),
                            fontSize = 11.sp
                        )
                        Text(
                            text = item.approver,
                            color = Color(0xFF364153),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun StatusChip(status: MySecurityStatus) {
    val text: String
    val textColor: Color
    val bgColor: Color
    when (status) {
        MySecurityStatus.PENDING -> {
            text = "PENDING"
            textColor = Color(0xFFCA8000)
            bgColor = Color(0xFFFFEDD4)
        }
        MySecurityStatus.APPROVED -> {
            text = "APPROVED"
            textColor = Color(0xFF008236)
            bgColor = Color(0xFFDCFCE7)
        }
        MySecurityStatus.REJECT -> {
            text = "REJECT"
            textColor = Color(0xFFF10C00)
            bgColor = Color(0xFFFFC1C2)
        }
    }
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(999.dp))
            .background(bgColor)
            .padding(horizontal = 10.dp, vertical = 8.dp)
    ) {
        Text(
            text = text,
            color = textColor,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}
