package com.example.ihrm.core.viewmodel

import android.util.Log
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ihrm.core.errorHandler.CommonErrorException
import com.example.ihrm.data.remote.base.NetworkResult
import com.example.ihrm.util.EmptyFunc
import com.example.ihrm.util.ParamFunc
import com.example.ihrm.util.SupEmptyFunc
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.json.JSONException
import retrofit2.HttpException
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLException

interface CallbackWrapper<T> {
    fun onSuccess(data: T) {}

    // in case mapping data or do another work in background
    suspend fun doOnBackground(data: T) {}

    // for API calling failure case (400...)
    fun onFail(e: CommonErrorException) {}
}

@Immutable
data class CommonUIState<T>(
    val errorType: CommonErrorException? = null,
    val isLoading: Boolean = false,
    val uiState: T
)

abstract class BaseViewmodel : ViewModel() {
    // for API calling error exception handler
    private val _error = MutableStateFlow<CommonErrorException?>(null)
    val errorEvent = _error.asStateFlow()

    // for API calling indicator
    private val _loading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _loading.asStateFlow()

    // a common way to wrap all thing in one UI class
    open val commonUIState = mutableStateOf<CommonUIState<*>?>(null)

    /**
     * for API calling error exception handler
     */
    private fun errorCatcher(callbackWrapper: CallbackWrapper<*>) =
        CoroutineExceptionHandler { _, throwable ->
            Log.e("GlobalError", "Caught unhandled exception: ${throwable.message}")

            val errorException = when (throwable) {
                is JSONException, is SocketTimeoutException, is SSLException, is ConnectException, is UnknownHostException -> {
                    CommonErrorException.NetworkException(
                        throwable.message ?: "An unexpected error occurred"
                    )
                }

                is IOException -> CommonErrorException.NetworkException(
                    throwable.message ?: "An unexpected error occurred"
                )

                is HttpException -> CommonErrorException.ServerException(
                    throwable.message ?: "Server exception"
                )

                else -> CommonErrorException.UnknownException(
                    throwable.message ?: "An unexpected error occurred"
                )
            }

            _loading.tryEmit(false)
            _error.tryEmit(errorException)
            callbackWrapper.onFail(errorException)
        }

    /**
     * common fetch data function using CallbackWrapper
     */
    fun <R> fetchOriginData(
        onLoading: EmptyFunc? = null,
        fetching: SupEmptyFunc<R>,
        callbackWrapper: CallbackWrapper<R>,
    ) {
        viewModelScope.launch(Dispatchers.IO + errorCatcher(callbackWrapper)) {
            onLoading?.invoke() ?: _loading.tryEmit(true)

            val response = fetching()

            launch(Dispatchers.Default) {
                callbackWrapper.doOnBackground(response)
            }
            launch(Dispatchers.Main) {
                _loading.tryEmit(false)
                callbackWrapper.onSuccess(response)
            }
        }
    }

    /**
     * New fetchData overload specifically for NetworkResult with lambda callbacks.
     * Manages loading state and error handling automatically.
     */
    fun <T> fetchData(
        onLoading: EmptyFunc? = null,
        fetching: suspend () -> NetworkResult<T>,
        callbackWrapper: CallbackWrapper<T>,
    ) {
        viewModelScope.launch(Dispatchers.IO + errorCatcher(callbackWrapper)) {
            onLoading?.invoke() ?: _loading.tryEmit(true)
            val response = fetching()
            launch(Dispatchers.Main) {
                _loading.emit(false)
                handleApiResponse(response, callbackWrapper) {
                    launch(Dispatchers.Default) {
                        callbackWrapper.doOnBackground(it)
                    }
                    launch {
                        callbackWrapper.onSuccess(it)
                    }
                }
            }
        }
    }


    /**
     * handle API response with callback wrapper and error handling using by [fetchData]
     */
    private fun <T> handleApiResponse(
        response: NetworkResult<T>,
        onCallbackWrapper: CallbackWrapper<T>,
        onSuccess: ParamFunc<T>
    ) {
        when (response) {
            is NetworkResult.Success -> {
                onSuccess(response.data)
            }

            is NetworkResult.Failure -> {
                onCallbackWrapper.onFail(response.error)
                _error.tryEmit(response.error)
            }

            is NetworkResult.Exception -> {
                onCallbackWrapper.onFail(CommonErrorException.UnknownException(response.e.message))
                _error.tryEmit(
                    CommonErrorException.NetworkException(
                        response.e.message ?: "Network error"
                    )
                )
            }
        }
    }

    /**
     * handle API response with lambda callbacks and error handling using [fetchData]
     */
    fun <T> handleApiResponse(
        response: NetworkResult<T>,
        onSuccess: ParamFunc<T>?,
        onFailure: EmptyFunc? = null
    ) {
        when (response) {
            is NetworkResult.Success -> {
                onSuccess?.invoke(response.data)
            }

            is NetworkResult.Failure -> {
                onFailure?.invoke()
                _error.tryEmit(response.error)
            }

            is NetworkResult.Exception -> {
                onFailure?.invoke()
                _error.tryEmit(
                    CommonErrorException.NetworkException(
                        response.e.message ?: "Network error"
                    )
                )
            }
        }
    }
}
