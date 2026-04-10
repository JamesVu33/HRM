package com.example.ihrm.ui.security.mysecurity

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.RadioButtonDefaults
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ihrm.R
import com.example.ihrm.ui.common.bottomsheet.FilterPrimaryBlue
import com.example.ihrm.ui.common.bottomsheet.FilterSheetMuted
import com.example.ihrm.ui.theme.DashboardFigmaInk
import com.example.ihrm.ui.theme.InterFontFamily
import com.example.ihrm.ui.localization.tr
import com.example.ihrm.util.singleClick
import java.util.Calendar

private val FilterSheetInk = DashboardFigmaInk
private val FilterSheetBorder = Color(0xFFE5E7EB)
private val FilterSheetFieldBg = Color(0xFFF9FAFB)

@Composable
fun MySecurityFilterBottomSheet(
    visible: Boolean,
    draft: MySecurityListFilters,
    onDraftChange: (MySecurityListFilters) -> Unit,
    onDismiss: () -> Unit,
    onClearAll: () -> Unit,
    onApply: () -> Unit,
) {
    if (!visible) return

    var showYearPicker by remember { mutableStateOf(false) }
    val currentYear = remember { Calendar.getInstance().get(Calendar.YEAR) }
    val yearOptions = remember(currentYear) {
        (currentYear downTo (currentYear - 20)).toList()
    }

    val yearDisplayText = draft.year?.toString()
        ?: tr(R.string.my_security_filter_year_all)

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnClickOutside = true,
            dismissOnBackPress = true,
            usePlatformDefaultWidth = false,
        ),
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 520.dp)
                    .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
                    .background(Color.White)
                    .navigationBarsPadding(),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Box(
                        modifier = Modifier
                            .width(36.dp)
                            .height(5.dp)
                            .clip(RoundedCornerShape(50))
                            .background(Color(0xFFD1D5DB)),
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = tr(R.string.security_checks_filters_title),
                        style = TextStyle(
                            fontFamily = InterFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 18.sp,
                            color = FilterSheetInk,
                        ),
                        modifier = Modifier.padding(start = 16.dp),
                    )
                    IconButton(onClick = onDismiss.singleClick()) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = tr(R.string.security_checks_filters_close_cd),
                            tint = FilterSheetMuted,
                        )
                    }
                }

                HorizontalDivider(color = Color(0xFFF3F4F6))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 320.dp)
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 24.dp, vertical = 16.dp),
                ) {
                    MySecurityFilterSectionLabel(tr(R.string.my_security_filter_status))
                    Spacer(modifier = Modifier.height(8.dp))
                    MySecurityStatusRadioRow(
                        label = tr(R.string.security_checks_filters_search_all),
                        selected = draft.status == MySecurityListStatusFilter.ALL,
                        onSelect = { onDraftChange(draft.copy(status = MySecurityListStatusFilter.ALL)) },
                    )
                    MySecurityStatusRadioRow(
                        label = tr(R.string.security_checks_submitted),
                        selected = draft.status == MySecurityListStatusFilter.SUBMITTED,
                        onSelect = { onDraftChange(draft.copy(status = MySecurityListStatusFilter.SUBMITTED)) },
                    )
                    MySecurityStatusRadioRow(
                        label = tr(R.string.security_checks_status_rejected),
                        selected = draft.status == MySecurityListStatusFilter.REJECTED,
                        onSelect = { onDraftChange(draft.copy(status = MySecurityListStatusFilter.REJECTED)) },
                    )
                    MySecurityStatusRadioRow(
                        label = tr(R.string.security_checks_approved),
                        selected = draft.status == MySecurityListStatusFilter.APPROVED,
                        onSelect = { onDraftChange(draft.copy(status = MySecurityListStatusFilter.APPROVED)) },
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    MySecurityFilterSectionLabel(tr(R.string.my_security_filter_year))
                    Spacer(modifier = Modifier.height(8.dp))

                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .border(1.dp, FilterSheetBorder, RoundedCornerShape(14.dp))
                            .clickable(onClick = { showYearPicker = true }.singleClick()),
                        color = FilterSheetFieldBg,
                        shape = RoundedCornerShape(14.dp),
                    ) {
                        Text(
                            text = yearDisplayText,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 14.dp),
                            style = TextStyle(
                                fontFamily = InterFontFamily,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Medium,
                                color = FilterSheetInk,
                            ),
                        )
                    }
                }

                HorizontalDivider(color = Color(0xFFF3F4F6))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    OutlinedButton(
                        onClick = onClearAll.singleClick(),
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = FilterSheetInk),
                    ) {
                        Text(
                            text = tr(R.string.security_checks_filters_clear_all),
                            fontFamily = InterFontFamily,
                            fontWeight = FontWeight.SemiBold,
                        )
                    }
                    Button(
                        onClick = onApply.singleClick(),
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = FilterPrimaryBlue),
                    ) {
                        Text(
                            text = tr(R.string.security_checks_filters_apply),
                            fontFamily = InterFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White,
                        )
                    }
                }
            }
        }
    }

    if (showYearPicker) {
        Dialog(
            onDismissRequest = { showYearPicker = false },
            properties = DialogProperties(
                dismissOnClickOutside = true,
                dismissOnBackPress = true,
                usePlatformDefaultWidth = false,
            ),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.35f))
                    .clickable { showYearPicker = false },
                contentAlignment = Alignment.Center,
            ) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White,
                ) {
                    LazyColumn(modifier = Modifier.heightIn(max = 360.dp)) {
                        item {
                            Text(
                                text = tr(R.string.my_security_filter_year_all),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        onDraftChange(draft.copy(year = null))
                                        showYearPicker = false
                                    }
                                    .padding(16.dp),
                                style = TextStyle(
                                    fontFamily = InterFontFamily,
                                    fontSize = 16.sp,
                                    color = FilterSheetInk,
                                ),
                            )
                        }
                        items(yearOptions, key = { it }) { y ->
                            Text(
                                text = y.toString(),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        onDraftChange(draft.copy(year = y))
                                        showYearPicker = false
                                    }
                                    .padding(16.dp),
                                style = TextStyle(
                                    fontFamily = InterFontFamily,
                                    fontSize = 16.sp,
                                    color = FilterSheetInk,
                                ),
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MySecurityFilterSectionLabel(text: String) {
    Text(
        text = text,
        style = TextStyle(
            fontFamily = InterFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            color = FilterSheetMuted,
        ),
    )
}

@Composable
private fun MySecurityStatusRadioRow(
    label: String,
    selected: Boolean,
    onSelect: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onSelect.singleClick())
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        RadioButton(
            selected = selected,
            onClick = onSelect.singleClick(),
            colors = RadioButtonDefaults.colors(
                selectedColor = FilterPrimaryBlue,
            ),
        )
        Text(
            text = label,
            style = TextStyle(
                fontFamily = InterFontFamily,
                fontSize = 15.sp,
                color = FilterSheetInk,
            ),
        )
    }
}
