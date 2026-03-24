package com.example.ihrm.util

typealias EmptyFunc = () -> Unit
typealias ParamFunc<T> = (data: T) -> Unit
typealias SupEmptyFunc<T> = suspend () -> T