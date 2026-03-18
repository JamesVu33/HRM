package com.example.ihrm.ui.navigation

sealed class Screen(val route: String) {
    data object Splash : Screen("splash")
    data object LoginTest : Screen("login_test")
    data object SignUp : Screen("signup")
    data object Dashboard : Screen("dashboard")
    data object EmployeeList : Screen("employee_list")
    data object EmployeeDetail : Screen("employee_detail/{employeeId}") {
        fun createRoute(employeeId: String) = "employee_detail/$employeeId"
    }
    data object AddEmployee : Screen("add_employee")
    data object EditEmployee : Screen("edit_employee/{employeeId}") {
        fun createRoute(employeeId: String) = "edit_employee/$employeeId"
    }
}