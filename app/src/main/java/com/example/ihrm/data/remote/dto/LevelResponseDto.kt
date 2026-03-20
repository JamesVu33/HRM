package com.example.ihrm.data.remote.dto

import com.google.gson.annotations.SerializedName

/** Level from GET /levels or GET /levels/{id}. */
data class LevelResponseDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("code")
    val code: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("description")
    val description: String? = null,
    @SerializedName("status")
    val status: String? = null,
    @SerializedName("createdAt")
    val createdAt: String? = null,
    @SerializedName("updatedAt")
    val updatedAt: String? = null
)

fun LevelResponseDto.toLevelShortDto(): LevelShortDto =
    LevelShortDto(id = id, code = code, name = name, description = description)
