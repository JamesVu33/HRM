package com.example.ihrm.data.remote.securities

import com.google.gson.annotations.SerializedName

data class SecuritySubmissionRequest(
    @SerializedName("answers")
    val answers: List<SecurityAnswerRequest>
)

data class SecurityAnswerRequest(
    @SerializedName("key")
    val key: String,
    @SerializedName("value")
    val value: Boolean,
    @SerializedName("remark")
    val remark: String? = null
)