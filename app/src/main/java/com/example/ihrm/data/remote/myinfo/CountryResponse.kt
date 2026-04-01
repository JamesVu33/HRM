package com.example.ihrm.data.remote.myinfo

import com.example.ihrm.data.remote.base.ResponseToInfoMapper
import com.example.ihrm.domain.model.Country
import com.google.gson.annotations.SerializedName

data class CountryResponse(
    @SerializedName("name")
    val name: NameResponse,
    @SerializedName("cca2")
    val cca2: String
) : ResponseToInfoMapper<Country> {
    override fun fromResponseToInfo(): Country {
        return Country(
            name = name.common,
            code = cca2,
        )
    }
}

data class NameResponse(
    @SerializedName("common")
    val common: String,
    @SerializedName("official")
    val official: String,
    @SerializedName("nativeName")
    val nativeName: Map<String, NativeNameResponse>?
)

data class NativeNameResponse(
    @SerializedName("official")
    val official: String,
    @SerializedName("common")
    val common: String
)