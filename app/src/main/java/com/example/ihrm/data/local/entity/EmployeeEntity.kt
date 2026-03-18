package com.example.ihrm.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "employees")
data class EmployeeEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val email: String,
    val phone: String,
    val levelId: Int? = null,
    val department: String?,
    val position: String?,
    val hireDate: String?,
    val salary: Double?,
    val address: String?,
    val englishName: String? = null,
    val gender: String? = null,
    val personalId: String? = null,
    val idIssueDate: String? = null,
    val createdAt: Long,
    val updatedAt: Long
)