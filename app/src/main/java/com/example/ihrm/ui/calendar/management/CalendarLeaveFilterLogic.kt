package com.example.ihrm.ui.calendar.management

/**
 * Stable order for up to 3 dots under a day (most important / common first).
 */
private val DotDisplayOrder: List<LeaveFilterType> = listOf(
    LeaveFilterType.ANNUAL,
    LeaveFilterType.PERSONAL,
    LeaveFilterType.SICK,
    LeaveFilterType.MATERNITY,
    LeaveFilterType.UNPAID
)

private const val MaxDotsPerDay = 3

/**
 * Builds the calendar dot map for [applied] leave types only.
 * Days with no matching types are omitted (no dots, no muted cell).
 */
internal fun buildFilteredDotMapByDay(
    leaveTypesByDay: Map<Int, Set<LeaveFilterType>>,
    applied: Set<LeaveFilterType>
): Map<Int, List<Long>> {
    if (applied.isEmpty()) return emptyMap()
    val out = LinkedHashMap<Int, List<Long>>()
    leaveTypesByDay.forEach { (day, typesOnDay) ->
        val colors = DotDisplayOrder
            .filter { it in applied && it in typesOnDay }
            .map { it.dotArgb }
            .take(MaxDotsPerDay)
        if (colors.isNotEmpty()) {
            out[day] = colors
        }
    }
    return out
}
