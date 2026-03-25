package com.example.ihrm.ui.calendar.management

import java.time.LocalDate
import java.time.YearMonth

/**
 * Column index for the 1st of [month] when the week starts on Sunday (S M T W T F S).
 * Sunday = 0, Monday = 1, … Saturday = 6.
 */
internal fun firstDaySundayColumnIndex(year: Int, month: Int): Int {
    val d = LocalDate.of(year, month, 1)
    val iso = d.dayOfWeek.value // ISO: Mon=1 … Sun=7
    return iso % 7
}

internal fun daysInMonth(year: Int, month: Int): Int =
    YearMonth.of(year, month).lengthOfMonth()

/**
 * Builds a fixed 6×7 grid (42 slots). Empty slots use [CalendarGridSlot.Pad].
 */
internal fun buildCalendarGrid42(
    year: Int,
    month: Int,
    selectedDayOfMonth: Int,
    dotArgbByDay: Map<Int, List<Long>>
): List<CalendarGridSlot> {
    val dim = daysInMonth(year, month)
    val offset = firstDaySundayColumnIndex(year, month)
    val out = ArrayList<CalendarGridSlot>(42)
    repeat(42) { idx ->
        val day = idx - offset + 1
        if (day < 1 || day > dim) {
            out += CalendarGridSlot.Pad
        } else {
            val dots = dotArgbByDay[day].orEmpty()
            val selected = day == selectedDayOfMonth
            val mutedBg = dots.isNotEmpty() && !selected
            out += CalendarGridSlot.Day(
                dayOfMonth = day,
                selected = selected,
                dotArgb = dots,
                mutedBackground = mutedBg
            )
        }
    }
    return out
}

internal sealed class CalendarGridSlot {
    data object Pad : CalendarGridSlot()

    data class Day(
        val dayOfMonth: Int,
        val selected: Boolean,
        val dotArgb: List<Long>,
        val mutedBackground: Boolean
    ) : CalendarGridSlot()
}
