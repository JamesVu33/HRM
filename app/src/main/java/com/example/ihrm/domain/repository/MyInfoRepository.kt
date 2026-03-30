package com.example.ihrm.domain.repository

import com.example.ihrm.data.remote.base.NetworkResult
import com.example.ihrm.data.remote.myinfo.MeProfileResponse
import com.example.ihrm.data.remote.myinfo.MeResponse
import com.example.ihrm.domain.model.MyInfo
import com.example.ihrm.domain.model.MyProfile

interface MyInfoRepository {
    suspend fun getMeEmployeeInfo(): NetworkResult<MyInfo>

    suspend fun getMeProfile(): NetworkResult<MyProfile>
}