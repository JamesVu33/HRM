package com.example.ihrm.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ResponseListDto<T>(
    @SerializedName("statusCode")
    val statusCode: Int,
    @SerializedName("message")
    val message: String? = null,
    @SerializedName("data")
    val data: List<T>,
    @SerializedName("meta")
    val meta: MetaDto? = null
)
