package com.example.ihrm.data.remote.base

interface ResponseToInfoMapper<T> {
    fun fromResponseToInfo(): T
}