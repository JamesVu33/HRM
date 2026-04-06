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

    override suspend fun getSubmissions(
        fromDate: String?,
        toDate: String?,
        query: String?,
        page: Int?,
        limit: Int?,
        orderBy: String?,
        sortBy: String?,
        status: String?,
        type: String?,
        monthCode: String?,
        groupId: String?,
    ): NetworkResult<SecurityCheckSubmissionsPage> {
        return safeApiCallPaginated(retrofit) {
            apiService.getSubmissions(
                fromDate = fromDate,
                toDate = toDate,
                query = query,
                page = page,
                limit = limit,
                orderBy = orderBy,
                sortBy = sortBy,
                status = status,
                type = type,
                monthCode = monthCode,
                groupId = groupId,
            )
        }.map { paginated ->
            val items = paginated.data
            SecurityCheckSubmissionsPage(
                items = items?.data.orEmpty().map { it.toDomain() },
                listStats = items?.submissionsByStatus ?: emptyList(),
                meta = paginated.meta?.toSubmissionPaginationMeta(),
            )
        }
    }

    override suspend fun getNotSubmissions(
        fromDate: String?,
        toDate: String?,
        query: String?,
        page: Int?,
        limit: Int?,
        orderBy: String?,
        sortBy: String?,
        status: String?,
        type: String?,
        monthCode: String?,
        groupId: String?,
    ): NetworkResult<SecurityCheckSubmissionsPage> {
        return safeApiCallPaginated(retrofit) {
            apiService.getNotSubmissions(
                fromDate = fromDate,
                toDate = toDate,
                query = query,
                page = page,
                limit = limit,
                orderBy = orderBy,
                sortBy = sortBy,
                status = status,
                type = type,
                monthCode = monthCode,
                groupId = groupId,
            )
        }.map { paginated ->
            Log.d("not submitted", "getNotSubmissions: $paginated")
            SecurityCheckSubmissionsPage(
                items = paginated.data.orEmpty().map { it.toDomain() },
                listStats = emptyList(),
                meta = paginated.meta?.toSubmissionPaginationMeta(),
            )
        }
    }

    override suspend fun getGroups(): NetworkResult<List<SecurityGroups>> {
        val test = safeApiCall(retrofit) {
            return@safeApiCall apiService.getGroups()
        }.map { data ->
            data.map {
                it.fromResponseToInfo()
            }
        }
        return test
    }
}
