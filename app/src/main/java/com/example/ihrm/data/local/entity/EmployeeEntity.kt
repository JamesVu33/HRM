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
    val department: String?,
    val position: String?,
    val hireDate: String?,
    val salary: Double?,
    val address: String?,
    val createdAt: Long,
    val updatedAt: Long
)