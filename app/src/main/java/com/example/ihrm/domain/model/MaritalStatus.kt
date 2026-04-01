package com.example.ihrm.domain.model

enum class MaritalStatus {
    SINGLE,
    MARRIED,
    WIDOWED,
    DIVORCED,
    UNKNOWN;

    companion object {
        fun from(value: String?): MaritalStatus {
            return entries.find { it.name == value } ?: UNKNOWN
        }
    }
}
