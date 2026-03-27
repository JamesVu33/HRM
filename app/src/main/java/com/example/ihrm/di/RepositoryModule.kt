package com.example.ihrm.di

import com.example.ihrm.data.repository.AuthRepositoryImpl
import com.example.ihrm.data.repository.EmployeeRepositoryImpl
import com.example.ihrm.data.repository.LanguageRepositoryImpl
import com.example.ihrm.domain.repository.AuthRepository
import com.example.ihrm.domain.repository.EmployeeRepository
import com.example.ihrm.domain.repository.LanguageRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindEmployeeRepository(
        employeeRepositoryImpl: EmployeeRepositoryImpl
    ): EmployeeRepository

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindLangRepository(
        authRepositoryImpl: LanguageRepositoryImpl
    ): LanguageRepository
}