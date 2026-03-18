package com.example.ihrm.data.remote.dto

import com.google.gson.annotations.SerializedName

data class MetaDto(
    @SerializedName("page")
    val page: Int,
    @SerializedName("limit")
    val limit: Int,
    @SerializedName("total")
    val total: Int,
    @SerializedName("totalPages")
    val totalPages: Int
)
