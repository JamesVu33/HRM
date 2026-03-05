package com.example.ihrm.data.remote.api

import com.example.ihrm.data.remote.dto.EmployeeDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface EmployeeApiService {
    @GET("employees")
    suspend fun getAllEmployees(): List<EmployeeDto>

    @GET("employees/{id}")
    suspend fun getEmployeeById(@Path("id") id: String): EmployeeDto

    @POST("employees")
    suspend fun createEmployee(@Body employee: EmployeeDto): EmployeeDto

    @PUT("employees/{id}")
    suspend fun updateEmployee(@Path("id") id: String, @Body employee: EmployeeDto): EmployeeDto

    @DELETE("employees/{id}")
    suspend fun deleteEmployee(@Path("id") id: String): Unit
}