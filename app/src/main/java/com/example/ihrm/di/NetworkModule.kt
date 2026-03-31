package com.example.ihrm.di

import com.example.ihrm.BuildConfig
import com.example.ihrm.data.remote.api.AuthApiService
import com.example.ihrm.data.remote.api.CountryApiService
import com.example.ihrm.data.remote.api.EmployeeApiService
import com.example.ihrm.data.remote.api.LanguageApiService
import com.example.ihrm.data.remote.api.MyInfoApiService
import com.example.ihrm.data.remote.interceptor.AuthInterceptor
import com.example.ihrm.data.remote.interceptor.ErrorInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class InternalApi

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class ExternalApi

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder()
            .setLenient()
            .create()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(gson: Gson): OkHttpClient {
        val authInterceptor = AuthInterceptor()
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30L, TimeUnit.SECONDS)
            .readTimeout(30L, TimeUnit.SECONDS)
            .writeTimeout(30L, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    @InternalApi
    fun provideRetrofit(okHttpClient: OkHttpClient, gson: Gson): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    @Singleton
    @ExternalApi
    fun provideExternalRetrofit(okHttpClient: OkHttpClient, gson: Gson): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://restcountries.com/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    @Singleton
    fun provideEmployeeApiService(@InternalApi retrofit: Retrofit): EmployeeApiService {
        return retrofit.create(EmployeeApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideAuthApiService(@InternalApi retrofit: Retrofit): AuthApiService {
        return retrofit.create(AuthApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideLanguageApiService(@InternalApi retrofit: Retrofit): LanguageApiService {
        return retrofit.create(LanguageApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideMyInfoApiService(@InternalApi retrofit: Retrofit): MyInfoApiService {
        return retrofit.create(MyInfoApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideCountryApi(@ExternalApi externalRetrofit: Retrofit): CountryApiService {
        return externalRetrofit.create(CountryApiService::class.java)
    }
}