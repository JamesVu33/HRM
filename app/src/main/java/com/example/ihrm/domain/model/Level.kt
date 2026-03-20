package com.example.ihrm.domain.model

/** Domain model for level (from GET /levels/{id}). */
data class Level(
    val id: Int,
    val code: String,
    val name: String,
    val description: String? = null
)
