package com.example.ihrm.ui.calendar.management

/** ARGB packed as Long for [buildCalendarGrid42] dot colors (Figma 871:35255). */
internal object CalendarManagementDemoData {

    const val DEMO_STAT_VALUE: Int = 22

    private val DotBlue = 0xFF2B7FFF
    private val DotTeal = 0xFF00BBA7
    private val DotGray = 0xFF6A7282
    private val DotRed = 0xFFFB2C36
    private val DotPink = 0xFFF6339A

    /**
     * March 2026: which leave categories occur on each day (drives filter Apply).
     * Derived from the original multi-dot calendar semantics.
     */
    val march2026LeaveTypesByDay: Map<Int, Set<LeaveFilterType>> = buildMap {
        fun putDay(day: Int, vararg types: LeaveFilterType) {
            put(day, types.toSet())
        }
        putDay(2, LeaveFilterType.ANNUAL)
        putDay(4, LeaveFilterType.ANNUAL)
        putDay(6, LeaveFilterType.PERSONAL)
        putDay(8, LeaveFilterType.ANNUAL)
        putDay(9, LeaveFilterType.ANNUAL)
        putDay(10, LeaveFilterType.ANNUAL)
        putDay(12, LeaveFilterType.UNPAID)
        putDay(13, LeaveFilterType.UNPAID)
        putDay(14, LeaveFilterType.UNPAID)
        putDay(15, LeaveFilterType.SICK, LeaveFilterType.MATERNITY)
        putDay(16, LeaveFilterType.SICK, LeaveFilterType.MATERNITY)
        putDay(17, LeaveFilterType.SICK, LeaveFilterType.MATERNITY)
        putDay(18, LeaveFilterType.SICK, LeaveFilterType.MATERNITY)
        putDay(19, LeaveFilterType.MATERNITY, LeaveFilterType.PERSONAL)
        putDay(20, LeaveFilterType.MATERNITY, LeaveFilterType.PERSONAL)
        putDay(21, LeaveFilterType.MATERNITY)
        putDay(22, LeaveFilterType.SICK, LeaveFilterType.MATERNITY)
        putDay(23, LeaveFilterType.MATERNITY)
        putDay(24, LeaveFilterType.MATERNITY, LeaveFilterType.ANNUAL)
        putDay(25, LeaveFilterType.MATERNITY, LeaveFilterType.ANNUAL)
        putDay(26, LeaveFilterType.MATERNITY, LeaveFilterType.ANNUAL)
        putDay(27, LeaveFilterType.MATERNITY)
        putDay(28, LeaveFilterType.MATERNITY)
        putDay(29, LeaveFilterType.MATERNITY)
        putDay(30, LeaveFilterType.MATERNITY)
        putDay(31, LeaveFilterType.MATERNITY)
    }

    /**
     * March 2026 leave markers aligned with Figma calendar dots.
     */
    val march2026DotArgbByDay: Map<Int, List<Long>> = buildMap {
        put(2, listOf(DotBlue))
        put(4, listOf(DotBlue))
        put(6, listOf(DotTeal))
        put(8, listOf(DotBlue))
        put(9, listOf(DotBlue))
        put(10, listOf(DotBlue))
        put(12, listOf(DotGray))
        put(13, listOf(DotGray))
        put(14, listOf(DotGray))
        put(15, listOf(DotRed, DotPink))
        put(16, listOf(DotRed, DotPink))
        put(17, listOf(DotRed, DotPink))
        put(18, listOf(DotRed, DotPink))
        put(19, listOf(DotPink, DotTeal))
        put(20, listOf(DotPink, DotTeal))
        put(21, listOf(DotPink))
        put(22, listOf(DotPink, DotRed, DotPink))
        put(23, listOf(DotPink, DotPink))
        put(24, listOf(DotPink, DotPink, DotBlue))
        put(25, listOf(DotPink, DotPink, DotBlue))
        put(26, listOf(DotPink, DotPink, DotBlue))
        put(27, listOf(DotPink, DotPink))
        put(28, listOf(DotPink, DotPink))
        put(29, listOf(DotPink))
        put(30, listOf(DotPink))
        put(31, listOf(DotPink))
    }

    fun avatarColorArgb(initialsKey: String): Long = when (initialsKey) {
        "SJ" -> DotBlue
        "CB" -> DotBlue
        "RT" -> DotRed
        "SM" -> DotPink
        else -> DotBlue
    }
}
