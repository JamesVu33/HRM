package com.example.ihrm.ui.calendar.management

import androidx.annotation.StringRes
import com.example.ihrm.R

/**
 * Leave categories used in Calendar Management filter + calendar dot colors (Figma-aligned).
 */
enum class LeaveFilterType(
    @StringRes val labelRes: Int,
    /** Packed ARGB for calendar dots (0xAARRGGBB as Long). */
    val dotArgb: Long,
    /** Accent / checkbox fill when selected. */
    val accentArgb: Long,
    /** Soft row background when selected. */
    val softRowBackgroundArgb: Long
) {
    ANNUAL(
        labelRes = R.string.calendar_mgmt_leave_type_annual,
        dotArgb = 0xFF2B7FFFL,
        accentArgb = 0xFF2B7FFFL,
        softRowBackgroundArgb = 0xFFE6F1FFL
    ),
    SICK(
        labelRes = R.string.calendar_mgmt_leave_type_sick,
        dotArgb = 0xFFFB2C36L,
        accentArgb = 0xFFFB2C36L,
        softRowBackgroundArgb = 0xFFFEF2F2L
    ),
    MATERNITY(
        labelRes = R.string.calendar_mgmt_leave_type_maternity,
        dotArgb = 0xFFF6339AL,
        accentArgb = 0xFFF6339AL,
        softRowBackgroundArgb = 0xFFFCE7F3L
    ),
    UNPAID(
        labelRes = R.string.calendar_mgmt_leave_type_unpaid,
        dotArgb = 0xFF6A7282L,
        accentArgb = 0xFF6A7282L,
        softRowBackgroundArgb = 0xFFF3F4F6L
    ),
    PERSONAL(
        labelRes = R.string.calendar_mgmt_leave_type_personal,
        dotArgb = 0xFF00BBA7L,
        accentArgb = 0xFF00BBA7L,
        softRowBackgroundArgb = 0xFFD1FAE5L
    );

    companion object {
        val All: Set<LeaveFilterType> = entries.toSet()
    }
}
