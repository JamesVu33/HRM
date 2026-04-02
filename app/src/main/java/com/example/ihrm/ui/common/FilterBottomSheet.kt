package com.example.ihrm.ui.common

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ihrm.R
import com.example.ihrm.domain.model.SecurityGroups
import com.example.ihrm.ui.security.checks.SecurityChecksActiveFilters
import com.example.ihrm.ui.security.checks.SecurityChecksSearchByMode
import com.example.ihrm.ui.theme.DashboardFigmaInk
import com.example.ihrm.ui.theme.InterFontFamily
import com.example.ihrm.util.singleClick
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

private val FilterSheetInk = DashboardFigmaInk
private val FilterSheetMuted = Color(0xFF6B7280)
private val FilterSheetBorder = Color(0xFFE5E7EB)
private val FilterSheetFieldBg = Color(0xFFF9FAFB)
private val FilterPrimaryBlue = Color(0xFF2563EB)

private fun formatMillisToDisplay(millis: Long?): String {
    if (millis == null) return ""
    return try {
        val z = ZoneId.systemDefault()
        val date = Instant.ofEpochMilli(millis).atZone(z).toLocalDate()
        DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.getDefault()).format(date)
    } catch (_: Exception) {
        ""
    }
}

/**
 * Bottom sheet filter cho màn Security Checks: date range, search by, group + nút Clear / Apply.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SecurityChecksFilterBottomSheet(
    visible: Boolean,
    draft: SecurityChecksActiveFilters,
    onDraftChange: (SecurityChecksActiveFilters) -> Unit,
    groupOptions: List<SecurityGroups>,
    onDismiss: () -> Unit,
    onClearAll: () -> Unit,
    onApply: () -> Unit,
) {
    if (!visible) return

    var showFromPicker by remember { mutableStateOf(false) }
    var showToPicker by remember { mutableStateOf(false) }
    var groupMenuExpanded by remember { mutableStateOf(false) }

    val allGroupsLabel = stringResource(R.string.security_checks_filters_all_groups)
    val selectGroupHint = stringResource(R.string.security_checks_filters_select_group)

    val selectedGroupLabel = draft.groupId?.let { key ->
        groupOptions.firstOrNull { option ->
            option.code == key || option.name == key
        }?.let { option ->
            option.name?.takeIf { it.isNotBlank() } ?: option.code.orEmpty()
        }
    }.orEmpty()

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
                    .heightIn(max = 600.dp)
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
                        text = stringResource(R.string.security_checks_filters_title),
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
                            contentDescription = stringResource(R.string.security_checks_filters_close_cd),
                            tint = FilterSheetMuted,
                        )
                    }
                }

                HorizontalDivider(color = Color(0xFFF3F4F6))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 400.dp)
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 24.dp, vertical = 16.dp),
                ) {
                    FilterSectionLabel(stringResource(R.string.security_checks_filters_date_range))
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        DateFieldChip(
                            label = stringResource(R.string.security_checks_filters_from),
                            valueText = formatMillisToDisplay(draft.dateFromMillis)
                                .ifEmpty { stringResource(R.string.security_checks_filters_pick_date) },
                            isPlaceholder = draft.dateFromMillis == null,
                            modifier = Modifier.weight(1f),
                            onClick = { showFromPicker = true },
                        )
                        DateFieldChip(
                            label = stringResource(R.string.security_checks_filters_to),
                            valueText = formatMillisToDisplay(draft.dateToMillis)
                                .ifEmpty { stringResource(R.string.security_checks_filters_pick_date) },
                            isPlaceholder = draft.dateToMillis == null,
                            modifier = Modifier.weight(1f),
                            onClick = { showToPicker = true },
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                    FilterSectionLabel(stringResource(R.string.security_checks_filters_search_by))
                    Spacer(modifier = Modifier.height(8.dp))
                    SecurityChecksSearchByMode.entries.forEach { mode ->
                        val label = when (mode) {
                            SecurityChecksSearchByMode.ALL ->
                                stringResource(R.string.security_checks_filters_search_all)
                            SecurityChecksSearchByMode.EMPLOYEE_ID ->
                                stringResource(R.string.security_checks_filters_search_employee_id)
                            SecurityChecksSearchByMode.NAME ->
                                stringResource(R.string.security_checks_filters_search_name)
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .clickable {
                                    onDraftChange(draft.copy(searchBy = mode))
                                }
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            RadioButton(
                                selected = draft.searchBy == mode,
                                onClick = { onDraftChange(draft.copy(searchBy = mode)) },
                                colors = RadioButtonDefaults.colors(selectedColor = FilterPrimaryBlue),
                            )
                            Text(
                                text = label,
                                style = TextStyle(
                                    fontFamily = InterFontFamily,
                                    fontSize = 16.sp,
                                    color = FilterSheetInk,
                                ),
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))
                    FilterSectionLabel(stringResource(R.string.security_checks_filters_group))
                    Spacer(modifier = Modifier.height(8.dp))
                    ExposedDropdownMenuBox(
                        expanded = groupMenuExpanded,
                        onExpandedChange = { groupMenuExpanded = it },
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        OutlinedTextField(
                            value = selectedGroupLabel,
                            onValueChange = {},
                            readOnly = true,
                            placeholder = {
                                Text(
                                    text = selectGroupHint,
                                    color = FilterSheetMuted,
                                    fontSize = 15.sp,
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(),
                            shape = RoundedCornerShape(14.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = FilterSheetFieldBg,
                                unfocusedContainerColor = FilterSheetFieldBg,
                                focusedBorderColor = FilterSheetBorder,
                                unfocusedBorderColor = FilterSheetBorder,
                                unfocusedPlaceholderColor = FilterSheetMuted,
                            ),
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = groupMenuExpanded)
                            },
                        )
                        DropdownMenu(
                            expanded = groupMenuExpanded,
                            onDismissRequest = { groupMenuExpanded = false },
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.White, RoundedCornerShape(14.dp)),
                        ) {
                            DropdownMenuItem(
                                text = { Text(allGroupsLabel) },
                                onClick = {
                                    onDraftChange(draft.copy(groupId = null))
                                    groupMenuExpanded = false
                                },
                            )
                            groupOptions.forEach { option ->
                                val rowLabel = option.name?.takeIf { it.isNotBlank() }
                                    ?: option.code
                                    ?: ""
                                if (rowLabel.isNotBlank()) {
                                    DropdownMenuItem(
                                        text = { Text(rowLabel) },
                                        onClick = {
                                            val key = option.code?.takeIf { it.isNotBlank() }
                                                ?: option.name?.takeIf { it.isNotBlank() }
                                            onDraftChange(draft.copy(groupId = key))
                                            groupMenuExpanded = false
                                        },
                                    )
                                }
                            }
                        }
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
                            text = stringResource(R.string.security_checks_filters_clear_all),
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
                            text = stringResource(R.string.security_checks_filters_apply),
                            fontFamily = InterFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White,
                        )
                    }
                }
            }
        }
    }

    if (showFromPicker) {
        SecurityChecksDatePickerDialog(
            initialMillis = draft.dateFromMillis,
            onDateSelected = { ms ->
                onDraftChange(draft.copy(dateFromMillis = ms))
                showFromPicker = false
            },
            onDismiss = { showFromPicker = false },
        )
    }
    if (showToPicker) {
        SecurityChecksDatePickerDialog(
            initialMillis = draft.dateToMillis,
            onDateSelected = { ms ->
                onDraftChange(draft.copy(dateToMillis = ms))
                showToPicker = false
            },
            onDismiss = { showToPicker = false },
        )
    }
}

@Composable
private fun FilterSectionLabel(text: String) {
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
private fun DateFieldChip(
    label: String,
    valueText: String,
    isPlaceholder: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(
            text = label,
            style = TextStyle(
                fontFamily = InterFontFamily,
                fontSize = 12.sp,
                color = FilterSheetMuted,
            ),
        )
        Spacer(modifier = Modifier.height(6.dp))
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .border(1.dp, FilterSheetBorder, RoundedCornerShape(14.dp))
                .clickable(onClick = onClick.singleClick()),
            color = FilterSheetFieldBg,
            shape = RoundedCornerShape(14.dp),
        ) {
            Text(
                text = valueText,
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 14.dp),
                style = TextStyle(
                    fontFamily = InterFontFamily,
                    fontSize = 15.sp,
                    color = if (isPlaceholder) FilterSheetMuted else FilterSheetInk,
                ),
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SecurityChecksDatePickerDialog(
    initialMillis: Long?,
    onDateSelected: (Long) -> Unit,
    onDismiss: () -> Unit,
) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = initialMillis ?: System.currentTimeMillis(),
        yearRange = (Calendar.getInstance().get(Calendar.YEAR) - 10)..(Calendar.getInstance()
            .get(Calendar.YEAR) + 1),
    )
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                DatePicker(
                    state = datePickerState,
                    colors = DatePickerDefaults.colors(
                        selectedDayContainerColor = FilterPrimaryBlue,
                        selectedDayContentColor = Color.White,
                        todayContentColor = FilterPrimaryBlue,
                    ),
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                ) {
                    TextButton(onClick = onDismiss.singleClick()) {
                        Text(stringResource(android.R.string.cancel), color = FilterSheetMuted)
                    }
                    TextButton(
                        onClick = {
                            datePickerState.selectedDateMillis?.let { ms ->
                                onDateSelected(ms)
                            } ?: onDismiss()
                        }.singleClick(),
                    ) {
                        Text(stringResource(android.R.string.ok), color = FilterPrimaryBlue)
                    }
                }
            }
        }
    }
}
