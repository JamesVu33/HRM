package com.example.ihrm.data.remote.securities

import com.google.gson.annotations.SerializedName

data class SubmissionStatusResponse(
    @SerializedName("status") val status: String? = null,
    @SerializedName("count") val count: Int? = null,
)