package com.example.ihrm.core.errorHandler

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

object GlobalErrorHandler {
    private val _error = MutableSharedFlow<String>(extraBufferCapacity = 1)
    val globalError = _error.asSharedFlow()

    fun showError(msg: String?) {
        if (msg.isNullOrEmpty()) return
        _error.tryEmit(msg)
    }

    fun clearError() {
        _error.tryEmit("")
    }
}