package com.example.ihrm.domain.usecase.myInfo

import com.example.ihrm.core.usecase.BaseUseCase
import com.example.ihrm.data.remote.base.NetworkResult
import com.example.ihrm.domain.model.MyInfo
import com.example.ihrm.domain.model.withMergedProfile
import com.example.ihrm.domain.repository.LanguageRepository
import com.example.ihrm.domain.repository.MyInfoRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class MyInfoUseCase @Inject constructor(
    private val myInfoRepository: MyInfoRepository,
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

        when {
            meResult is NetworkResult.Failure -> meResult
            profileResult is NetworkResult.Failure -> profileResult
            meResult is NetworkResult.Exception -> meResult
            profileResult is NetworkResult.Exception -> profileResult
            else -> {
                val base = (meResult as NetworkResult.Success).data
                val detail = (profileResult as NetworkResult.Success).data
                NetworkResult.Success(base.withMergedProfile(detail))
            }
        }
    }
}
