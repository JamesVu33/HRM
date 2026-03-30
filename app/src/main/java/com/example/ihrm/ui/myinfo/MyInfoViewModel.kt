package com.example.ihrm.ui.myinfo

import android.util.Log
import com.example.ihrm.core.errorHandler.CommonErrorException
import com.example.ihrm.core.viewmodel.BaseViewmodel
import com.example.ihrm.core.viewmodel.CallbackWrapper
import com.example.ihrm.domain.model.MyInfo
import com.example.ihrm.domain.usecase.myInfo.MyInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MyInfoViewModel @Inject constructor(
    private val myInfoUseCase: MyInfoUseCase,
) : BaseViewmodel() {

    private val _myInfo = MutableStateFlow<MyInfo?>(null)
    val myInfo: StateFlow<MyInfo?> = _myInfo.asStateFlow()

    init {
        loadMyInfo()
    }

    /**
     * Tải GET /me + GET /me/profile (gộp trong use case), cùng pattern [fetchData] như [com.example.ihrm.ui.login.LoginViewModel].
     */
    fun loadMyInfo() {
        fetchData(
            fetching = { myInfoUseCase.loadMyInfo() },
            callbackWrapper = object : CallbackWrapper<MyInfo> {
                override fun onSuccess(data: MyInfo) {
                    _myInfo.value = data
                }

                override fun onFail(e: CommonErrorException) {
                    _myInfo.value = null
                }
            },
        )
    }
}
