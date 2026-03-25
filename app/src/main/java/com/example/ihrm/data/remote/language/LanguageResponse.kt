package com.example.ihrm.data.remote.language

import com.google.gson.annotations.SerializedName


enum class LanguageStatus {
    ACTIVE, DEPRECATED, DISABLED
}

data class LanguageResponse(
    @SerializedName("id")
    val id: String,

    @SerializedName("key")
    val key: String,

    @SerializedName("namespace")
    val namespace: String,

    @SerializedName("valueVi")
    val valueVi: String,

    @SerializedName("valueEn")
    val valueEn: String,

    @SerializedName("valueKr")
    val valueKr: String,

    @SerializedName("status")
    val status: LanguageStatus,
)