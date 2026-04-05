package com.example.ihrm.data.remote.base

import com.example.ihrm.data.remote.dto.MetaDto

/**
 * Payload từ [ApiSuccessResponse] khi cần cả [data] và [meta] (phân trang).
 */
data class PaginatedApiData<T>(
    val data: T?,
    val meta: MetaDto?,
)
