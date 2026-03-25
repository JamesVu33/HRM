package com.example.ihrm.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.ihrm.data.local.HRMDatabase
import com.example.ihrm.data.local.dao.EmployeeDao
import com.example.ihrm.data.local.dao.LanguageDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    private const val DATABASE_NAME = "hrm_database"

    @Provides
    @Singleton
    fun provideEmployeeDatabase(@ApplicationContext context: Context): HRMDatabase {
        return Room.databaseBuilder(
            context = context,
            klass = HRMDatabase::class.java,
            name = DATABASE_NAME
        )
            .build()
    }

    @Provides
    @Singleton
    fun provideEmployeeDao(database: HRMDatabase): EmployeeDao {
        return database.employeeDao()
    }

    @Provides
    @Singleton
    fun provideLanguageDao(database: HRMDatabase): LanguageDao {
        return database.languageDao()
    }
}