package com.example.ihrm.data.repository

import com.example.ihrm.data.local.dao.LanguageDao
import com.example.ihrm.data.local.entity.LanguageEntity
import com.example.ihrm.data.remote.api.LanguageApiService
import com.example.ihrm.data.remote.base.NetworkResult
import com.example.ihrm.data.remote.base.safeApiCall
import com.example.ihrm.data.remote.language.LanguageRequest
import com.example.ihrm.data.remote.language.LanguageResponse
import com.example.ihrm.data.remote.language.LanguageStatus
import com.example.ihrm.di.NetworkModule
import com.example.ihrm.domain.repository.LanguageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import retrofit2.Retrofit
import javax.inject.Inject

/** Gọi API auth be-nest-hrm: POST /auth/login (base URL từ Constants.BASE_URL). */
class LanguageRepositoryImpl @Inject constructor(
    private val apiService: LanguageApiService,
    private val languageDao: LanguageDao,
    @param:NetworkModule.InternalApi private val retrofit: Retrofit
) : LanguageRepository {

    override suspend fun language(): NetworkResult<List<LanguageResponse>> =
        safeApiCall(retrofit) {
            apiService.language(LanguageRequest().toMap())
        }

    override suspend fun saveLanguages(languages: List<LanguageResponse>) {
        val entities = languages.map {
            LanguageEntity(
                id = it.id,
                key = it.key,
                namespace = it.namespace,
                valueVi = it.valueVi,
                valueEn = it.valueEn,
                valueKr = it.valueKr,
                status = it.status
            )
        }
        languageDao.insertLanguages(entities)
    }

    override fun getLanguages(): Flow<List<LanguageResponse>> {
        return languageDao.getAllLanguages().map { entities ->
            entities.map {
                LanguageResponse(
                    id = it.id,
                    key = it.key,
                    namespace = it.namespace,
                    valueVi = it.valueVi,
                    valueEn = it.valueEn,
                    valueKr = it.valueKr,
                    status = it.status
                )
            }
        }
    }

    override fun getLanguageByKey(key: String): Flow<LanguageResponse> {
        return languageDao.getLanguageByKey(key).map {
            LanguageResponse(
                id = it?.id ?: "",
                key = it?.key ?: "",
                namespace = it?.namespace ?: "",
                valueVi = it?.valueVi ?: "",
                valueEn = it?.valueEn ?: "",
                valueKr = it?.valueKr ?: "",
                status = it?.status ?: LanguageStatus.ACTIVE,
            )
        }
    }

}
