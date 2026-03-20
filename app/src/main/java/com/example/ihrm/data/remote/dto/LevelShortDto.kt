package com.example.ihrm.data.remote.dto

import com.google.gson.annotations.SerializedName

data class LevelShortDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("code")
    val code: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("description")
    val description: String? = null
)
