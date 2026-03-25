package com.example.ihrm.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.ihrm.data.remote.language.LanguageStatus

@Entity(tableName = "languages")
data class LanguageEntity(
    @PrimaryKey
    val id: String,
    val key: String,
    val namespace: String,
    val valueVi: String,
    val valueEn: String,
    val valueKr: String,
    val status: LanguageStatus
)
