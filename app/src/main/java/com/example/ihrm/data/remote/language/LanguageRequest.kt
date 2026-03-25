package com.example.ihrm.data.remote.language

import com.google.gson.annotations.SerializedName

/**
 * Request body for POST
 */
data class LanguageRequest(
    @SerializedName("page")
    val page: String = "1",
    @SerializedName("limit")
    val limit: String = "1000"
) {
    fun toMap(): Map<String, String> {
        return mapOf(
            "page" to page,
            "limit" to limit
        )
    }
}