package com.example.ihrm.domain.usecase.myInfo

import com.example.ihrm.core.usecase.BaseUseCase
import com.example.ihrm.data.remote.base.NetworkResult
import com.example.ihrm.data.remote.myinfo.ChangePasswordRequest
import com.example.ihrm.data.remote.myinfo.UpdateProfileRequest
import com.example.ihrm.domain.model.Country
import com.example.ihrm.domain.model.MyInfo
import com.example.ihrm.domain.model.MyProfile
import com.example.ihrm.domain.model.withMergedProfile
import com.example.ihrm.domain.repository.EmployeeRepository
import com.example.ihrm.domain.repository.LanguageRepository
import com.example.ihrm.domain.repository.MyInfoRepository
import com.example.ihrm.util.combineResult
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import okhttp3.MultipartBody
import javax.inject.Inject

class MyInfoUseCase @Inject constructor(
    private val myInfoRepository: MyInfoRepository,
    private val employeeRepository: EmployeeRepository,
) : BaseUseCase() {
    @Inject
    override lateinit var languageRepository: LanguageRepository

    /**
     * Gọi song song GET /me và GET /me/profile, gộp vào một [MyInfo] (domain).
     */
    suspend fun loadMyInfo(): NetworkResult<MyInfo> = coroutineScope {
        val meDeferred = async { translateResponse(myInfoRepository.getMeEmployeeInfo()) }
        val profileDeferred = async { translateResponse(myInfoRepository.getMeProfile()) }
        val meResult = meDeferred.await()
        val profileResult = profileDeferred.await()

        combineResult(meResult, profileResult) { base, detail -> base.withMergedProfile(detail) }
    }

    suspend fun loadCountries(): NetworkResult<List<Country>> = coroutineScope {
        translateResponse(myInfoRepository.getCountries())
    }

    suspend fun changeAvatar(avatar: MultipartBody.Part): NetworkResult<Unit> = coroutineScope {
        translateResponse(employeeRepository.changeAvatar(avatar))
    }

    suspend fun updateMeProfile(request: UpdateProfileRequest): NetworkResult<MyProfile> = coroutineScope {
        translateResponse(myInfoRepository.updateMeProfile(request))
    }

    suspend fun updateInfoMeProfile(request: UpdateProfileRequest): NetworkResult<MyProfile> = coroutineScope {
        translateResponse(myInfoRepository.updateInfoMeProfile(request))
    }

    suspend fun changePassword(request: ChangePasswordRequest): NetworkResult<Unit> = coroutineScope {
        translateResponse(myInfoRepository.changePassword(request))
    }
}
