package com.example.ihrm.core

import com.example.ihrm.domain.model.LoginInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

object AppContainer {
    var loginInfo: LoginInfo? = null
        private set

    private var _loginInfoFlow = MutableStateFlow<LoginInfo?>(null)
    val loginInfoFlow = _loginInfoFlow.asStateFlow()

    fun updateLoginInfo(data: LoginInfo) {
        loginInfo = data
        _loginInfoFlow.value = data
    }

}