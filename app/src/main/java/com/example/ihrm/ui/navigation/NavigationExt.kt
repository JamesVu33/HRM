package com.example.ihrm.ui.navigation

import androidx.navigation.NavHostController

/**
 * Pop một bước chỉ khi còn entry trước đó.
 * Tránh stress/auto-click gọi [NavHostController.popBackStack] liên tiếp làm hết stack và NavHost hiển thị trống.
 */
fun NavHostController.popBackStackIfPossible(): Boolean {
    if (previousBackStackEntry == null) return false
    return popBackStack()
}
