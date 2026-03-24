package com.example.ihrm.domain.model

data class Employee(
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
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)


private fun mockTeamEmployees(
    leadName: String,
    leadEmail: String,
    leadPhone: String,
    dept: String
): List<Employee> = listOf(
    Employee(
        id = "mock-emp-1",
        name = leadName,
        email = leadEmail,
        phone = leadPhone,
        department = dept,
        position = "Engineering Lead",
        hireDate = "2024-01-01",
        salary = null,
        address = null
    ),
    Employee(
        id = "mock-emp-2",
        name = "Alex Tran",
        email = "alex.tran@company.com",
        phone = "0909888777",
        department = dept,
        position = "Developer",
        hireDate = "2024-06-15",
        salary = null,
        address = null
    ),
    Employee(
        id = "mock-emp-3",
        name = "Minh Nguyen",
        email = "minh.nguyen@company.com",
        phone = "0909111222",
        department = "HR",
        position = "HR Partner",
        hireDate = "2023-11-01",
        salary = null,
        address = null
    )
)