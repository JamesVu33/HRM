package com.example.ihrm.domain.model

/**
 * Dữ liệu mẫu theo layout Figma (Manage employee / team list) — dùng khi DB/API chưa có bản ghi,
 * Preview, hoặc demo nội bộ.
 */
object EmployeeListSampleData {

    val uiModels: List<EmployeeUiModel> = listOf(
        EmployeeUiModel(
            employee = Employee(
                id = "1234567",
                name = "Alexander Wright",
                email = "alexander.wright@company.com",
                phone = "+1 (555) 123-4567",
                levelId = 1,
                department = "Design",
                position = "Designer",
                statusWorking = "null",
                salary = 0.25,
                address = "null",
                role = "TODO()",
                level = "TODO()",
                englishName =" TODO()",
                gender = "TODO()",
                personalId = "TODO()",
                idIssueDate = "TODO()",
                createdAt = 0L,
                updatedAt = 0L
            ),
            level = Level(id = 1, code = "S2", name = "Senior 2")
        ),
        EmployeeUiModel(
            employee = Employee(
                id = "7654321",
                name = "Samuel Chen",
                email = "samuel.chen@company.com",
                phone = "+1 (555) 987-6543",
                levelId = 2,
                department = "Engineering",
                position = "Developer",
                statusWorking = "null",
                salary = 0.25,
                address = "null",
                role = "TODO()",
                level = "TODO()",
                englishName =" TODO()",
                gender = "TODO()",
                personalId = "TODO()",
                idIssueDate = "TODO()",
                createdAt = 0L,
                updatedAt = 0L
            ),
            level = Level(id = 2, code = "J1", name = "Junior 1")
        ),
        EmployeeUiModel(
            employee = Employee(
                id = "5566778",
                name = "Maria Garcia",
                email = "maria.garcia@company.com",
                phone = "+1 (555) 246-8135",
                levelId = 3,
                department = "Product",
                position = "Product Manager",
                statusWorking = "null",
                salary = 0.25,
                address = "null",
                role = "TODO()",
                level = "TODO()",
                englishName =" TODO()",
                gender = "TODO()",
                personalId = "TODO()",
                idIssueDate = "TODO()",
                createdAt = 0L,
                updatedAt = 0L
            ),
            level = Level(id = 3, code = "M1", name = "Manager 1")
        )
    )
}
