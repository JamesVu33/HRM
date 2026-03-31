package com.example.ihrm.ui.myinfo

import android.content.Context
import android.net.Uri
import android.util.Log
import com.example.ihrm.core.errorHandler.CommonErrorException
import com.example.ihrm.core.viewmodel.BaseViewmodel
import com.example.ihrm.core.viewmodel.CallbackWrapper
import com.example.ihrm.data.remote.base.NetworkResult
import com.example.ihrm.data.remote.myinfo.UpdateProfileRequest
import com.example.ihrm.domain.model.Country
import com.example.ihrm.domain.model.MyInfo
import com.example.ihrm.domain.model.MyProfile
import com.example.ihrm.domain.usecase.myInfo.MyInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import javax.inject.Inject

@HiltViewModel
class MyInfoViewModel @Inject constructor(
    private val myInfoUseCase: MyInfoUseCase,
    @ApplicationContext private val appContext: Context,
) : BaseViewmodel() {

    private val _myInfo = MutableStateFlow<MyInfo?>(null)
    val myInfo: StateFlow<MyInfo?> = _myInfo.asStateFlow()

    private val _countries = MutableStateFlow<List<Country>?>(null)
    val countries: StateFlow<List<Country>?> = _countries.asStateFlow()

    init {
        loadMyInfo()
        loadCountries()
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

    fun loadCountries() {
        fetchData(
            fetching = { myInfoUseCase.loadCountries() },
            callbackWrapper = object : CallbackWrapper<List<Country>> {
                override fun onSuccess(data: List<Country>) {
                    _countries.value = data
                }

                override fun onFail(e: CommonErrorException) {
                    _countries.value = null
                }
            },
        )
    }

    fun changeAvatar(uri: Uri) {
        fetchData(
            fetching = {
                val avatarPart = buildAvatarPart(uri) ?: return@fetchData NetworkResult.Exception(
                    CommonErrorException.InvalidLogicException(
                        errorKey = "invalid_avatar",
                        errorMsg = "Invalid image file"
                    )
                )
                myInfoUseCase.changeAvatar(avatarPart)
            },
            callbackWrapper = object : CallbackWrapper<Unit> {
                override fun onSuccess(data: Unit) {
                    loadMyInfo()
                }

                override fun onFail(e: CommonErrorException) {
                    super.onFail(e)
                }
            }
        )
    }

    fun updateProfile(request: UpdateProfileRequest) {
        fetchData(
            fetching = { myInfoUseCase.updateMeProfile(request) },
            callbackWrapper = object : CallbackWrapper<MyProfile> {
                override fun onSuccess(data: MyProfile) {
                    loadMyInfo()
                }

                override fun onFail(e: CommonErrorException) {
                    super.onFail(e)
                }
            }
        )
    }

    private fun buildAvatarPart(uri: Uri): MultipartBody.Part? {
        val contentResolver = appContext.contentResolver
        val mimeType = contentResolver.getType(uri) ?: "image/jpeg"
        val bytes = contentResolver.openInputStream(uri)?.use { it.readBytes() } ?: return null
        val requestBody = bytes.toRequestBody(mimeType.toMediaTypeOrNull())
        val filename = "avatar_${System.currentTimeMillis()}.jpg"
        return MultipartBody.Part.createFormData(
            name = "avatar",
            filename = filename,
            body = requestBody
        )
    }
}
