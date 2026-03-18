package com.example.ihrm.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ErrorFieldDto(
    @SerializedName("key")
    val key: String,
    @SerializedName("params")
    val params: Map<String, Any?>? = null
)
