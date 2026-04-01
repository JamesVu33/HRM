package com.example.ihrm.data.remote.api

import com.example.ihrm.data.remote.myinfo.CountryResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CountryApiService {
    @GET("v3.1/all")
    suspend fun getCountries(
        @Query("fields") fields: String = "name,cca2"
    ): Response<List<CountryResponse>>
}