package com.example.ihrm.di

import com.example.ihrm.data.repository.AuthRepositoryImpl
import com.example.ihrm.data.repository.EmployeeRepositoryImpl
import com.example.ihrm.data.repository.LanguageRepositoryImpl
import com.example.ihrm.data.repository.MyInfoRepositoryImpl
import com.example.ihrm.data.repository.MySecurityRepositoryImpl
import com.example.ihrm.data.repository.OrganizationRepositoryImpl
import com.example.ihrm.data.repository.SecurityCheckDetailRepositoryImpl
import com.example.ihrm.data.repository.SecurityCheckRepositoryImpl
import com.example.ihrm.domain.repository.AuthRepository
import com.example.ihrm.domain.repository.EmployeeRepository
import com.example.ihrm.domain.repository.LanguageRepository
import com.example.ihrm.domain.repository.MyInfoRepository
import com.example.ihrm.domain.repository.MySecurityCheckRepository
import com.example.ihrm.domain.repository.OrganizationRepository
import com.example.ihrm.domain.repository.SecurityCheckDetailRepository
import com.example.ihrm.domain.repository.SecurityCheckRepository
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

    @Binds
    @Singleton
    abstract fun bindMyInfoRepository(
        authRepositoryImpl: MyInfoRepositoryImpl
    ): MyInfoRepository

    @Binds
    @Singleton
    abstract fun bindSecurityCheckRepository(
        impl: SecurityCheckRepositoryImpl
    ): SecurityCheckRepository

    @Binds
    @Singleton
    abstract fun bindSecurityCheckDetailRepository(
        impl: SecurityCheckDetailRepositoryImpl
    ): SecurityCheckDetailRepository

    @Binds
    @Singleton
    abstract fun bindMySecurityCheckRepository(
        impl: MySecurityRepositoryImpl
    ): MySecurityCheckRepository

    @Binds
    @Singleton
    abstract fun bindOrganizationRepository(
        impl: OrganizationRepositoryImpl
    ): OrganizationRepository

}