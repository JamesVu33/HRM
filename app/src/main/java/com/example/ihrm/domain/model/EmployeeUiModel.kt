package com.example.ihrm.domain.model

/**
 * UI model: Employee + Level (gộp từ getAllEmployees + getLevelById).
 * Dùng cho màn danh sách nhân viên để hiển thị badge/level mà không gọi API trùng.
 */
data class EmployeeUiModel(
    val employee: Employee,
    val level: Level? = null
) {
    /** Level code cho badge (e.g. S1, J2); null nếu chưa load hoặc không có level. */
    val levelCode: String? get() = level?.code
}
