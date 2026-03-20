package com.example.ihrm.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.ihrm.data.local.EmployeeDatabase
import com.example.ihrm.data.local.dao.EmployeeDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE employees ADD COLUMN englishName TEXT")
        db.execSQL("ALTER TABLE employees ADD COLUMN gender TEXT")
        db.execSQL("ALTER TABLE employees ADD COLUMN personalId TEXT")
        db.execSQL("ALTER TABLE employees ADD COLUMN idIssueDate TEXT")
    }
}

private val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE employees ADD COLUMN levelId INTEGER")
    }
}

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
        )
            .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
            .addCallback(SeedDataCallback())
            .build()
    }

    @Provides
    @Singleton
    fun provideEmployeeDao(database: EmployeeDatabase): EmployeeDao {
        return database.employeeDao()
    }
}

/**
 * Inserts 10 seed employees when the database is first created (onCreate)
 * or when it is opened with an empty table (onOpen), so existing installs get seed data too.
 */
private class SeedDataCallback : RoomDatabase.Callback() {
    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        insertSeedEmployees(db)
    }

    override fun onOpen(db: SupportSQLiteDatabase) {
        super.onOpen(db)
        val cursor = db.query(SimpleSQLiteQuery("SELECT COUNT(*) FROM employees"))
        cursor.use {
            if (it.moveToFirst() && it.getInt(0) == 0) {
                insertSeedEmployees(db)
            }
        }
    }

    private fun insertSeedEmployees(db: SupportSQLiteDatabase) {
        val now = System.currentTimeMillis()
        val employees = listOf(
            SeedEmp("emp_001", "Alexander Wright", "alexander.wright@company.com", "Designer", "S1"),
            SeedEmp("emp_002", "Nguyen Van A", "nguyen.vana@company.com", "Developer", "S1"),
            SeedEmp("emp_003", "Sarah Chen", "sarah.chen@company.com", "Analyst", "J1"),
            SeedEmp("emp_004", "James Wilson", "james.wilson@company.com", "HR", "S2"),
            SeedEmp("emp_005", "Maria Garcia", "maria.garcia@company.com", "Designer", "S1"),
            SeedEmp("emp_006", "David Kim", "david.kim@company.com", "Developer", "J1"),
            SeedEmp("emp_007", "Emily Brown", "emily.brown@company.com", "Manager", "S2"),
            SeedEmp("emp_008", "Michael Lee", "michael.lee@company.com", "Analyst", "S1"),
            SeedEmp("emp_009", "Lisa Anderson", "lisa.anderson@company.com", "HR", "J1"),
            SeedEmp("emp_010", "Robert Taylor", "robert.taylor@company.com", "Developer", "S2")
        )
        employees.forEachIndexed { index, e ->
            val phone = "012345678$index"
            db.execSQL(
                """
                INSERT OR REPLACE INTO employees (id, name, email, phone, levelId, department, position, hireDate, salary, address, englishName, gender, personalId, idIssueDate, createdAt, updatedAt)
                VALUES ('${e.id}', '${e.name}', '${e.email}', '$phone', NULL, '${e.dept}', '${e.position}', NULL, NULL, NULL, NULL, NULL, NULL, NULL, $now, $now)
                """.trimIndent()
            )
        }
    }
}

private data class SeedEmp(val id: String, val name: String, val email: String, val position: String, val dept: String)