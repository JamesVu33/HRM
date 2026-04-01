package com.example.ihrm.domain.model

enum class Gender {
    MALE,
    FEMALE,
    OTHER,
    UNKNOWN;

    companion object {
        fun from(value: String?): Gender {
            return entries.find { it.name == value } ?: UNKNOWN
        }
    }
}
