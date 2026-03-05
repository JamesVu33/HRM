package com.example.ihrm.di

import android.content.Context
import androidx.room.Room
import com.example.ihrm.data.local.EmployeeDatabase
import com.example.ihrm.data.local.dao.EmployeeDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideEmployeeDatabase(@ApplicationContext context: Context): EmployeeDatabase {
        return Room.databaseBuilder(
            context,
            EmployeeDatabase::class.java,
            "employee_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideEmployeeDao(database: EmployeeDatabase): EmployeeDao {
        return database.employeeDao()
    }
}