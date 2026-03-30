package com.example.ihrm.domain.repository

import com.example.ihrm.data.remote.base.NetworkResult
import com.example.ihrm.data.remote.myinfo.MeProfileResponse
import com.example.ihrm.data.remote.myinfo.MeResponse

interface MyInfoRepository {
    suspend fun getMeEmployeeInfo(): NetworkResult<MeResponse>

    suspend fun getMeProfile(): NetworkResult<MeProfileResponse>
}