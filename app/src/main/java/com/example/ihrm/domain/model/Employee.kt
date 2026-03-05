package com.example.ihrm.domain.model

data class Employee(
    val id: String,
    val name: String,
    val email: String,
    val phone: String,
    val department: String?,
    val position: String?,
    val hireDate: String?,
    val salary: Double?,
    val address: String?,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)