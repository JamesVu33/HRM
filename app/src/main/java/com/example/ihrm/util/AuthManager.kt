package com.example.ihrm.util

object AuthManager {
    private var isLoggedIn: Boolean = false

    fun setLoggedIn(loggedIn: Boolean) {
        isLoggedIn = loggedIn
    }

    fun isUserLoggedIn(): Boolean = isLoggedIn
}