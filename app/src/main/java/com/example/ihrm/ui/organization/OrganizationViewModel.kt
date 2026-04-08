package com.example.ihrm.ui.organization

import android.util.Log
import com.example.ihrm.core.errorHandler.CommonErrorException
import com.example.ihrm.core.viewmodel.BaseViewmodel
import com.example.ihrm.core.viewmodel.CallbackWrapper
import com.example.ihrm.domain.model.Department
import com.example.ihrm.domain.usecase.organization.OrganizationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class OrganizationViewModel @Inject constructor(
    private val organizationUseCase: OrganizationUseCase
) : BaseViewmodel() {

    init {
        getOrganizations()
    }

    private val _uiState = MutableStateFlow<List<Department>>(emptyList())
    val uiState: StateFlow<List<Department>> = _uiState.asStateFlow()

    fun getOrganizations() {
        fetchData(
            fetching = { organizationUseCase.getOrganizations() },
            callbackWrapper = object : CallbackWrapper<List<Department>> {
                override fun onSuccess(data: List<Department>) {
                    super.onSuccess(data)
                    _uiState.value = data
                    Log.d("OrganizationViewModel", "code go here: $data")
                }

                override fun onFail(e: CommonErrorException) {
                    super.onFail(e)
                    Log.d("OrganizationViewModel", "onFail: $e")
                }
            }
        )
    }
}