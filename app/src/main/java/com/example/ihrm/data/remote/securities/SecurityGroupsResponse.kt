package com.example.ihrm.data.remote.securities

import com.example.ihrm.data.remote.base.ResponseToInfoMapper
import com.example.ihrm.domain.model.SecurityGroups
import com.google.gson.annotations.SerializedName

data class SecurityGroupsResponse(
    @SerializedName("id")
    val id: String? = null,
    @SerializedName("code")
    val code: String? = null,
    @SerializedName("name")
    val name: String? = null,
) : ResponseToInfoMapper<SecurityGroups> {
    override fun fromResponseToInfo(): SecurityGroups {
        return SecurityGroups(
            code = code,
            name = name,
        )
    }
}
