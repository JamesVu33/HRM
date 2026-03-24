package com.example.ihrm.data.remote.base

import com.google.gson.annotations.SerializedName

data class ErrorFieldResponse(
    @SerializedName("key")
    val key: String,
    @SerializedName("params")
    val params: Map<String, Any?>? = null
)