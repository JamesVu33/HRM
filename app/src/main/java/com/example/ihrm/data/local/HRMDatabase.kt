package com.example.ihrm.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.ihrm.data.local.dao.EmployeeDao
import com.example.ihrm.data.local.dao.LanguageDao
import com.example.ihrm.data.local.entity.EmployeeEntity
import com.example.ihrm.data.local.entity.LanguageEntity

@Database(
    entities = [EmployeeEntity::class, LanguageEntity::class],
    version = 1,
    exportSchema = false
)
abstract class HRMDatabase : RoomDatabase() {
    abstract fun employeeDao(): EmployeeDao
    abstract fun languageDao(): LanguageDao
}