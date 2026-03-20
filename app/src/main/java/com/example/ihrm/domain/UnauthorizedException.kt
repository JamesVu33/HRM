package com.example.ihrm.domain

/**
 * API trả về 401 Unauthorized (token hết hạn hoặc không hợp lệ).
 * ViewModel dùng để hiển thị thông báo thân thiện thay vì raw "401".
 */
class UnauthorizedException : Exception("Unauthorized")
