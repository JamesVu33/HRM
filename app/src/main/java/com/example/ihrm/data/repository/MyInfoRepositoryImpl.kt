package com.example.ihrm.data.repository

import com.example.ihrm.data.remote.api.CountryApiService
import com.example.ihrm.data.remote.api.MyInfoApiService
import com.example.ihrm.data.remote.base.NetworkResult
import com.example.ihrm.data.remote.base.safeApiCall
import com.example.ihrm.data.remote.base.safeApiCallRaw
import com.example.ihrm.data.remote.myinfo.UpdateProfileRequest
import com.example.ihrm.di.NetworkModule
import com.example.ihrm.domain.model.Country
import com.example.ihrm.domain.model.MyInfo
import com.example.ihrm.domain.model.MyProfile
import com.example.ihrm.domain.repository.MyInfoRepository
import retrofit2.Retrofit
import javax.inject.Inject

class MyInfoRepositoryImpl @Inject constructor(
    private val myInfoApiService: MyInfoApiService,
    private val countryApiService: CountryApiService,
    @param:NetworkModule.InternalApi private val retrofit: Retrofit,
    @param:NetworkModule.ExternalApi private val retrofitExternal: Retrofit
) : MyInfoRepository {
    override suspend fun getMeEmployeeInfo(): NetworkResult<MyInfo> =
        safeApiCall(retrofit) {
            myInfoApiService.getMe()
        }.map { it.fromResponseToInfo() }

    override suspend fun getMeProfile(): NetworkResult<MyProfile> =
        safeApiCall(retrofit) {
            myInfoApiService.getMeProfile()
        }.map { it.fromResponseToInfo() }

    override suspend fun getCountries(): NetworkResult<List<Country>> =
        safeApiCallRaw {
            countryApiService.getCountries()
        }.map { data ->
            data.map { it.fromResponseToInfo() }
        }

    override suspend fun updateMeProfile(request: UpdateProfileRequest): NetworkResult<MyProfile> =
        safeApiCall(retrofit) {
            myInfoApiService.updateMeProfile(request)
        }.map { it.fromResponseToInfo() }
}
