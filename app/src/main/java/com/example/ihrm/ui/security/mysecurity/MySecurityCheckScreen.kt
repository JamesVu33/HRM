package com.example.ihrm.ui.security.mysecurity

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ihrm.R
import com.example.ihrm.domain.usecase.securities.SecurityStatus
import com.example.ihrm.ui.common.BaseHRMCompose
import com.example.ihrm.ui.common.header.BaseHeader
import com.example.ihrm.ui.theme.InterFontFamily
import com.example.ihrm.util.DashboardBrush
import com.example.ihrm.util.LabelTextStyle13MediumGrey
import com.example.ihrm.util.LabelTextStyle13RegularGrey
import com.example.ihrm.util.LabelTextStyle13SemiBold
import com.example.ihrm.util.LabelTextStyle14RegularBlack
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import com.example.ihrm.ui.localization.tr

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

private val mySecurityItems = listOf(
    MySecurityChecklistUi(
        "#3",
        "GCD",
        "Nguyen Van A",
        "temp-v5",
        "2503001",
        MySecurityStatus.PENDING,
        R.string.my_security_check_submitted,
        "03/01/2025"
    ),
    MySecurityChecklistUi(
        "#4",
        "GCD",
        "Nguyen Van A",
        "temp-v5",
        "2503001",
        MySecurityStatus.PENDING,
        R.string.my_security_check_submitted,
        "03/01/2025"
    ),
    MySecurityChecklistUi(
        "#5",
        "GCD",
        "Nguyen Van A",
        "temp-v5",
        "2503001",
        MySecurityStatus.PENDING,
        R.string.my_security_check_submitted,
        "03/01/2025"
    ),
    MySecurityChecklistUi(
        "#1",
        "GCD",
        "Nguyen Van A",
        "temp-v5",
        "2503001",
        MySecurityStatus.APPROVED,
        R.string.my_security_check_updated,
        "12/01/2024",
        "Nguyen Van A"
    ),
    MySecurityChecklistUi(
        "#2",
        "GCD",
        "Nguyen Van A",
        "temp-v5",
        "2503001",
        MySecurityStatus.REJECT,
        R.string.my_security_check_updated,
        "12/01/2024",
        "Nguyen Van A"
    )
)

@Composable
fun MySecurityCheckScreen(
    onMenuClick: () -> Unit,
    onChecklistClick: (String) -> Unit = {},
    onCreateChecklistClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: MySecurityCheckViewModel = hiltViewModel()
) {
    BaseHRMCompose(
        content = {
            MySecurityCheckScreenContent(
                onMenuClick = onMenuClick,
                onChecklistClick = onChecklistClick,
                onCreateChecklistClick = onCreateChecklistClick,
                modifier = modifier,
                viewModel = viewModel
            )
        },
        onErrorAlertClose = onMenuClick,
        viewmodel = viewModel
    )
}

@Composable
fun MySecurityCheckScreenContent(
    onMenuClick: () -> Unit,
    onChecklistClick: (String) -> Unit = {},
    onCreateChecklistClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: MySecurityCheckViewModel
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
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
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
                    contentDescription = tr(R.string.my_security_check_add_cd),
                    tint = White
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(brush = DashboardBrush.BaseBackground)
                .padding(horizontal = 16.dp)
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
            ) {

                Spacer(modifier = Modifier.height(12.dp))
                BaseHeader(
                    modifier = Modifier
                        .padding(paddingValues)
                        .statusBarsPadding(),
                    title = tr(R.string.drawer_item_security_check),
                    showNavigationIcon = true,
                    onNavigationClick = onMenuClick,
                    containerColor = Color.Transparent,
                    navigationIcon = {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = tr(R.string.dashboard_cd_open_menu),
                            tint = White
                        )
                    }
                )
                Spacer(modifier = Modifier.height(12.dp))
                SearchBar(
                    keyword = keyword,
                    onKeywordChange = { keyword = it }
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    Text(
                        text = tr(
                            R.string.my_security_check_items_count,
                            filtered.size
                        ),
                        color = White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    bottom = paddingValues.calculateBottomPadding()
                ),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                items(uiState) { item ->
                    ChecklistCard(
                        item = item,
                        onClick = { onChecklistClick(item.id) }
                    )
                }
                item { Spacer(modifier = Modifier.height(8.dp)) }
            }
        }
    }
    if (showCreatedInMonthDialog) {
        AlertDialog(
            onDismissRequest = { showCreatedInMonthDialog = false },
            confirmButton = {
                TextButton(onClick = { showCreatedInMonthDialog = false }) {
                    Text(text = tr(R.string.my_security_check_exists_dialog_ok))
                }
            },
            title = {
                Text(text = tr(R.string.my_security_check_exists_dialog_title))
            },
            text = {
                Text(text = tr(R.string.my_security_check_exists_dialog_message))
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
                        text = tr(R.string.my_security_check_search_placeholder),
                        color = Color(0xFF99A1AF),
                        fontSize = 14.sp
                    )
                }
                inner()
            }
        )
        Icon(
            painter = painterResource(R.drawable.ic_filter),
            contentDescription = tr(R.string.my_security_check_filter_cd),
            tint = Color(0xFF6A7282),
            modifier = Modifier.size(18.dp)
        )
    }
}

@Composable
private fun ChecklistCard(
    item: MySecurityCheckUiState,
    onClick: () -> Unit
) {
    val status = SecurityStatus.fromKey(item.status?.key ?: "")

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick),
        color = White,
        shape = RoundedCornerShape(16.dp),
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "ID: ${item.employeeId}",
                    style = LabelTextStyle13RegularGrey
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = item.userName,
                    style = LabelTextStyle14RegularBlack
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = buildAnnotatedString {
                        withStyle(SpanStyle(color = Color(0xFF364153), fontSize = 13.sp)) {
                            append(tr(R.string.my_security_check_submitted))
                        }
                        append(" ")
                        withStyle(SpanStyle(color = Color(0xFF364153), fontWeight = FontWeight.Medium, fontSize = 13.sp)) {
                            append(item.submittedAt)
                        }
                    }
                )
            }
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.End,
            ) {
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
                        text = item.status.label,
                        color = item.status.chipTextColor,
                        style = LabelTextStyle13SemiBold
                    )
                }
                Spacer(modifier = Modifier.height(26.dp))
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Surface(
                        color = Color(0xFFEFF6FF),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.padding(all = 5.dp)
                    ) {
                        Text(
                            text = item.templateName,
                            color = Color(0xFF155DFC),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            style = LabelTextStyle13MediumGrey,
                            textAlign = TextAlign.Center
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
