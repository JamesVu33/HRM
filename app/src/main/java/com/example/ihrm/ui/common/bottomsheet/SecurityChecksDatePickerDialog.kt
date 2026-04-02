package com.example.ihrm.ui.common.bottomsheet

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
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SecurityChecksDatePickerDialog(
    initialMillis: Long?,
    onDateSelected: (Long) -> Unit,
    onDismiss: () -> Unit,
) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = initialMillis ?: System.currentTimeMillis(),
        yearRange = (Calendar.getInstance().get(Calendar.YEAR) - 10)..(Calendar.getInstance()
            .get(Calendar.YEAR) + 1),
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                return utcTimeMillis <= System.currentTimeMillis()
            }

            override fun isSelectableYear(year: Int): Boolean {
                return year <= Calendar.getInstance().get(Calendar.YEAR)
            }
        }
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
                Text("Chọn", color = FilterPrimaryBlue, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                contentPadding = PaddingValues(horizontal = 8.dp),
            ) {
                Text("Hủy", color = FilterSheetMuted)
            }
        },
        shape = RoundedCornerShape(28.dp),
        colors = DatePickerDefaults.colors(containerColor = Color.White)
    ) {
        DatePicker(
            state = datePickerState,
            title = {
                Text(
                    text = "CHỌN NGÀY KIỂM TRA",
                    modifier = Modifier.padding(start = 24.dp, top = 24.dp),
                    style = MaterialTheme.typography.labelMedium,
                    color = FilterSheetMuted
                )
            },
            headline = {
                Text(
                    text = "Lịch trình",
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