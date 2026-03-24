package com.example.ihrm.core.viewmodel

import android.util.Log
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ihrm.core.errorHandler.CommonErrorException
import com.example.ihrm.data.remote.dto.NetworkResult
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

typealias EmptyFunc = () -> Unit
typealias ParamFunc<T> = (data: T) -> Unit
typealias FetchSupFunc<T> = suspend () -> T

interface CallbackWrapper<T> {
    fun onSuccess(data: T) {}
    suspend fun doOnBackground(data: T) {}
    fun onError(e: CommonErrorException) {}
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
                // will define another exception for this, instead of network exception if has
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
            callbackWrapper.onError(errorException)
        }

    /**
     * common fetch data function
     *
     */
    fun <R> fetchData(
        onLoading: EmptyFunc? = null,
        fetching: FetchSupFunc<R>,
        callbackWrapper: CallbackWrapper<R>,
    ) {
        viewModelScope.launch(Dispatchers.IO + errorCatcher(callbackWrapper)) {
            onLoading?.invoke()
            _loading.tryEmit(true)

            // globalCoroutineExceptionHandler will handle error exception about API fetching
            // for business/logic exception when handle data after fetched
            // please handle in useCase class instead
            val response = fetching()

            launch(Dispatchers.Default) {
                callbackWrapper.doOnBackground(response)
            }
            launch(Dispatchers.Main) {
                if (_loading.value) {
                    _loading.value = false
                }
                callbackWrapper.onSuccess(response)
            }
        }
    }

    fun <T> handleApiResponse(response: NetworkResult<T>, onSuccess: ParamFunc<T>?, onFailure: EmptyFunc? = null) {
        when (response) {
            is NetworkResult.Success -> {
                onSuccess?.invoke(response.data)
            }
            is NetworkResult.ApiError -> {
                onFailure?.invoke()
                _error.tryEmit(CommonErrorException.InvalidInputException(null, response.error.errorType, response.error.errors))
            }
            is NetworkResult.Exception -> {
                onFailure?.invoke()
                _error.tryEmit(CommonErrorException.NetworkException(response.e.message))
            }
        }
    }
}