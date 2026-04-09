package com.example.ihrm.ui.security.mysecurity

import android.util.Log
import com.example.ihrm.core.errorHandler.CommonErrorException
import com.example.ihrm.core.viewmodel.BaseViewmodel
import com.example.ihrm.core.viewmodel.CallbackWrapper
import com.example.ihrm.data.remote.securities.MySecurityCheckResponse
import com.example.ihrm.domain.usecase.securities.MySecurityUseCase
import com.example.ihrm.util.Constants.DEFAULT_LIMIT
import com.example.ihrm.util.Constants.DEFAULT_PAGE
import com.example.ihrm.util.SecurityLegendMeta
import com.example.ihrm.util.SecurityLegendMeta.Companion.DEFAULT
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class MySecurityCheckUiState(
    val id: String = "",
    val employeeId: String = "",
    val userName: String = "",
    val status: SecurityLegendMeta = DEFAULT,
    val templateName: String = "",
    val submittedAt: String = "",
)
@HiltViewModel
class MySecurityCheckViewModel @Inject constructor(
    private val mySecurityUseCase: MySecurityUseCase
) : BaseViewmodel() {
    private val _uiState = MutableStateFlow<List<MySecurityCheckUiState>>(emptyList())
    val uiState: StateFlow<List<MySecurityCheckUiState>> = _uiState.asStateFlow()

    init {
        getMySecurityCheck()
    }

    fun getMySecurityCheck(
        year: Int? = null,
        page: Int = DEFAULT_PAGE,
        limit: Int = DEFAULT_LIMIT,
        orderBy: String? = null,
        sortBy: String? = null,
        status: String? = null,
    ) {
        clearList()
        fetchData(
            fetching = {
                mySecurityUseCase.getMySecurityCheck(
                    year = year,
                    page = page,
                    limit = limit,
                    orderBy = orderBy,
                    sortBy = sortBy,
                    status = status
                )
            },
            callbackWrapper = object : CallbackWrapper<List<MySecurityCheckResponse>> {
                override fun onSuccess(data: List<MySecurityCheckResponse>) {
                    super.onSuccess(data)
                    setSecurityChecks(
                        data.map { item ->
                            mySecurityUseCase.mapToUiState(item)
                        }
                    )
                }

                override fun onFail(e: CommonErrorException) {
                    super.onFail(e)
                    Log.d("MySecurityCheckViewModel", "onFail: $e")
                }
            },
        )
    }

    fun setSecurityChecks(newList: List<MySecurityCheckUiState>) {
        _uiState.update { newList }
    }

    fun clearList() {
        _uiState.update { emptyList() }
    }
}