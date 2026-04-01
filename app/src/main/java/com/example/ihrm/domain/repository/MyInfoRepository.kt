package com.example.ihrm.domain.repository

import com.example.ihrm.data.remote.base.NetworkResult
import com.example.ihrm.data.remote.myinfo.ChangePasswordRequest
import com.example.ihrm.data.remote.myinfo.UpdateProfileRequest
import com.example.ihrm.domain.model.Country
import com.example.ihrm.domain.model.MyInfo
import com.example.ihrm.domain.model.MyProfile

interface MyInfoRepository {
    suspend fun getMeEmployeeInfo(): NetworkResult<MyInfo>

    suspend fun getMeProfile(): NetworkResult<MyProfile>

    suspend fun getCountries(): NetworkResult<List<Country>>

    suspend fun updateMeProfile(request: UpdateProfileRequest): NetworkResult<MyProfile>
    suspend fun updateInfoMeProfile(request: UpdateProfileRequest): NetworkResult<MyProfile>
    suspend fun changePassword(request: ChangePasswordRequest): NetworkResult<Unit>
}