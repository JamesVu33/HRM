package com.example.ihrm.ui.common.bottomsheet

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ihrm.R
import com.example.ihrm.ui.localization.tr
import java.util.Calendar

/** Chỉ cho phép ngày không sau hôm nay (DOB, ngày cấp, filter security checks). */
fun hrmSelectableDatesUpToToday(): SelectableDates = object : SelectableDates {
    override fun isSelectableDate(utcTimeMillis: Long): Boolean =
        utcTimeMillis <= System.currentTimeMillis()

    override fun isSelectableYear(year: Int): Boolean =
        year <= Calendar.getInstance().get(Calendar.YEAR)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HrmDatePickerDialog(
    initialMillis: Long?,
    @StringRes titleRes: Int,
    @StringRes headlineRes: Int,
    @StringRes confirmRes: Int,
    @StringRes cancelRes: Int,
    yearRange: IntRange,
    selectableDates: SelectableDates,
    onDateSelected: (Long) -> Unit,
    onDismiss: () -> Unit,
) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = initialMillis ?: System.currentTimeMillis(),
        yearRange = yearRange,
        selectableDates = selectableDates,
    )

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    datePickerState.selectedDateMillis?.let { onDateSelected(it) }
                },
                contentPadding = PaddingValues(horizontal = 8.dp),
            ) {
                Text(tr(confirmRes), color = FilterPrimaryBlue, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                contentPadding = PaddingValues(horizontal = 8.dp),
            ) {
                Text(tr(cancelRes), color = FilterSheetMuted)
            }
        },
        shape = RoundedCornerShape(28.dp),
        colors = DatePickerDefaults.colors(containerColor = Color.White)
    ) {
        DatePicker(
            state = datePickerState,
            title = {
                Text(
                    text = tr(titleRes),
                    modifier = Modifier.padding(start = 24.dp, top = 24.dp),
                    style = MaterialTheme.typography.labelMedium,
                    color = FilterSheetMuted
                )
            },
            headline = {
                Text(
                    text = tr(headlineRes),
                    modifier = Modifier.padding(start = 24.dp, bottom = 8.dp),
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.Black
                )
            },
            colors = DatePickerDefaults.colors(
                selectedDayContainerColor = FilterPrimaryBlue,
                selectedDayContentColor = Color.White,
                todayContentColor = FilterPrimaryBlue,
                todayDateBorderColor = FilterPrimaryBlue,
                navigationContentColor = Color.DarkGray,
                containerColor = Color.White,
                disabledDayContentColor = Color.LightGray,
                disabledSelectedDayContainerColor = Color.Transparent
            ),
            showModeToggle = false
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SecurityChecksDatePickerDialog(
    initialMillis: Long?,
    onDateSelected: (Long) -> Unit,
    onDismiss: () -> Unit,
) {
    val y = Calendar.getInstance().get(Calendar.YEAR)
    HrmDatePickerDialog(
        initialMillis = initialMillis,
        titleRes = R.string.security_checks_date_picker_title,
        headlineRes = R.string.security_checks_date_picker_headline,
        confirmRes = R.string.date_picker_confirm,
        cancelRes = R.string.date_picker_cancel,
        yearRange = (y - 10)..(y + 1),
        selectableDates = hrmSelectableDatesUpToToday(),
        onDateSelected = onDateSelected,
        onDismiss = onDismiss,
    )
}
