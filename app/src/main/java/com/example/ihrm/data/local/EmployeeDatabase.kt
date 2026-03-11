package com.example.ihrm.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.ihrm.data.local.dao.EmployeeDao
import com.example.ihrm.data.local.entity.EmployeeEntity

@Database(
    entities = [EmployeeEntity::class],
    version = 2,
    exportSchema = false
)
abstract class EmployeeDatabase : RoomDatabase() {
    abstract fun employeeDao(): EmployeeDao
}