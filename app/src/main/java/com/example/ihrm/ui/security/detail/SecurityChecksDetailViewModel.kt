package com.example.ihrm.ui.security.detail

import android.util.Log
import com.example.ihrm.core.errorHandler.CommonErrorException
import com.example.ihrm.core.viewmodel.BaseViewmodel
import com.example.ihrm.core.viewmodel.CallbackWrapper
import com.example.ihrm.domain.model.securitycheck.SecurityCheckDetail
import com.example.ihrm.domain.model.securitycheck.SecurityTemplate
import com.example.ihrm.domain.usecase.securities.SecurityDetailUiModel
import com.example.ihrm.domain.usecase.securities.SecurityDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject


@HiltViewModel
class SecurityChecksDetailViewModel @Inject constructor(
    private val securityDetailUseCase: SecurityDetailUseCase,
) : BaseViewmodel() {
    private val _uiState = MutableStateFlow(SecurityDetailUiModel())
    val uiState: StateFlow<SecurityDetailUiModel> = _uiState.asStateFlow()
    fun getSecurityCheckDetail(id: String) {
        fetchData(
            fetching = { securityDetailUseCase.getSecurityCheckDetail(id) },
            callbackWrapper = object : CallbackWrapper<SecurityCheckDetail> {
                override fun onSuccess(data: SecurityCheckDetail) {
                    getSecurityTemplates(data)
                }

                override fun onFail(e: CommonErrorException) {
                    super.onFail(e)
                    Log.d("SecurityChecksDetailViewModel", "onFail: $e")
                }
            }
        )
    }

    fun getSecurityTemplates(dataSecurityCheckDetail: SecurityCheckDetail) {
        fetchData(
            fetching = { securityDetailUseCase.getSecurityTemplates(dataSecurityCheckDetail.templateId) },
            callbackWrapper = object : CallbackWrapper<SecurityTemplate> {
                override fun onSuccess(data: SecurityTemplate) {
                    _uiState.value = securityDetailUseCase.mapToUiState(dataSecurityCheckDetail, data)
                }

                override fun onFail(e: CommonErrorException) {
                    super.onFail(e)
                    Log.d("SecurityChecksDetailViewModel", "onFail: $e")
                }
            }
        )
    }
}