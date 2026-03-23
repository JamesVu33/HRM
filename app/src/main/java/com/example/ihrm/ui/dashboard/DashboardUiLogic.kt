package com.example.ihrm.ui.dashboard

/**
 * Share of rejected items in the monthly security breakdown (Figma progress emphasis on rejected).
 */
internal fun securityRejectedShare(
    approved: Int,
    rechecking: Int,
    rejected: Int
): Float {
    val total = approved + rechecking + rejected
    if (total <= 0) return 0f
    return rejected.toFloat() / total.toFloat()
}
