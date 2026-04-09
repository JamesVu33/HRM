package com.example.ihrm.data.remote.securities

import com.google.gson.annotations.SerializedName

data class SecurityCheckStatusResponse(
    @SerializedName("hasSubmitted")
    val hasSubmitted: Boolean? = null,
    @SerializedName("isSecurityCheckRequired")
    val isSecurityCheckRequired: Boolean? = null
)