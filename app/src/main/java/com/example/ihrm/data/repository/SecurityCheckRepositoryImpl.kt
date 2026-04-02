package com.example.ihrm.data.repository

import android.util.Log
import com.example.ihrm.data.remote.api.SecurityCheckApiService
import com.example.ihrm.data.remote.base.NetworkResult
import com.example.ihrm.data.remote.base.safeApiCall
import com.example.ihrm.data.remote.base.safeApiCallPaginated
import com.example.ihrm.data.remote.mapper.toDomain
import com.example.ihrm.data.remote.mapper.toSubmissionPaginationMeta
import com.example.ihrm.data.remote.securities.SecurityGroupsResponse
import com.example.ihrm.di.NetworkModule
import com.example.ihrm.domain.model.SecurityCheckSubmissionsPage
import com.example.ihrm.domain.model.SecurityGroups
import com.example.ihrm.domain.repository.SecurityCheckRepository
import retrofit2.Retrofit
import javax.inject.Inject

class SecurityCheckRepositoryImpl @Inject constructor(
    private val apiService: SecurityCheckApiService,
    @param:NetworkModule.InternalApi private val retrofit: Retrofit,
) : SecurityCheckRepository {

    override suspend fun getSubmissions(page: Int, limit: Int): NetworkResult<SecurityCheckSubmissionsPage> {
        return safeApiCallPaginated(retrofit) {
            apiService.getSubmissions(page = page, limit = limit)
        }.map { paginated ->
            SecurityCheckSubmissionsPage(
                items = paginated.data.orEmpty().map { it.toDomain() },
                meta = paginated.meta?.toSubmissionPaginationMeta(),
            )
        }
    }

    override suspend fun getGroups(): NetworkResult<List<SecurityGroups>>  {
        val test =  safeApiCall(retrofit) {
            Log.d("apiFlows", "apiService.getGroups(): ${apiService.getGroups()}")
            return@safeApiCall apiService.getGroups()
        }.map { data ->
            Log.d("apiFlows", "map with: $data")
            data.map {
                it.fromResponseToInfo()
            }
        }
        Log.d("apiFlows", "test: $test")
        return test
    }


}
